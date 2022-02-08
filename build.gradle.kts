import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val micrometer = "1.8.2"
    const val javaJwt = "3.18.2"
    const val jwksRsa = "0.20.0"
    const val logstash = "7.0.1"

    // test
    const val junit = "5.8.2"
    const val mockk = "1.12.2"
}

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
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
    this.version.set("0.43.2")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:${Versions.micrometer}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.auth0:java-jwt:${Versions.javaJwt}")
    implementation("com.auth0:jwks-rsa:${Versions.jwksRsa}")
    implementation("net.logstash.logback:logstash-logback-encoder:${Versions.logstash}")

    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit}")
    testImplementation("io.mockk:mockk:${Versions.mockk}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
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
