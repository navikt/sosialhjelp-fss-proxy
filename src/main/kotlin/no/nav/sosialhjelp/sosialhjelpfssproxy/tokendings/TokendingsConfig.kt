package no.nav.sosialhjelp.sosialhjelpfssproxy.tokendings

import no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang.WellKnown
import no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang.downloadWellKnown
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class TokendingsConfig(
    private val webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.tokendings_url}") val tokendingsUrl: String,
) {

    private val tokendingsWebClient: WebClient
        get() = webClientBuilder.build()

    private val wellKnown: WellKnown
        get() = downloadWellKnown(tokendingsUrl)

    @Profile("!test")
    @Bean
    fun tokendingsClient(): TokendingsClient {
        log.info("creating tokendingsClient")
        return TokendingsClient(tokendingsWebClient, wellKnown)
    }

    @Profile("test")
    @Bean
    fun tokendingsClientTest(): TokendingsClient {
        log.info("creating tokendingsClient test")
        return TokendingsClient(
            tokendingsWebClient,
            WellKnown("iss-localhost", "authorizationEndpoint", "tokenEndpoint", tokendingsUrl)
        )
    }

    companion object {
        private val log by logger()
    }
}
