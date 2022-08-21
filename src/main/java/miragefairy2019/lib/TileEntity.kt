package miragefairy2019.lib

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

fun <T> TileEntity.getCapabilityIfHas(capability: Capability<T>, facing: EnumFacing?) = if (this.hasCapability(capability, facing)) this.getCapability(capability, facing) else null

operator fun IItemHandler.get(index: Int) = this.getStackInSlot(index)
operator fun IItemHandlerModifiable.set(index: Int, itemStack: ItemStack) = this.setStackInSlot(index, itemStack)
val IItemHandler.size get() = this.slots
val IItemHandler.indices get() = 0 until this.size
val IItemHandler.itemStacks get() = (0 until this.size).map { this[it] }


/**
 * BlockStateの違いを無視するTileEntityです。
 *
 * 厳密には、Blockが同一なままBlockStateが変化した際に、内部状態を破棄しないTileEntityです。
 *
 * ブロックの回転時にインベントリの中身が消失しません。
 */
abstract class TileEntityIgnoreBlockState : TileEntity() {
    override fun shouldRefresh(world: World, blockPos: BlockPos, oldBlockState: IBlockState, newBlockSate: IBlockState) = oldBlockState.block !== newBlockSate.block
}
