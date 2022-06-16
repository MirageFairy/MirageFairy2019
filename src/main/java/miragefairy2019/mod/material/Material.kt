package miragefairy2019.mod.material

import miragefairy2019.libkt.module

object Material {
    val module = module {
        CommonMaterials.module(this)
        CompressedMaterials.module(this)
        Ores.module(this)
    }
}
