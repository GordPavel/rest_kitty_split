package ru.sau.kitty_split.configuration

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType.DIFFERENT
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver
import org.springframework.boot.actuate.endpoint.web.EndpointMapping
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.util.StringUtils.hasText
import ru.sau.kitty_split.KittySplitApplication
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType.SWAGGER_2
import springfox.documentation.spring.web.plugins.Docket
import java.util.Collections

@Configuration
@ConditionalOnProperty("spring.swagger.enabled")
class SwaggerConfiguration {
    @Bean
    fun api(): Docket = Docket(SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(KittySplitApplication::class.java.getPackage().name))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())

    @Bean
    fun webEndpointServletHandlerMapping(
        webEndpointsSupplier: WebEndpointsSupplier,
        servletEndpointsSupplier: ServletEndpointsSupplier,
        controllerEndpointsSupplier: ControllerEndpointsSupplier,
        endpointMediaTypes: EndpointMediaTypes,
        corsProperties: CorsEndpointProperties,
        webEndpointProperties: WebEndpointProperties,
        environment: Environment
    ): WebMvcEndpointHandlerMapping {
        val webEndpoints = webEndpointsSupplier.endpoints
        val allEndpoints = listOf(
            webEndpoints,
            servletEndpointsSupplier.endpoints,
            controllerEndpointsSupplier.endpoints,
        ).flatten()
        val basePath = webEndpointProperties.basePath
        return WebMvcEndpointHandlerMapping(
            EndpointMapping(basePath),
            webEndpoints,
            endpointMediaTypes,
            corsProperties.toCorsConfiguration(),
            EndpointLinksResolver(allEndpoints, basePath),
            shouldRegisterLinksMapping(webEndpointProperties, environment, basePath),
            null,
        )
    }

    private fun shouldRegisterLinksMapping(
        webEndpointProperties: WebEndpointProperties,
        environment: Environment,
        basePath: String
    ): Boolean = webEndpointProperties.discovery.isEnabled
            && (hasText(basePath) || ManagementPortType.get(environment) == DIFFERENT)

    private fun apiInfo(): ApiInfo = ApiInfo(
        "Kitty split REST API",
        "",
        "",
        "",
        null,
        "",
        "",
        Collections.emptyList(),
    )
}