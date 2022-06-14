package miragefairy2019.mod.fairyweapon

import miragefairy2019.libkt.randomInt
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.max
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer

fun <T> spawnParticleTargets(world: World, targets: List<T>, fPosition: (T) -> Vec3d, fColor: (T) -> Int) {
    val rate = 5 / (targets.size atLeast 5).toDouble()
    targets.forEach { target ->
        if (Math.random() < 0.2 * rate) {
            val position = fPosition(target)
            val color = fColor(target)
            spawnParticle(world, position, color)
        }
    }
}

fun spawnDamageParticle(world: WorldServer, entity: Entity, damage: Double) {
    val count = world.rand.randomInt(damage / 2.0)
    if (count > 0) {
        world.spawnParticle(
            EnumParticleTypes.DAMAGE_INDICATOR,
            entity.posX,
            entity.posY + entity.height * 0.5,
            entity.posZ, count,
            0.1, 0.0, 0.1,
            0.2
        )
    }
}

fun spawnMagicParticle(world: WorldServer, player: EntityPlayer, target: Entity) = spawnMagicParticle(world, player, target.positionVector.addVector(0.0, (target.height / 2).toDouble(), 0.0))
fun spawnMagicParticle(world: WorldServer, player: EntityPlayer, end: Vec3d) {
    val start = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ).add(player.lookVec.scale(2.0))
    val delta = end.subtract(start)

    val distance = start.distanceTo(end)

    repeat((distance * 4.0).toInt()) { i ->
        val pos = start.add(delta.scale(i / (distance * 4.0)))
        world.spawnParticle(
            EnumParticleTypes.ENCHANTMENT_TABLE,
            pos.x + (world.rand.nextDouble() - 0.5) * 0.2,
            pos.y + (world.rand.nextDouble() - 0.5) * 0.2,
            pos.z + (world.rand.nextDouble() - 0.5) * 0.2,
            0,
            0.0, 0.0, 0.0,
            0.0
        )
    }
}

fun spawnMagicSplashParticle(world: WorldServer, position: Vec3d) {
    repeat(5) { i ->
        world.spawnParticle(
            EnumParticleTypes.SPELL_INSTANT,
            position.x,
            position.y,
            position.z,
            10,
            (world.rand.nextDouble() - 0.5) * 2.0,
            (world.rand.nextDouble() - 0.5) * 2.0,
            (world.rand.nextDouble() - 0.5) * 2.0,
            0.0
        )
    }
}

private const val MAX_PARTICLE_COUNT = 1.0

fun spawnParticleTargets(world: World, tuples: List<Pair<Vec3d, EnumTargetExecutability>>) {

    // 1tickに平均MAX_PARTICLE_COUNT個までしかパーティクルを表示しない
    val rate = MAX_PARTICLE_COUNT / (tuples.size.toDouble() max MAX_PARTICLE_COUNT)

    // パーティクル生成
    tuples.forEach { tuple ->
        if (Math.random() < rate) spawnParticleTarget(world, tuple.first, tuple.second)
    }

}

fun spawnParticleTarget(world: World, position: Vec3d, targetExecutability: EnumTargetExecutability) = spawnParticle(world, position, targetExecutability.color)

fun spawnParticle(world: World, position: Vec3d, color: Int) = world.spawnParticle(
    EnumParticleTypes.SPELL_MOB,
    position.x,
    position.y,
    position.z,
    ((color shr 16) and 0xFF) / 255.0,
    ((color shr 8) and 0xFF) / 255.0,
    ((color shr 0) and 0xFF) / 255.0
)
