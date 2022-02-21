package no.nav.sosialhjelp.sosialhjelpfssproxy.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

fun <R : Any> R.logger(): Lazy<Logger> {
    return lazy { LoggerFactory.getLogger(unwrapCompanionClass(this.javaClass).name) }
}
// unwrap companion class to enclosing class given a Java Class
private fun <T : Any> unwrapCompanionClass(ofClass: Class<T>): Class<*> {
    return ofClass.enclosingClass?.takeIf {
        ofClass.enclosingClass.kotlin.companionObject?.java == ofClass
    } ?: ofClass
}

const val NAIS_CLUSTER_NAME = "NAIS_CLUSTER_NAME"

fun isRunningInProd(): Boolean {
    val clusterName = System.getenv(NAIS_CLUSTER_NAME)
    return clusterName != null && clusterName.contains("prod")
}
