package miragefairy2019.mod3.artifacts

import miragefairy2019.mod.lib.multi.BlockMulti
import miragefairy2019.mod.lib.multi.IBlockVariant
import miragefairy2019.mod.lib.multi.IListBlockVariant
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLiving.SpawnPlacementType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

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

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos) = getVariant(state)!!.lightValue
    override fun canCreatureSpawn(state: IBlockState, world: IBlockAccess, pos: BlockPos, type: SpawnPlacementType) = false
}

enum class EnumVariantTwinkleStone(
    @JvmField val metadata: Int,
    @JvmField val resourceName: String,
    @JvmField val unlocalizedName: String,
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
    override fun getMetadata(): Int = metadata
    override fun getResourceName(): String = resourceName
    override fun getUnlocalizedName(): String = unlocalizedName

    companion object {
        val variantList: IListBlockVariant<EnumVariantTwinkleStone> = object : IListBlockVariant<EnumVariantTwinkleStone> {
            private val values = values().toList()
            private val metaLookup = mutableMapOf<Int, EnumVariantTwinkleStone>().also { map ->
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
