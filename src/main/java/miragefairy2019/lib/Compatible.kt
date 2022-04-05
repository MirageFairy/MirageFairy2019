package miragefairy2019.lib

import mirrg.kotlin.getClass

object Installed {
    val ae2 by lazy { getClass("appeng.core.Api") != null }
}
