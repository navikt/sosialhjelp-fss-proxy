package no.nav.sosialhjelp.sosialhjelpfssproxy.exceptions

class NorgException(override val message: String?, override val cause: Throwable?) : RuntimeException(message, cause)