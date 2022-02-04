package no.nav.sosialhjelp.sosialhjelpfssproxy.utils

import org.springframework.web.reactive.function.server.ServerRequest
import java.security.SecureRandom

object HeaderUtils {

    const val HEADER_CALL_ID = "Nav-Call-Id"

    private val RANDOM = SecureRandom()

    fun getCallId(serverRequest: ServerRequest? = null) =
        serverRequest?.headers()?.firstHeader(HEADER_CALL_ID) ?: generateCallId()

    private fun generateCallId(): String {
        val randomNr = RANDOM.nextInt(Integer.MAX_VALUE)
        val systemTime = System.currentTimeMillis()
        return "CallId_${systemTime}_$randomNr"
    }
}
