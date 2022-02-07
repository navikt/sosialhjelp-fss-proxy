package no.nav.sosialhjelp.sosialhjelpfssproxy.ereg

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class EregHandler(
    private val eregClient: EregClient
) {

    suspend fun getOrganisasjon(request: ServerRequest): ServerResponse {
        val orgnr = request.pathVariable("orgnr")
        val organisasjon = eregClient.getOrganisasjon(orgnr, request)
        return organisasjon?.let { ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }
}
