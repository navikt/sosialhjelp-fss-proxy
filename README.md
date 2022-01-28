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

## Annet

### Ktlint
Ktlint brukes for linting av kotlin-kode, vha [ktlint-gradle plugin](https://github.com/JLLeitschuh/ktlint-gradle)\
Sjekk kode: `./gradlew ktlintCheck`\
Formater kode: `./gradlew ktlintFormat`

Man kan endre IntelliJ autoformateringskonfigurasjon for det aktuelle prosjektet: `./gradlew ktlintApplyToIdea`

Installere pre-commit hook lokalt i aktuelt repo, som enten sjekker eller formatterer koden:\
`./gradlew addKtlintCheckGitPreCommitHook` eller `./gradlew addKtlintFormatGitPreCommitHook`

### Versions plugin
[gradle-versions-plugin](https://github.com/ben-manes/gradle-versions-plugin) kan brukes for å sjekke utdaterte avhengigheter.

`./gradlew dependencyUpdates`, evt `./gradlew dependencyUpdates -Drevision=release`