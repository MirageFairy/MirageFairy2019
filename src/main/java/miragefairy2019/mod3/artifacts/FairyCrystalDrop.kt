package miragefairy2019.mod3.artifacts.fairycrystal

import miragefairy2019.mod3.fairy.RankedFairyTypeBundle
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraftforge.common.BiomeDictionary

object ApiFairyCrystal {
    val dropsFairyCrystal = mutableListOf<IRightClickDrop>()
}


enum class DropCategory { FIXED, COMMON, RARE }


interface IDrop {
    fun getItemStack(rank: Int): ItemStack
    val dropCategory: DropCategory
    val weight: Double
}

class DropFixed(
    val bundle: RankedFairyTypeBundle,
    override val dropCategory: DropCategory,
    override val weight: Double
) : IDrop {
    override fun getItemStack(rank: Int) = bundle[rank].createItemStack()
}


interface IRightClickDrop {
    val drop: IDrop
    fun testUseItem(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = false
    fun testWorld(world: World, pos: BlockPos) = false
    fun testBlock(block: Block) = false
    fun testBlockState(world: World, blockPos: BlockPos, blockState: IBlockState) = false
    fun testItem(item: Item) = false
    fun testItemStack(itemStack: ItemStack) = false
    fun testBiome(biome: Biome) = false
    fun testBiomeType(biomeType: BiomeDictionary.Type) = false
    fun testClassEntity(classEntity: Class<out Entity>) = false
    fun testEntity(entity: Entity) = false
}
