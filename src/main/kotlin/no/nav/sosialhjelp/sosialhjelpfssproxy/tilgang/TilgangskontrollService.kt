package no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang

import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.TilgangsException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class TilgangskontrollService(
    private val jwkProvider: ProxyJwkProvider,
    @Value("\${fss-proxy.tokendings_client_id}") private val audience: String,
) {

    fun verifyToken(request: ServerRequest) {
        val verifiedToken = decodeAndVerifyJWT(
            jwkProvider.jwkProvider!!,
            request,
            jwkProvider.wellKnown!!.issuer,
            audience
        )
        request.attributes()["verifiedToken"] = verifiedToken.token
    }
}

fun getTokenFromServerRequest(serverRequest: ServerRequest): String {
    val value = serverRequest.attributes()["verifiedToken"] ?: throw TilgangsException("Token is not set in serverRequest!")
    return value as String
}
