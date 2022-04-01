import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val springboot = "2.6.6"
    const val kotlin = "1.6.10"
    const val coroutines = "1.6.0"
    const val jackson = "2.13.2"
    const val micrometer = "1.8.4"
    const val javaJwt = "3.18.3"
    const val jwksRsa = "0.20.2"
    const val logstash = "7.0.1"
    const val nimbusJoseJwt = "9.21"

    // test
    const val junit = "5.8.2"
    const val mockk = "1.12.3"
}

plugins {
    id("org.springframework.boot") version "2.6.6"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = "no.nav.sosialhjelp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

ktlint {
    this.version.set("0.45.1")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.springframework.boot:spring-boot-starter-webflux:${Versions.springboot}")
    implementation("org.springframework.boot:spring-boot-starter-actuator:${Versions.springboot}")
    implementation("io.micrometer:micrometer-registry-prometheus:${Versions.micrometer}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${Versions.coroutines}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")
    implementation("com.auth0:java-jwt:${Versions.javaJwt}")
    implementation("com.auth0:jwks-rsa:${Versions.jwksRsa}")
    implementation("com.nimbusds:nimbus-jose-jwt:${Versions.nimbusJoseJwt}")
    implementation("net.logstash.logback:logstash-logback-encoder:${Versions.logstash}")

    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit}")
    testImplementation("io.mockk:mockk:${Versions.mockk}")
    testImplementation("org.springframework.boot:spring-boot-starter-test:${Versions.springboot}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    this.archiveFileName.set("app.jar")
}
