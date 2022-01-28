object Kotlin {
    const val version = "1.6.10"
}

object VersionsPlugin {
    const val version = "0.41.0"
}

object KtlintPlugin {
    const val version = "10.2.1"
}

object Ktlint {
    const val version = "0.43.2"
}

object SpringBoot {
    private const val groupId = "org.springframework.boot"
    const val version = "2.6.3"

    const val webflux = "$groupId:spring-boot-starter-webflux:$version"
    const val actuator = "$groupId:spring-boot-starter-actuator:$version"

    const val test = "$groupId:spring-boot-starter-test:$version"
}

object Auth0 {
    private const val javaJwtVersion = "3.18.3"
    const val javaJwt = "com.auth0:java-jwt:$javaJwtVersion"

    private const val jwksRsaVersion = "0.20.1"
    const val jwksRsa = "com.auth0:jwks-rsa:$jwksRsaVersion"
}

object Micrometer {
    private const val version = "1.8.2"
    const val registryPrometheus = "io.micrometer:micrometer-registry-prometheus:$version"
}

object Junit {
    private const val version = "5.8.2"
    const val jupiter = "org.junit.jupiter:junit-jupiter:$version"
}

object Mockk {
    private const val version = "1.12.2"
    const val mockk = "io.mockk:mockk:$version"
}
