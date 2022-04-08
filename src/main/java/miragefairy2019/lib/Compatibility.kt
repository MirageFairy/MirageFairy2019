package miragefairy2019.lib

import mirrg.kotlin.getClass

object Compatibility {
    val ae2 by lazy { getClass("appeng.core.Api") != null }
}
