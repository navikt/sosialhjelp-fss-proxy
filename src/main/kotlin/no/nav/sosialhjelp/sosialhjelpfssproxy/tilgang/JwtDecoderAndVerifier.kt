package no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.SigningKeyNotFoundException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.TilgangsException
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.server.ServerRequest
import java.security.interfaces.RSAPublicKey

fun decodeAndVerifyJWT(
    jwkProvider: JwkProvider,
    serverRequest: ServerRequest,
    issuer: String,
    audience: String,
    cookieName: String = "localhost-idtoken",
): DecodedJWT {
    val tokenString = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION)?.replace("Bearer ", "")
        ?: serverRequest.cookies()[cookieName]?.get(0)?.value
        ?: throw TilgangsException("Token not found.")

    val jwt = JWT.decode(tokenString)

    if (jwt.algorithm != "RS256") throw TilgangsException("Bad algorithm for jwt \"${jwt.algorithm}\" != \"RS256\"")
    val jwk = try {
        jwkProvider.get(jwt.keyId)
    } catch (e: SigningKeyNotFoundException) {
        if (e.cause?.message?.startsWith("com.auth0.jwk.NetworkException:") == true) {
            throw e.cause!!
        }
        throw TilgangsException("JWKS mangler key id (\"${jwt.keyId}\") fra jwt.")
    }
    val publicKey = jwk.publicKey as? RSAPublicKey ?: throw TilgangsException("Invalid key type in jwk.")
    val verifier = JWT.require(Algorithm.RSA256(publicKey, null))
        .withIssuer(issuer)
        .withAudience(audience)
        .build()
    val verifiedToken = try {
        verifier.verify(tokenString)
    } catch (e: SignatureVerificationException) {
        throw TilgangsException(e.message ?: "Signature verification failed.")
    } catch (e: TokenExpiredException) {
        throw TilgangsException(e.message ?: "Token expired.")
    } catch (e: JWTVerificationException) {
        val config = "\nWellKnown issuer: $issuer JWT issuer: ${jwt.issuer}" +
            "\nKonfig audience: $audience JWT audience = ${jwt.audience}"
        throw TilgangsException((e.message ?: "Jwt token validation error without message.") + config)
    }
    return verifiedToken
}

