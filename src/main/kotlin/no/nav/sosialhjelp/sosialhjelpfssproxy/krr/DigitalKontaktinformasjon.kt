package no.nav.sosialhjelp.sosialhjelpfssproxy.krr

data class DigitalKontaktinformasjon(
    val personident: String,
    val aktiv: Boolean,
    val kanVarsles: Boolean?,
    val reservert: Boolean?,
    val mobiltelefonnummer: String?
)
