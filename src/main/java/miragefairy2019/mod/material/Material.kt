package miragefairy2019.mod.material

import miragefairy2019.libkt.module

val materialModule = module {
    CommonMaterials.commonMaterialsModule(this)
    CompressedMaterials.compressedMaterialsModule(this)
    Ores.oresModule(this)
}
