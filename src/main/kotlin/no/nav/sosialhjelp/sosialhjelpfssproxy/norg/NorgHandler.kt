package no.nav.sosialhjelp.sosialhjelpfssproxy.norg

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class NorgHandler(
    private val norgClient: NorgClient
) {

    suspend fun getEnhet(request: ServerRequest): ServerResponse {
        val enhetsnr = request.pathVariable("enhetsnr")
        val navEnhet = norgClient.hentNavEnhet(enhetsnr)
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(navEnhet)
    }

    suspend fun ping(request: ServerRequest): ServerResponse {
        norgClient.ping()
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValueAndAwait("OK")
    }
}