Dette repoet er arkivert, og appen er skrudd av ettersom den ikke trengs lengre.

# sosialhjelp-fss-proxy
Proxy-app for å nå tjenester on-prem fra GCP (med tokenX)

## Henvendelser
Spørsmål knyttet til koden eller teamet kan stilles til teamdigisos@nav.no.

### For NAV-ansatte
Interne henvendelser kan sendes via Slack i kanalen #team_digisos.

## Teknologi
* Kotlin
* JDK 17
* Gradle
* Spring boot (reactive)

### Krav
* JDK 17


## Endepunkter

### Krr
Midlertidig proxy for sosialhjelp-soknad-api i sbs-clustre.
`GET /proxy/krr/rest/v1/person`

### Aareg
Midlertidig proxy for sosialhjelp-soknad-api i sbs-clustre.
`GET /proxy/aareg/v1/arbeidstaker/arbeidsforhold`

### Ping
`OPTIONS /ping`

## Annet

### Ktlint
Ktlint brukes for linting av kotlin-kode, vha [ktlint-gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle)\
Sjekk kode: `./gradlew ktlintCheck`\
Formater kode: `./gradlew ktlintFormat`

Man kan endre IntelliJ autoformateringskonfigurasjon for det aktuelle prosjektet: `./gradlew ktlintApplyToIdea`

Installere pre-commit hook lokalt i aktuelt repo, som enten sjekker eller formatterer koden:\
`./gradlew addKtlintCheckGitPreCommitHook` eller `./gradlew addKtlintFormatGitPreCommitHook`
