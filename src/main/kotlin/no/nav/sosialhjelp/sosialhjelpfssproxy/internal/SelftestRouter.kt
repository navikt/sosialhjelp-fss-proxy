package no.nav.sosialhjelp.sosialhjelpfssproxy.internal

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class SelftestRouter(
    private val selftestHandler: SelftestHandler
) {

    fun statusRoutes() = coRouter {
        path("/internal").nest {
            GET("/isAlive", selftestHandler::isAlive)
            GET("/isReady", selftestHandler::isReady)
        }
    }
}