import io.wusa.GitService.Companion.lastTag
import io.wusa.RegexResolver.Companion.findMatchingRegex
import io.wusa.SemanticVersionFactory
import io.wusa.TagType.LIGHTWEIGHT
import io.wusa.extension.SemverGitPluginExtension
import io.wusa.extension.SemverGitPluginExtension.Companion.DEFAULT_INCREMENTER
import io.wusa.incrementer.VersionIncrementer.Companion.getVersionIncrementerByName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("io.wusa.semver-git-plugin") version ("2.3.7")
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    kotlin("plugin.jpa") version "1.7.0"
    kotlin("kapt") version "1.7.0"
}

group = "ru.sau"
java.sourceCompatibility = JavaVersion.VERSION_17

extra["springCloudVersion"] = "2021.0.4"

repositories {
    mavenCentral()
}

dependencies {
    val mapstructVersion = "1.5.2.Final"
    val kotlinLoggingVersion = "2.1.23"
    val caffeineVersion = "3.1.1"
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.vladmihalcea:hibernate-types-55:2.20.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk:1.12.5")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
}

val semver = project.extensions.getByType(SemverGitPluginExtension::class)
version = ""

semver {
    snapshotSuffix = ""
    dirtyMarker = ""
    initialVersion = "0.1.0"
    tagType = LIGHTWEIGHT
    branches {
        branch {
            regex = "master"
            incrementer = "CONVENTIONAL_COMMITS_INCREMENTER"
            formatter = Transformer {
                "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}"
            }
        }
        branch {
            regex = ".+"
            incrementer = "NO_VERSION_INCREMENTER"
            formatter = Transformer {
                "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}-${semver.info.branch.name}.build.${semver.info.count}"
            }
        }
    }
}

tasks.register("incrementVersion") {
    val semanticVersionFactory = SemanticVersionFactory()
    val tagPrefix = semver.tagPrefix
    val lastTag = lastTag(project, tagPrefix, tagType = semver.tagType)
    val incrementer = findMatchingRegex(semver.branches, semver.info.branch.name)
        ?.let { getVersionIncrementerByName(it.incrementer) }
        ?: getVersionIncrementerByName(DEFAULT_INCREMENTER)
    incrementer.increment(semanticVersionFactory.createFromString(lastTag.substring(tagPrefix.length)), project)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}
