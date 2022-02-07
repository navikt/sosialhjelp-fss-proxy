package no.nav.sosialhjelp.sosialhjelpfssproxy.ereg

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class EregRouter(
    private val eregHandler: EregHandler
) {
    fun eregRoutes() = coRouter {
        path("/ereg").nest {
            GET("/organisasjon/{orgnr}", eregHandler::getOrganisasjon)
        }
    }
}
