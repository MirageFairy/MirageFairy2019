package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
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

object TwinkleStone {
    lateinit var blockTwinkleStone: () -> BlockTwinkleStone
    lateinit var itemBlockTwinkleStone: () -> ItemBlockMulti<BlockTwinkleStone, EnumVariantTwinkleStone>
    val twinkleStoneModule = module {
        blockTwinkleStone = block({ BlockTwinkleStone() }, "twinkle_stone") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataBlockStates(
                    variants = listOf(
                        "white", "orange", "magenta", "light_blue",
                        "yellow", "lime", "pink", "gray",
                        "silver", "cyan", "purple", "blue",
                        "brown", "green", "red", "black"
                    ).mapIndexed { i, it -> "variant=$i" to DataBlockState("miragefairy2019:${it}_twinkle_stone") }.toMap()
                )
            }
        }
        run {
            fun makeBlockModel(name: String) = makeBlockModel(name) {
                DataModel(
                    parent = "block/cube_all",
                    textures = mapOf(
                        "all" to "miragefairy2019:blocks/$name"
                    )
                )
            }
            listOf(
                "white", "orange", "magenta", "light_blue",
                "yellow", "lime", "pink", "gray",
                "silver", "cyan", "purple", "blue",
                "brown", "green", "red", "black"
            ).forEach {
                makeBlockModel("${it}_twinkle_stone")
            }
        }
        itemBlockTwinkleStone = item({ ItemBlockMulti(blockTwinkleStone()) }, "twinkle_stone") {
            setUnlocalizedName("twinkleStone")
            onRegisterItem {
                blockTwinkleStone().variantList.blockVariants.forEach { variant ->
                    item.setCustomModelResourceLocation(variant.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, variant.resourceName))
                }
            }
            onCreateItemStack {
                blockTwinkleStone().variantList.blockVariants.forEach { variant ->
                    variant.oreNames.forEach { oreName ->
                        item.addOreName(oreName, variant.metadata)
                    }
                }
            }
        }
        listOf(
            "white", "orange", "magenta", "light_blue",
            "yellow", "lime", "pink", "gray",
            "silver", "cyan", "purple", "blue",
            "brown", "green", "red", "black"
        ).forEach {
            makeItemModel("${it}_twinkle_stone") { block }
        }
        onMakeLang {
            enJa("tile.twinkleStoneWhite.name", "White Twinkle Stone", "白色のトゥインクルストーン")
            enJa("tile.twinkleStoneOrange.name", "Orange Twinkle Stone", "橙色のトゥインクルストーン")
            enJa("tile.twinkleStoneMagenta.name", "Magenta Twinkle Stone", "赤紫色のトゥインクルストーン")
            enJa("tile.twinkleStoneLightBlue.name", "Light Blue Twinkle Stone", "空色のトゥインクルストーン")
            enJa("tile.twinkleStoneYellow.name", "Yellow Twinkle Stone", "黄色のトゥインクルストーン")
            enJa("tile.twinkleStoneLime.name", "Lime Twinkle Stone", "黄緑色のトゥインクルストーン")
            enJa("tile.twinkleStonePink.name", "Pink Twinkle Stone", "桃色のトゥインクルストーン")
            enJa("tile.twinkleStoneGray.name", "Gray Twinkle Stone", "灰色のトゥインクルストーン")
            enJa("tile.twinkleStoneSilver.name", "Silver Twinkle Stone", "薄灰色のトゥインクルストーン")
            enJa("tile.twinkleStoneCyan.name", "Cyan Twinkle Stone", "青緑色のトゥインクルストーン")
            enJa("tile.twinkleStonePurple.name", "Purple Twinkle Stone", "紫色のトゥインクルストーン")
            enJa("tile.twinkleStoneBlue.name", "Blue Twinkle Stone", "青色のトゥインクルストーン")
            enJa("tile.twinkleStoneBrown.name", "Brown Twinkle Stone", "茶色のトゥインクルストーン")
            enJa("tile.twinkleStoneGreen.name", "Green Twinkle Stone", "緑色のトゥインクルストーン")
            enJa("tile.twinkleStoneRed.name", "Red Twinkle Stone", "赤色のトゥインクルストーン")
            enJa("tile.twinkleStoneBlack.name", "Black Twinkle Stone", "黒色のトゥインクルストーン")
        }
        makeRecipe("fairymaterials/twinkle_stone") {
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
        fun f(metadata: Int, colorRegistryNameSuffix: String, colorOreNameSuffix: String) = makeRecipe("fairymaterials/${colorRegistryNameSuffix}_twinkle_stone") {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "mirageFairy2019TwinkleStone"),
                    DataOreIngredient(ore = "dye$colorOreNameSuffix")
                ),
                result = DataResult(item = "miragefairy2019:twinkle_stone", data = metadata)
            )
        }
        f(0, "white", "White")
        f(1, "orange", "Orange")
        f(2, "magenta", "Magenta")
        f(3, "light_blue", "LightBlue")
        f(4, "yellow", "Yellow")
        f(5, "lime", "Lime")
        f(6, "pink", "Pink")
        f(7, "gray", "Gray")
        f(8, "silver", "LightGray")
        f(9, "cyan", "Cyan")
        f(10, "purple", "Purple")
        f(11, "blue", "Blue")
        f(12, "brown", "Brown")
        f(13, "green", "Green")
        f(14, "red", "Red")
        f(15, "black", "Black")
    }
}

class BlockTwinkleStone : BlockMulti<EnumVariantTwinkleStone>(Material.ROCK, EnumVariantTwinkleStone.variantList) {
    init {
        // style
        soundType = SoundType.STONE
        // 挙動
        setHardness(3.0f)
        setResistance(5.0f)
        variantList.blockVariants.forEach { setHarvestLevel("pickaxe", 0, getState(it)) }
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = true

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos) = getVariant(state).lightValue
    override fun canCreatureSpawn(state: IBlockState, world: IBlockAccess, pos: BlockPos, type: SpawnPlacementType) = false
}

enum class EnumVariantTwinkleStone(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val oreNames: List<String>,
    val lightValue: Int
) : IStringSerializable, IBlockVariant {
    WHITE(0, "white_twinkle_stone", "twinkleStoneWhite", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneWhite"), 15),
    ORANGE(1, "orange_twinkle_stone", "twinkleStoneOrange", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneOrange"), 15),
    MAGENTA(2, "magenta_twinkle_stone", "twinkleStoneMagenta", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneMagenta"), 11),
    LIGHT_BLUE(3, "light_blue_twinkle_stone", "twinkleStoneLightBlue", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneLightBlue"), 15),
    YELLOW(4, "yellow_twinkle_stone", "twinkleStoneYellow", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneYellow"), 15),
    LIME(5, "lime_twinkle_stone", "twinkleStoneLime", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneLime"), 15),
    PINK(6, "pink_twinkle_stone", "twinkleStonePink", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStonePink"), 15),
    GRAY(7, "gray_twinkle_stone", "twinkleStoneGray", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneGray"), 11),
    SILVER(8, "silver_twinkle_stone", "twinkleStoneSilver", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneSilver"), 15),
    CYAN(9, "cyan_twinkle_stone", "twinkleStoneCyan", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneCyan"), 11),
    PURPLE(10, "purple_twinkle_stone", "twinkleStonePurple", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStonePurple"), 15),
    BLUE(11, "blue_twinkle_stone", "twinkleStoneBlue", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneBlue"), 11),
    BROWN(12, "brown_twinkle_stone", "twinkleStoneBrown", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneBrown"), 11),
    GREEN(13, "green_twinkle_stone", "twinkleStoneGreen", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneGreen"), 11),
    RED(14, "red_twinkle_stone", "twinkleStoneRed", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneRed"), 11),
    BLACK(15, "black_twinkle_stone", "twinkleStoneBlack", listOf("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneBlack"), 3),
    ;

    override fun toString(): String = resourceName
    override fun getName(): String = resourceName

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}
