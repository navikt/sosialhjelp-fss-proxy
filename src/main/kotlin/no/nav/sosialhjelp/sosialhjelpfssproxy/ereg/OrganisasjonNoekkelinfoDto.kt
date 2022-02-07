package no.nav.sosialhjelp.sosialhjelpfssproxy.ereg

data class OrganisasjonNoekkelinfoDto(
    val navn: NavnDto,
    val organisasjonsnummer: String
)

data class NavnDto(
    val navnelinje1: String?,
    val navnelinje2: String?,
    val navnelinje3: String?,
    val navnelinje4: String?,
    val navnelinje5: String?
)
