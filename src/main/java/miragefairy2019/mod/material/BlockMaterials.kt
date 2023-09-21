package miragefairy2019.mod.material

import miragefairy2019.libkt.BlockMulti
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.IBlockVariant
import miragefairy2019.libkt.ItemBlockMulti
import miragefairy2019.mod.IFuelItem
import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random

interface IBlockVariantMaterials : IBlockVariant {
    val blockHardness: Float
    val harvestTool: String
    val harvestLevel: Int
    val burnTime: Int
    val soundType: SoundType
    val isFallable: Boolean
    val material: Material
    val isBeaconBase: Boolean
    val flammability: Int
    val fireSpreadSpeed: Int
    val enchantPowerBonus: Float
}

class BlockMaterials<V : IBlockVariantMaterials>(variantList: BlockVariantList<V>) : BlockMulti<V>(Material.IRON, variantList) {
    init {
        variantList.blockVariants.forEach { setHarvestLevel(it.harvestTool, it.harvestLevel, getState(it)) }
    }

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = getVariant(world.getBlockState(pos)).flammability
    override fun getFireSpreadSpeed(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = getVariant(world.getBlockState(pos)).fireSpreadSpeed
    override fun getEnchantPowerBonus(world: World, pos: BlockPos) = getVariant(world.getBlockState(pos)).enchantPowerBonus


    // 一般
    override fun getMaterial(blockState: IBlockState) = getVariant(blockState).material
    override fun getBlockHardness(blockState: IBlockState, world: World, blockPos: BlockPos) = getVariant(blockState).blockHardness
    override fun getExplosionResistance(world: World, blockPos: BlockPos, exploder: Entity?, explosion: Explosion) = getVariant(world.getBlockState(blockPos)).blockHardness * 5
    override fun getSoundType(blockState: IBlockState, world: World, blockPos: BlockPos, entity: Entity?) = getVariant(blockState).soundType


    // ドロップ系
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = true


    // 落下判定

    override fun onBlockAdded(world: World, blockPos: BlockPos, blockState: IBlockState) {
        if (!getVariant(blockState).isFallable) return
        world.scheduleUpdate(blockPos, this, tickRate(world))
    }

    override fun neighborChanged(blockState: IBlockState, world: World, blockPos: BlockPos, block: Block, fromBlockPos: BlockPos) {
        if (!getVariant(blockState).isFallable) return
        world.scheduleUpdate(blockPos, this, tickRate(world))
    }

    override fun updateTick(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        if (world.isRemote) return
        if (!getVariant(blockState).isFallable) return
        checkFallable(world, blockPos)
    }

    private fun checkFallable(world: World, blockPos: BlockPos) {
        fun canFallThrough(blockPos: BlockPos) = world.isAirBlock(blockPos) || BlockFalling.canFallThrough(world.getBlockState(blockPos))

        val downBlockPos = blockPos.down()
        if (!(canFallThrough(downBlockPos) && blockPos.y >= 0)) return // 現在奈落よりも上にあり、かつ床が無い　でないなら落下しない

        if (!BlockFalling.fallInstantly && world.isAreaLoaded(blockPos.add(-32, -32, -32), blockPos.add(32, 32, 32))) {
            // 即時落下状態でなく、周辺が読み込まれているならば、落下ブロックエンティティになる

            if (!world.isRemote) {
                world.spawnEntity(
                    EntityFallingBlock(
                        world,
                        blockPos.x + 0.5,
                        blockPos.y.toDouble(),
                        blockPos.z + 0.5,
                        world.getBlockState(blockPos)
                    )
                )
            }
        } else {
            // 即時落下状態、もしくは周辺が部分的に読み込まれていないならば、即落下する

            val blockState = world.getBlockState(blockPos)
            world.setBlockToAir(blockPos)

            var groundBlockPos = downBlockPos
            while (canFallThrough(groundBlockPos) && groundBlockPos.y > 0) { // 地面かY=0になるまでループ
                groundBlockPos = groundBlockPos.down()
            }

            if (groundBlockPos.y > 0) { // Y=0の位置にある床には着地できない
                world.setBlockState(groundBlockPos.up(), blockState)
            }
        }
    }

    override fun tickRate(world: World) = 2

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(blockState: IBlockState, world: World, blockPos: BlockPos, random: Random) {
        if (!getVariant(blockState).isFallable) return
        if (random.nextInt(16) == 0) {
            val downBlockPos = blockPos.down()
            if (BlockFalling.canFallThrough(world.getBlockState(downBlockPos))) {
                val x = (downBlockPos.x + random.nextFloat()).toDouble()
                val y = downBlockPos.y - 0.05
                val z = (downBlockPos.z + random.nextFloat()).toDouble()
                world.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0, 0.0, 0.0, getStateId(blockState))
            }
        }
    }


    // ビーコン基礎ブロック判定
    override fun isBeaconBase(world: IBlockAccess, blockPos: BlockPos, beaconBlockPos: BlockPos) = getVariant(world.getBlockState(blockPos)).isBeaconBase
}

class ItemBlockMaterials<V : IBlockVariantMaterials>(block: BlockMaterials<V>) : ItemBlockMulti<BlockMaterials<V>, V>(block), IFuelItem {
    override fun getItemBurnTime(itemStack: ItemStack) = block2.getVariant(itemStack.metadata).burnTime
}
