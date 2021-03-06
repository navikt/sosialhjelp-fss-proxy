package no.nav.sosialhjelp.sosialhjelpfssproxy.tilgang

import com.fasterxml.jackson.annotation.JsonProperty

data class WellKnown(
    val issuer: String,
    @JsonProperty("authorization_endpoint")
    val authorizationEndpoint: String?,
    @JsonProperty("token_endpoint")
    val tokenEndpoint: String,
    @JsonProperty("jwks_uri")
    val jwksUri: String
)
