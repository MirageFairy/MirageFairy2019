package miragefairy2019.mod.modules.mirageflower

import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry
import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.shineEfficiency
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


class BlockMirageFlower : BlockMirageFlowerBase()
