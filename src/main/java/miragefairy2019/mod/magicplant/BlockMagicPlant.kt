package miragefairy2019.mod.magicplant

import net.minecraft.block.BlockBush
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class BlockMagicPlant : BlockBush(Material.PLANTS), IGrowable { // Solidであるマテリアルは耕土を破壊する
    abstract fun isMaxAge(state: IBlockState): Boolean
    abstract fun tryPick(world: World, blockPos: BlockPos, player: EntityPlayer?, fortune: Int): Boolean
}
