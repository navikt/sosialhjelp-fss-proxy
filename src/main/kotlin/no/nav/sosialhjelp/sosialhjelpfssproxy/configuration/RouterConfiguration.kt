package no.nav.sosialhjelp.sosialhjelpfssproxy.configuration

import no.nav.sosialhjelp.sosialhjelpfssproxy.internal.SelftestRouter
import no.nav.sosialhjelp.sosialhjelpfssproxy.proxy.ProxyRouter
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfiguration(
    private val selftestRouter: SelftestRouter,
    private val proxyRouter: ProxyRouter
) {
    companion object {
        private val log by logger()
    }

    @Bean
    fun routerConfig() = coRouter {
        add(selftestRouter.statusRoutes())
        add(proxyRouter.proxyRoutes())
        path("/**", logAndReturn404())
        onError<Throwable> { error, _ ->
            log.error("Internal server error!", error.message)
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValueAndAwait("Noe uventet feilet")
        }
    }

    fun logAndReturn404(): suspend (ServerRequest) -> ServerResponse =
        { serverRequest ->
            log.error("Unhandled request method: ${serverRequest.methodName()} path: \"${serverRequest.requestPath()}\"")
            ServerResponse.status(HttpStatus.NOT_FOUND).bodyValueAndAwait("Ukjent URL")
        }
}

@Suppress("unused")
open class FrontendErrorMessage(
    val type: String?,
    val message: String?
)

@Suppress("unused")
class FrontendUnauthorizedMessage(
    val id: String = "azuread_authentication_error",
    type: String = "azuread_authentication_error",
    message: String = "Autentiseringsfeil",
) : FrontendErrorMessage(type, message)
