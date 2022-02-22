package no.nav.sosialhjelp.sosialhjelpfssproxy.krr

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class KrrRouter(
    private val krrHandler: KrrHandler
) {

    fun krrRoutes() = coRouter {
        path("/krr").nest {
            GET("/rest/v1/person", krrHandler::getDigitalKontaktinformasjon)
        }
    }
}
