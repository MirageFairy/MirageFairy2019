package mirrg.kotlin.log4j.hydrogen

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun <T : Any> T.getLogger() = getLogger(javaClass)
fun getLogger(clazz: Class<*>): Logger = LogManager.getLogger(clazz)
