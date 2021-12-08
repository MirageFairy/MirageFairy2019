package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module

object Artifacts {
    val module: Module = {
        Dish.module(this)
        Fertilizer.module(this)
        TwinkleStone.module(this)
        FairyCollectionBox.module(this)
    }
}