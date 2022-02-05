package no.nav.sosialhjelp.sosialhjelpfssproxy.kodeverk

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class KodeverkRouter(
    private val kodeverkHandler: KodeverkHandler
) {

    fun kodeverkRoutes() = coRouter {
        path("/kodeverk").nest {
            GET("/{kodeverksnavn}", kodeverkHandler::getKodeverk)
        }
    }
}
