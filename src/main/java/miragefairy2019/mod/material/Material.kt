package miragefairy2019.mod.material

import miragefairy2019.lib.modinitializer.module

val materialModule = module {
    commonMaterialsModule(this)
    CompressedMaterials.compressedMaterialsModule(this)
    Ores.oresModule(this)
}
