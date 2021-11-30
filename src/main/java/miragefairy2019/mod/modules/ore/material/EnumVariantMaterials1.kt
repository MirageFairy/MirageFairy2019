package miragefairy2019.mod.modules.ore.material

import net.minecraft.block.SoundType
import net.minecraft.util.IStringSerializable
import miragefairy2019.mod.lib.multi.IListBlockVariant
import net.minecraft.block.material.Material

enum class EnumVariantMaterials1(
    @JvmField val metadata: Int,
    @JvmField val resourceName: String,
    @JvmField val unlocalizedName: String,
    val oreName: String,
    @JvmField val blockHardness: Float,
    @JvmField val harvestTool: String,
    @JvmField val harvestLevel: Int,
    @JvmField val burnTime: Int,
    @JvmField val soundType: SoundType,
    @JvmField val fallable: Boolean,
    @JvmField val material: Material
) : IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", 5f, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON),
    SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", 5f, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON),
    MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", 7f, "pickaxe", 3, 0, SoundType.STONE, false, Material.IRON),
    MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", 5f, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON),
    SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", 3f, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", 5f, "pickaxe", 0, 20 * 10 * 8 * 9, SoundType.STONE, false, Material.ROCK),
    MIRAGE_FLOWER_LEAF_BLOCK(9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", 2f, "axe", 0, 0, SoundType.GLASS, false, Material.LEAVES),
    MIRAGIUM_INGOT_BLOCK(10, "miragium_ingot_block", "blockMiragium", "blockMiragium", 5f, "pickaxe", 1, 0, SoundType.METAL, false, Material.IRON),
    MIRAGIUM_DUST_BLOCK(11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", 0.5f, "shovel", 0, 0, SoundType.SNOW, true, Material.SAND),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName
    override fun getMetadata() = metadata
    override fun getResourceName() = resourceName
    override fun getUnlocalizedName() = unlocalizedName
    override fun getBlockHardness() = blockHardness
    override fun getHarvestTool() = harvestTool
    override fun getHarvestLevel() = harvestLevel
    override fun getBurnTime() = burnTime
    override fun getSoundType() = soundType
    override fun isFallable() = fallable
    override fun getMaterial() = material

    companion object {
        val variantList: IListBlockVariant<EnumVariantMaterials1> = object : IListBlockVariant<EnumVariantMaterials1> {
            private val values = values().toList()
            private val metaLookup = mutableMapOf<Int, EnumVariantMaterials1>().also { map ->
                values().forEach { variant ->
                    require(!map.containsKey(variant.metadata))
                    map[variant.metadata] = variant
                }
            }

            override fun byMetadata(metadata: Int) = metaLookup[metadata] ?: values[0]
            override fun getBlockVariants() = values
        }
    }
}
