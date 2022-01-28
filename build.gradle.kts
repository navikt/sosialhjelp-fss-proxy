import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version SpringBoot.version
    kotlin("jvm") version Kotlin.version
    kotlin("plugin.spring") version Kotlin.version
    id("org.jlleitschuh.gradle.ktlint") version KtlintPlugin.version
    id("com.github.ben-manes.versions") version VersionsPlugin.version
}

group = "no.nav.sosialhjelp"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

ktlint {
    this.version.set(Ktlint.version)
}

dependencies {
    implementation(kotlin("reflect"))

    implementation(SpringBoot.webflux)
    implementation(SpringBoot.actuator)
    implementation(Micrometer.registryPrometheus)
    implementation(Auth0.javaJwt)
    implementation(Auth0.jwksRsa)

    testImplementation(Junit.jupiter)
    testImplementation(Mockk.mockk)
    testImplementation(SpringBoot.test)
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
