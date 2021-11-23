package miragefairy2019.mod.modules.mirageflower

import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.shineEfficiency
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun getGrowRateInFloor(fairyType: IFairyType) = fairyType.shineEfficiency * fairyType.erg(ErgTypes.crystal) / 100.0 * 3

fun getGrowRateTableMessage() = textComponent {
    listOf(
        !"===== Mirage Flower Grow Rate Table =====\n",
        ApiFairyRegistry.getFairyRegistry().fairies.toList()
            .map { Pair(it.fairyType, getGrowRateInFloor(it.fairyType)) }
            .filter { it.second > 1 }
            .sortedBy { it.second }
            .flatMap { format("%7.2f%%  ", it.second * 100) + !it.first!!.displayName + !"\n" },
        !"===================="
    ).flatten()
}

fun getGrowRateMessage(world: World, pos: BlockPos) = textComponent {
    !"===== Mirage Flower Grow Rate =====\n"
    !"Pos: ${pos.x} ${pos.y} ${pos.z}\n"
    !"Block: ${world.getBlockState(pos)}\n"
    !"Floor: ${world.getBlockState(pos.down())}\n"
    format("%.2f%%\n", BlockMirageFlowerBase.getGrowRate(world, pos) * 100)
    !"===================="
}


class BlockMirageFlower : BlockMirageFlowerBase(Material.PLANTS) {  // Solidでないマテリアルでなければ耕土の上に置けない
    init {
        // meta
        defaultState = blockState.baseState.withProperty(AGE, 0)
        // style
        soundType = SoundType.GLASS
    }

    companion object {
        val AGE = PropertyInteger.create("age", 0, 3)

        val AABB_STAGE0 = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
        val AABB_STAGE1 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0)
        val AABB_STAGE2 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
        val AABB_STAGE3 = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
    }
}
