package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.BlockMulti
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.IBlockVariant
import miragefairy2019.libkt.ItemBlockMulti
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.hydrogen.toUpperCaseHead
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLiving.SpawnPlacementType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

enum class TwinkleStoneCard(
    val metadata: Int,
    val colorRegistryName: String,
    val colorUnlocalizedName: String,
    val colorOreName: String,
    val colorEnglishName: String,
    val colorJapaneseName: String,
    val lightValue: Int
) : IStringSerializable {
    WHITE(0, "white", "white", "white", "White", "白色", 15),
    ORANGE(1, "orange", "orange", "orange", "Orange", "橙色", 15),
    MAGENTA(2, "magenta", "magenta", "magenta", "Magenta", "赤紫色", 11),
    LIGHT_BLUE(3, "light_blue", "lightBlue", "lightBlue", "Light Blue", "空色", 15),
    YELLOW(4, "yellow", "yellow", "yellow", "Yellow", "黄色", 15),
    LIME(5, "lime", "lime", "lime", "Lime", "黄緑色", 15),
    PINK(6, "pink", "pink", "pink", "Pink", "桃色", 15),
    GRAY(7, "gray", "gray", "gray", "Gray", "灰色", 11),
    LIGHT_GRAY(8, "light_gray", "lightGray", "lightGray", "Light Gray", "薄灰色", 15),
    CYAN(9, "cyan", "cyan", "cyan", "Cyan", "青緑色", 11),
    PURPLE(10, "purple", "purple", "purple", "Purple", "紫色", 15),
    BLUE(11, "blue", "blue", "blue", "Blue", "青色", 11),
    BROWN(12, "brown", "brown", "brown", "Brown", "茶色", 11),
    GREEN(13, "green", "green", "green", "Green", "緑色", 11),
    RED(14, "red", "Red", "red", "Red", "赤色", 11),
    BLACK(15, "black", "black", "black", "Black", "黒色", 3),
    ;

    override fun toString(): String = colorRegistryName
    override fun getName(): String = colorRegistryName
}

val TwinkleStoneCard.registryName get() = "${colorRegistryName}_twinkle_stone"
val TwinkleStoneCard.unlocalizedName get() = "twinkleStone${colorUnlocalizedName.toUpperCaseHead()}"
val TwinkleStoneCard.oreNames get() = listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStone${colorOreName.toUpperCaseHead()}")


lateinit var blockTwinkleStone: () -> BlockTwinkleStone
lateinit var itemBlockTwinkleStone: () -> ItemBlockMulti<BlockTwinkleStone, BlockVariantTwinkleStone>

val twinkleStoneModule = module {

    // 全体
    run {

        // ブロック
        blockTwinkleStone = block({ BlockTwinkleStone() }, "twinkle_stone") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataModelBlockDefinition(
                    variants = TwinkleStoneCard.values().map { "variant=${it.metadata}" to DataSingleVariantList(DataVariant("miragefairy2019:${it.registryName}")) }.toMap()
                )
            }
        }

        // アイテム
        itemBlockTwinkleStone = item({ ItemBlockMulti(blockTwinkleStone()) }, "twinkle_stone") {
            setUnlocalizedName("twinkleStone")
            TwinkleStoneCard.values().forEach { twinkleStoneCard ->
                setCustomModelResourceLocation(twinkleStoneCard.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, twinkleStoneCard.registryName))
                twinkleStoneCard.oreNames.forEach { oreName ->
                    addOreName(oreName, twinkleStoneCard.metadata)
                }
            }
        }

        // 水色のトゥインクルストーンのレシピ生成
        makeRecipe("twinkle_stone") {
            DataShapedRecipe(
                pattern = listOf(
                    "ScS",
                    "PlP",
                    "SmS"
                ),
                key = mapOf(
                    "S" to DataSimpleIngredient(item = "minecraft:stone", data = 0),
                    "P" to DataOreIngredient(ore = "plateMiragium"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData,
                    "l" to DataOreIngredient(ore = "mirageFairy2019SphereLight")
                ),
                result = DataResult(item = "miragefairy2019:twinkle_stone", data = 3, count = 4)
            )
        }

    }

    // それぞれ
    TwinkleStoneCard.values().forEach {

        // ブロックモデル生成
        makeBlockModel(it.registryName) {
            DataModel(
                parent = "block/cube_all",
                textures = mapOf(
                    "all" to "miragefairy2019:blocks/${it.registryName}"
                )
            )
        }

        // アイテムモデル生成
        makeItemModel(it.registryName) { block }

        // 翻訳生成
        onMakeLang {
            enJa(
                "tile.${it.unlocalizedName}.name",
                "${it.colorEnglishName} Twinkle Stone",
                "${it.colorJapaneseName}のトゥインクルストーン"
            )
        }

        // レシピ生成
        makeRecipe(it.registryName) {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairy2019TwinkleStone"),
                    DataOreIngredient(ore = "dye${it.colorUnlocalizedName.toUpperCaseHead()}")
                ),
                result = DataResult(item = "miragefairy2019:twinkle_stone", data = it.metadata)
            )
        }

    }

}

class BlockVariantTwinkleStone(val card: TwinkleStoneCard) : IBlockVariant {
    override val metadata: Int get() = card.metadata
    override val resourceName: String get() = card.registryName
    override val unlocalizedName: String get() = card.unlocalizedName
}

class BlockTwinkleStone : BlockMulti<BlockVariantTwinkleStone>(Material.ROCK, BlockVariantList(TwinkleStoneCard.values().map { BlockVariantTwinkleStone(it) })) {
    init {
        // style
        soundType = SoundType.STONE
        // 挙動
        setHardness(3.0f)
        setResistance(5.0f)
        variantList.blockVariants.forEach { setHarvestLevel("pickaxe", 0, getState(it)) }
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = true

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos) = getVariant(state).card.lightValue
    override fun canCreatureSpawn(state: IBlockState, world: IBlockAccess, pos: BlockPos, type: SpawnPlacementType) = false
}
