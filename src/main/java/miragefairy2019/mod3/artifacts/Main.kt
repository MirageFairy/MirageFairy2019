package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.module
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Main {
    private val logger: Logger = LogManager.getLogger(javaClass)
    val module = module {
        onConstruction {
            logger.info("Mod Version: $modVersion; Use Pre-Release Features: $usePreReleaseFeatures;")
        }
    }
}
