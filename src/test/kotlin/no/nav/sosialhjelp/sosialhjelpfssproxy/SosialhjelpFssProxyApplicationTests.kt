package no.nav.sosialhjelp.sosialhjelpfssproxy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = ["test"])
internal class SosialhjelpFssProxyApplicationTests {

    @Test
    fun contextLoads() {
        assertThat("OK").isEqualTo("OK")
    }
}
