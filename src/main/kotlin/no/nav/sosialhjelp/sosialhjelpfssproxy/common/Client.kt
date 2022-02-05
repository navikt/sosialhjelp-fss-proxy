package no.nav.sosialhjelp.sosialhjelpfssproxy.common

interface Client {
    suspend fun ping()
}
