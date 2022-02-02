package no.nav.sosialhjelp.sosialhjelpfssproxy.norg

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class NorgRouter(
    private val norgHandler: NorgHandler
) {

    fun norgRoutes() = coRouter {
        path("/norg").nest {
            GET("/enhet/{enhetsnr}", norgHandler::getEnhet)
            GET("/enhet/navkontor/{geografiskTilknytning}", norgHandler::getEnhetForGeografiskTilknytning)
            GET("/kodeverk/EnhetstyperNorg", norgHandler::ping)
            OPTIONS("/kodeverk/EnhetstyperNorg", norgHandler::ping)
        }
    }
}
