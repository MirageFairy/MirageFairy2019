package miragefairy2019.mod.material

import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.libkt.BlockMulti
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.IBlockVariant
import miragefairy2019.libkt.ItemBlockMulti
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.util.ResourceLocation

lateinit var blockCreativeMaterials: () -> BlockCreativeMaterials
lateinit var itemBlockCreativeMaterials: () -> ItemBlockCreativeMaterials

val creativeMaterialsModule = module {

    // ブロック
    blockCreativeMaterials = block({ BlockCreativeMaterials() }, "creative_materials") {
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataModelBlockDefinition(
                variants = (0..15).associate { meta ->
                    val modelName = CreativeMaterialCard.values().getOrNull(meta)?.let { "miragefairy2019:${it.resourceName}" } ?: "minecraft:stone"
                    "variant=$meta" to DataSingleVariantList(DataVariant(model = modelName))
                }
            )
        }
    }

    // アイテム
    itemBlockCreativeMaterials = item({ ItemBlockCreativeMaterials(blockCreativeMaterials()) }, "creative_materials") {
        CreativeMaterialCard.values().forEach { creativeMaterialCard ->
            setCustomModelResourceLocation(creativeMaterialCard.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, creativeMaterialCard.resourceName))
        }
    }

    // カードごと
    CreativeMaterialCard.values().forEach { creativeMaterialCard ->

        // 翻訳生成
        lang("tile.${creativeMaterialCard.unlocalizedName}.name", creativeMaterialCard.englishName, creativeMaterialCard.japaneseName)

        // ブロックモデル生成
        makeBlockModel(creativeMaterialCard.resourceName) {
            DataModel(
                parent = "block/cube_all",
                textures = mapOf(
                    "all" to "miragefairy2019:blocks/${creativeMaterialCard.resourceName}"
                )
            )
        }

        // アイテムモデル生成
        makeItemModel(creativeMaterialCard.resourceName) { block }

    }

}

enum class CreativeMaterialCard(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val englishName: String,
    val japaneseName: String
) : IBlockVariant {
    CREATIVE_AURA_STONE(0, "creative_aura_stone", "creativeAuraStone", "Creative Aura Stone", "アカーシャの霊気石"),
}

class BlockCreativeMaterials : BlockMulti<CreativeMaterialCard>(Material.ROCK, BlockVariantList(CreativeMaterialCard.values().toList(), 0)) {
    init {
        setBlockUnbreakable()
        setResistance(6000000.0F)
        soundType = SoundType.STONE
        disableStats()
    }
}

class ItemBlockCreativeMaterials(block: BlockCreativeMaterials) : ItemBlockMulti<BlockCreativeMaterials, CreativeMaterialCard>(block)
