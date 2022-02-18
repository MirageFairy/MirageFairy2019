package miragefairy2019.mod.modules.ore.material

import miragefairy2019.mod.lib.IBlockVariant
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

interface IBlockVariantMaterials : IBlockVariant {
    val blockHardness: Float
    val harvestTool: String
    val harvestLevel: Int
    val burnTime: Int
    val soundType: SoundType
    val isFallable: Boolean
    val material: Material?
    val isBeaconBase: Boolean
}
