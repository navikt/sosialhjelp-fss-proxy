package no.nav.sosialhjelp.sosialhjelpfssproxy.aareg

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class AaregRouter(
    private val aaregHandler: AaregHandler
) {
    fun aaregRoutes() = coRouter {
        path("/aareg").nest {
            GET("/v1/arbeidstaker/arbeidsforhold", aaregHandler::getArbeidsforhold)
        }
    }
}
