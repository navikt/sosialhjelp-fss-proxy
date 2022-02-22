package no.nav.sosialhjelp.sosialhjelpfssproxy.tokendings

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang.getTokenFromServerRequest
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.isRunningInProd
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.server.ServerRequest
import java.time.Instant
import java.util.Date
import java.util.UUID

@Component
class TokendingsService(
    @Value("\${fss-proxy.tokendings_client_id}") private val clientId: String,
    @Value("\${fss-proxy.tokendings_private_jwk}") private val privateJwk: String,
    private val tokendingsClient: TokendingsClient
) {

    private val privateRsaKey: RSAKey = if (privateJwk == "generateRSA") {
        if (isRunningInProd()) throw RuntimeException("Generation of RSA keys is not allowed in prod.")
        RSAKeyGenerator(2048).keyUse(KeyUse.SIGNATURE).keyID(UUID.randomUUID().toString()).generate()
    } else {
        RSAKey.parse(privateJwk)
    }

    suspend fun exchangeToken(request: ServerRequest, audience: String): String {
        log.info("exchanging token for audience: $audience")
        val token = getTokenFromServerRequest(request)
        val jwt = createSignedAssertion(clientId, tokendingsClient.audience, privateRsaKey)
        return try {
            tokendingsClient.exchangeToken(token, jwt, audience).accessToken
        } catch (e: WebClientResponseException) {
            log.warn("Error message from server: ${e.responseBodyAsString}")
            throw e
        }
    }

    companion object {
        private val log by logger()
    }
}

fun createSignedAssertion(clientId: String, audience: String, rsaKey: RSAKey): String {
    val now = Instant.now()
    return JWT.create()
        .withSubject(clientId)
        .withIssuer(clientId)
        .withAudience(audience)
        .withIssuedAt(Date.from(now))
        .withNotBefore(Date.from(now))
        .withExpiresAt(Date.from(now.plusSeconds(60)))
        .withJWTId(UUID.randomUUID().toString())
        .withKeyId(rsaKey.keyID)
        .sign(Algorithm.RSA256(null, rsaKey.toRSAPrivateKey()))
}
