package miragefairy2019.lib

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun <T : Any> T.getLogger(`class`: Class<*>? = null): Logger = LogManager.getLogger(`class` ?: javaClass)
