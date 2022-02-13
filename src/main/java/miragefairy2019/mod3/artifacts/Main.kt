package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Main {
    private val logger: Logger = LogManager.getLogger(javaClass)
    val module: Module = {
        onConstruction {
            logger.info("Mod Version: $modVersion; Use Pre-Release Features: $usePreReleaseFeatures;")
        }
    }
}
