package no.nav.sosialhjelp.sosialhjelpfssproxy.krr

import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.HeaderUtils.getNavPersonident
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class KrrHandler(
    private val krrClient: KrrClient
) {

    suspend fun getDigitalKontaktinformasjon(request: ServerRequest): ServerResponse {
        val ident = getNavPersonident(request)
        val digitalKontaktinformasjon = krrClient.getDigitalKontaktinformasjon(ident, request)
        return digitalKontaktinformasjon
            ?.let { ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()
    }
}
