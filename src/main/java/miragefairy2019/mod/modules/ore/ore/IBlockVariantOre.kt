package miragefairy2019.mod.modules.ore.ore

import miragefairy2019.libkt.createItemStack
import miragefairy2019.mod.lib.IBlockVariant
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import java.util.Random

interface IBlockVariantOre : IBlockVariant {
    val harvestTool: String get() = "pickaxe"
    val harvestLevel: Int
    fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> = listOf(block.createItemStack(metadata = metadata))

    /**
     * 非シルクタッチでの破壊時に得られる経験値の量です。
     * 破壊時に鉱石ブロックがそのまま得られる場合、原則として0を返します。
     *
     * バニラでの設定は以下の通りです。
     *
     * ```
     * coal   : [0, 2)
     * diamond: [3, 7)
     * emerald: [3, 7)
     * lapis  : [2, 5)
     * quartz : [2, 5)
     * ```
     */
    fun getExpDrop(random: Random, fortune: Int): Int = 0

    val hardness: Float
    val resistance: Float
}
