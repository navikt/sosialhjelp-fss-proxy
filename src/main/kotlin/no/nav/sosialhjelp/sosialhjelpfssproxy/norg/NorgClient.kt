package no.nav.sosialhjelp.sosialhjelpfssproxy.norg

import no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions.NorgException
import no.nav.sosialhjelp.sosialhjelpfssproxy.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody

@Component
class NorgClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${fss-proxy.norg_url}") private val norgUrl: String
) {
    private val norgWebClient = webClientBuilder.baseUrl(norgUrl).build()

    suspend fun hentNavEnhet(enhetsnr: String): NavEnhet {
        log.debug("Forsøker å hente NAV-enhet $enhetsnr fra NORG2")

        try {
            return norgWebClient.get()
                .uri("/enhet/{enhetsnr}", enhetsnr)
                .headers { it.addAll(headers()) }
                .retrieve()
                .awaitBody<NavEnhet>()
                .also { log.info("Hentet NAV-enhet $enhetsnr fra NORG2") }
        } catch (e: WebClientResponseException) {
            log.warn("Noe feilet ved kall mot NORG2 ${e.statusCode}", e)
            throw NorgException(e.message, e)
        }
    }

    suspend fun hentNavEnhetForGeografiskTilknytning(geografiskTilknytning: String): NavEnhet {
        log.debug("Forsøker å hente NAV-enhet for geografiskTilknytning=$geografiskTilknytning fra NORG2")

        try {
            return norgWebClient.get()
                .uri("/enhet/navkontor/{geografiskTilknytning}", geografiskTilknytning)
                .headers { it.addAll(headers()) }
                .retrieve()
                .awaitBody<NavEnhet>()
                .also { log.info("Hentet NAV-enhet for geografiskTilknytning=$geografiskTilknytning fra NORG2") }
        } catch (e: WebClientResponseException) {
            log.warn("Noe feilet ved kall mot NORG2 ${e.statusCode}", e)
            throw NorgException(e.message, e)
        }
    }

    // samme kall som selftest i soknad-api
    suspend fun ping() {
        norgWebClient.get()
            .uri("/kodeverk/EnhetstyperNorg")
            .headers { it.addAll(headers()) }
            .retrieve()
            .awaitBody<String>()
    }

    private fun headers(): HttpHeaders {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
//        headers.set(HEADER_CALL_ID, MDCUtils.get(MDCUtils.CALL_ID))
        return headers
    }

    companion object {
        private val log by logger()
    }
}
