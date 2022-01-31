package no.nav.sosialhjelp.sosialhjelpfssproxy.internal

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class SelftestHandler() {

    suspend fun isAlive(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValueAndAwait("OK")
    }

    suspend fun isReady(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValueAndAwait("OK")
    }
}
