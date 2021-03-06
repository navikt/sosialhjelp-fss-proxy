package no.nav.sosialhjelp.sosialhjelpfssproxy.proxy

import no.nav.sosialhjelp.sosialhjelpfssproxy.aareg.AaregRouter
import no.nav.sosialhjelp.sosialhjelpfssproxy.configuration.FrontendErrorMessage
import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.ServiceException
import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.TilgangForbudtException
import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.TilgangsException
import no.nav.sosialhjelp.sosialhjelpfssproxy.krr.KrrRouter
import no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang.TilgangskontrollService
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Component
class ProxyRouter(
    private val tilgangskontrollService: TilgangskontrollService,
    private val krrRouter: KrrRouter,
    private val aaregRouter: AaregRouter,
) {
    companion object {
        private val log by logger()
    }

    fun proxyRoutes() = coRouter {
        path("/proxy").nest {
            add(krrRouter.krrRoutes())
            add(aaregRouter.aaregRoutes())
            filter { serverRequest, next ->
                try {
                    tilgangskontrollService.verifyToken(serverRequest)
                    next(serverRequest)
                } catch (e: TilgangsException) {
                    log.error("Intern tilgangskontroll feil! ${e.message} Url: ${serverRequest.path()}")
                    status(HttpStatus.UNAUTHORIZED).bodyValueAndAwait("Unauthorized")
                }
            }
        }
        onError<Throwable> { error, _ ->
            when (error) {
                is ServiceException -> {
                    log.info("Servicefeil under /system: ${error.message}")
                    ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .bodyValueAndAwait(FrontendErrorMessage("Servicefeil", error.message))
                }
                is TilgangForbudtException -> {
                    log.info("Tilgang forbudt /system: ${error.message}")
                    ServerResponse.status(HttpStatus.FORBIDDEN)
                        .bodyValueAndAwait("Unauthorized")
                }
                else -> {
                    log.error("Internal server error under /system", error)
                    ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValueAndAwait("Noe uventet feilet")
                }
            }
        }
    }
}
