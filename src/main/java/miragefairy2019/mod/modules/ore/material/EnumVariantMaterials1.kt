package miragefairy2019.mod.modules.ore.material

import miragefairy2019.mod.lib.BlockVariantList
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.IStringSerializable

enum class EnumVariantMaterials1(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val oreName: String,
    override val blockHardness: Float,
    override val harvestTool: String,
    override val harvestLevel: Int,
    override val burnTime: Int,
    override val soundType: SoundType,
    override val isFallable: Boolean,
    override val material: Material,
    override val isBeaconBase: Boolean
) : IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON, true),
    FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", 5f, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON, true),
    SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON, true),
    CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", 5f, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON, true),
    MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", 7f, "pickaxe", 3, 0, SoundType.STONE, false, Material.IRON, true),
    MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON, true),
    PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", 5f, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON, true),
    SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON, true),
    CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", 5f, "pickaxe", 0, 20 * 10 * 8 * 9, SoundType.STONE, false, Material.ROCK, false),
    MIRAGE_FLOWER_LEAF_BLOCK(9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", 2f, "axe", 0, 0, SoundType.GLASS, false, Material.LEAVES, false),
    MIRAGIUM_INGOT_BLOCK(10, "miragium_ingot_block", "blockMiragium", "blockMiragium", 5f, "pickaxe", 1, 0, SoundType.METAL, false, Material.IRON, false),
    MIRAGIUM_DUST_BLOCK(11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", 0.5f, "shovel", 0, 0, SoundType.SNOW, true, Material.SAND, false),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}
