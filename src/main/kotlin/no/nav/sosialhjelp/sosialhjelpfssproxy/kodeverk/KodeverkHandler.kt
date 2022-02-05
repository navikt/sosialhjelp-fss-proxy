package no.nav.sosialhjelp.sosialhjelpfssproxy.kodeverk

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class KodeverkHandler(
    private val kodeverkClient: KodeverkClient
) {

    suspend fun getKodeverk(request: ServerRequest): ServerResponse {
        val kodeverksnavn = request.pathVariable("kodeverksnavn")
        val kodeverkDto = kodeverkClient.getKodeverk(kodeverksnavn, request)
        return kodeverkDto?.let { ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }
}
