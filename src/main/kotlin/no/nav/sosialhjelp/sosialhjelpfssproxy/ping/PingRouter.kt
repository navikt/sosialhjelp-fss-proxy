package no.nav.sosialhjelp.sosialhjelpfssproxy.ping

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class PingRouter(
    private val pingHandler: PingHandler
) {

    fun statusRoutes() = coRouter {
        OPTIONS("/ping", pingHandler::ping)
    }
}
