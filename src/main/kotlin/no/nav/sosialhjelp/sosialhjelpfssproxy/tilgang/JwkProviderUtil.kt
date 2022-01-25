package no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang

import com.auth0.jwk.JwkProviderBuilder
import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.TilgangsException
import org.springframework.web.reactive.function.client.WebClient
import java.net.URL
import java.util.concurrent.TimeUnit

fun downloadWellKnown(url: String): WellKnown =
    WebClient.create()
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(WellKnown::class.java)
        .block()
        ?: throw TilgangsException("Feiler under henting av well-known konfigurasjon fra $url")

fun buildJwkProvider(wellKnownJwksUri: String) =
    JwkProviderBuilder(URL(wellKnownJwksUri))
        // cache up to 10 JWKs for 24 hours
        .cached(10, 24, TimeUnit.HOURS)
        // if not cached, only allow max 10 different keys per minute to be fetched from external provider
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()
