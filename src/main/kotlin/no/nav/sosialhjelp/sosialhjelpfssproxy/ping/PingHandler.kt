package no.nav.sosialhjelp.sosialhjelpfssproxy.ping

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class PingHandler {

    suspend fun ping(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValueAndAwait("OK")
    }
}
