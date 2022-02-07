package no.nav.sosialhjelp.sosialhjelpfssproxy.internal

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.sosialhjelp.sosialhjelpfssproxy.common.Client
import no.nav.sosialhjelp.sosialhjelpfssproxy.internal.Status.OK
import no.nav.sosialhjelp.sosialhjelpfssproxy.internal.Status.WARNING
import org.springframework.stereotype.Component

@Component
class SelftestService(
    private val clients: List<Client>
) {

    suspend fun pingAll(): List<Result> {
        return coroutineScope {
            clients.map {
                async {
                    val start = System.currentTimeMillis()
                    try {
                        it.ping()
                        Result(OK, it.javaClass.name, System.currentTimeMillis() - start)
                    } catch (e: Exception) {
                        Result(WARNING, it.javaClass.name, System.currentTimeMillis() - start, e.message, e.cause)
                    }
                }.await()
            }
        }
    }
}

data class Result(
    val status: Status,
    val name: String,
    val responstid: Long,
    val message: String? = null,
    val error: Throwable? = null
)

enum class Status {
    OK, WARNING
}
