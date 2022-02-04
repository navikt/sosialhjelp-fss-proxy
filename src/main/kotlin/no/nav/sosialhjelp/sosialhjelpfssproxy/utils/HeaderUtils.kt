package no.nav.sosialhjelp.sosialhjelpfssproxy.utils

import org.springframework.web.reactive.function.server.ServerRequest
import java.security.SecureRandom

object HeaderUtils {

    const val HEADER_CALL_ID = "Nav-Call-Id"
    const val HEADER_CONSUMER_ID = "Nav-Consumer-Id"

    private val RANDOM = SecureRandom()

    fun getCallId(serverRequest: ServerRequest? = null) =
        serverRequest?.headers()?.firstHeader(HEADER_CALL_ID) ?: generateCallId()

    fun getConsumerId(serverRequest: ServerRequest? = null) =
        serverRequest?.headers()?.firstHeader(HEADER_CONSUMER_ID) ?: "sosialhjelp-fss-proxy"

    private fun generateCallId(): String {
        val randomNr = RANDOM.nextInt(Integer.MAX_VALUE)
        val systemTime = System.currentTimeMillis()
        return "CallId_${systemTime}_$randomNr"
    }
}
