package no.nav.sosialhjelp.sosialhjelpfssproxy.internal

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class SelftestHandler(
    private val selftestService: SelftestService
) {

    suspend fun isAlive(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValueAndAwait("OK")
    }

    suspend fun isReady(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValueAndAwait("OK")
    }

    suspend fun getSelftest(request: ServerRequest): ServerResponse {
        val result = selftestService.pingAll()
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(result)
    }
}
