package no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang

import com.auth0.jwk.JwkProvider
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

open class ProxyJwkProvider {
    var jwkProvider: JwkProvider? = null
        protected set
    var wellKnown: WellKnown? = null
        protected set
}

@Component
@Profile("!test")
class ProxyJwkProviderImpl(
    @Value("\${fss-proxy.tokenx_metadata_url}") private val metadata_url: String,
) : ProxyJwkProvider() {
    init {
        val downloadedWellKnown = downloadWellKnown(metadata_url)
        log.info("ProxyJwkProviderImpl: Lastet ned well known fra: $metadata_url bruker jwks URI: ${downloadedWellKnown.jwksUri}")
        wellKnown = downloadedWellKnown
        jwkProvider = buildJwkProvider(downloadedWellKnown.jwksUri)
    }

    companion object {
        private val log by logger()
    }
}

@Component
@Profile("test")
class TestProxyJwkProvider(
    @Value("\${fssProxy.bruker_metadata_url}") private val jwkProviderUrl: String,
) : ProxyJwkProvider() {
    init {
        // Henter ikke ned well known konfigurasjon.
        log.info("JwkProvider: Test provider som bruker jwks URI: $jwkProviderUrl")
        wellKnown = WellKnown("iss-localhost", "authorizationEndpoint", "tokenEndpoint", jwkProviderUrl)
        jwkProvider = buildJwkProvider(jwkProviderUrl)
    }

    companion object {
        private val log by logger()
    }
}
