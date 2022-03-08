package no.nav.sosialhjelp.sosialhjelpfssproxy.aareg

import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getNavPersonident
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class AaregHandler(
    private val aaregClient: AaregClient
) {
    suspend fun getArbeidsforhold(request: ServerRequest): ServerResponse {
        val ident = getNavPersonident(request)
        val arbeidsforhold = aaregClient.getArbeidsforhold(ident, request)
        return arbeidsforhold
            ?.let { ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }
}
