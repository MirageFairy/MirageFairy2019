package miragefairy2019.mod.artifacts

import miragefairy2019.libkt.BlockMulti
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.IBlockVariant
import miragefairy2019.libkt.ItemBlockMulti
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.resourcemaker.DataBlockState
import miragefairy2019.resourcemaker.DataBlockStates
import miragefairy2019.resourcemaker.makeBlockItemModel
import miragefairy2019.resourcemaker.makeBlockModel
import miragefairy2019.resourcemaker.makeBlockStates
import mirrg.kotlin.gson.jsonElement
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
    val module = module {
        blockTwinkleStone = block({ BlockTwinkleStone() }, "twinkle_stone") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
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
            fun makeBlockModel(name: String) = makeBlockModel(ResourceName(ModMirageFairy2019.MODID, name)) {
                jsonElement(
                    "parent" to "block/cube_all".jsonElement,
                    "textures" to jsonElement(
                        "all" to "miragefairy2019:blocks/$name".jsonElement
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
            makeBlockItemModel(ResourceName(ModMirageFairy2019.MODID, "${it}_twinkle_stone"))
        }
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
