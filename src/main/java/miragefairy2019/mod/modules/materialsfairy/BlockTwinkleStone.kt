package miragefairy2019.mod.modules.materialsfairy

import miragefairy2019.mod.lib.multi.BlockMulti
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLiving.SpawnPlacementType
import net.minecraft.entity.player.EntityPlayer
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
        variantList.forEach { setHarvestLevel("pickaxe", 0, getState(it)) }
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = true

    override fun getLightValue(state: IBlockState, world: IBlockAccess, pos: BlockPos) = getVariant(state)!!.lightValue
    override fun canCreatureSpawn(state: IBlockState, world: IBlockAccess, pos: BlockPos, type: SpawnPlacementType) = false
}
