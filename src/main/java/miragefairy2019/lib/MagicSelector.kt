package miragefairy2019.lib

import miragefairy2019.libkt.axisAlignedBBOf
import miragefairy2019.libkt.sq
import miragefairy2019.mod.fairyweapon.spawnParticle
import miragefairy2019.mod.fairyweapon.spawnParticleSphericalRange
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.ceil
import kotlin.math.floor

// TODO fairyweaponへの依存性除去


class MagicSelector<out T>(val item: T) {
    companion object
}


class WorldPosition(val world: World, val position: Vec3d)

fun MagicSelector.Companion.position(world: World, position: Vec3d) = MagicSelector(WorldPosition(world, position))
fun MagicSelector<WorldPosition>.circle(radius: Double) = MagicSelector.circle(item.world, item.position, radius)
fun MagicSelector<WorldPosition>.sphere(radius: Double) = MagicSelector.sphere(item.world, item.position, radius)
fun WorldPosition.doEffect(color: Int) = spawnParticle(world, position, color)


class WorldCircle(val world: World, val position: Vec3d, val radius: Double)

fun MagicSelector.Companion.circle(world: World, position: Vec3d, radius: Double) = MagicSelector(WorldCircle(world, position, radius))
fun MagicSelector<WorldCircle>.blocks() = MagicSelector(
    (floor(item.position.x - item.radius).toInt()..ceil(item.position.x + item.radius).toInt()).flatMap { x ->
        item.position.y.toInt().let { y ->
            (floor(item.position.z - item.radius).toInt()..ceil(item.position.z + item.radius).toInt()).mapNotNull { z ->
                val dx = (x + 0.5) - item.position.x
                val dz = (z + 0.5) - item.position.z
                val distanceSquared = dx.sq() + dz.sq()
                if (distanceSquared <= item.radius.sq()) Pair(BlockPos(x, y, z), distanceSquared) else null
            }
        }
    }
)

fun WorldCircle.doEffect() = spawnParticleSphericalRange(world, position, radius)


class WorldSphere(val world: World, val position: Vec3d, val radius: Double)

fun MagicSelector.Companion.sphere(world: World, position: Vec3d, radius: Double) = MagicSelector(WorldSphere(world, position, radius))

fun WorldSphere.doEffect() = spawnParticleSphericalRange(world, position, radius)


class WorldRayTrace(val world: World, val rayTraceWrapper: RayTraceWrapper)

fun MagicSelector.Companion.rayTraceBlock(world: World, player: EntityPlayer, additionalReach: Double) = MagicSelector(WorldRayTrace(world, rayTraceBlock(world, player, false, additionalReach)))

fun <E : Entity> MagicSelector.Companion.rayTrace(world: World, player: EntityPlayer, additionalReach: Double, classEntity: Class<E>, filterEntity: (E) -> Boolean) = MagicSelector(WorldRayTrace(world, rayTrace(world, player, false, additionalReach, classEntity, filterEntity)))

val MagicSelector<WorldRayTrace>.position get() = MagicSelector.position(item.world, item.rayTraceWrapper.targetPosition)


class WorldEntities<out E : Entity>(val world: World, val entities: List<E>)

/** ある点を中心とした球形の範囲のエンティティを選択します。 */
fun <E : Entity> MagicSelector<WorldSphere>.entities(classEntity: Class<E>, predicate: (E) -> Boolean, maxTargetCount: Int) = MagicSelector(WorldEntities(item.world, run {
    fun getAllEntities(): List<E> = item.world.getEntitiesWithinAABB(classEntity, axisAlignedBBOf(item.position).grow(item.radius)) { e ->
        // 区間との距離
        fun d(value: Double, min: Double, max: Double) = when {
            value < min -> min - value
            value < max -> 0.0
            else -> value - max
        }

        e!!
        val dx = d(item.position.x, e.posX - e.width / 2.0, e.posX + e.width / 2.0)
        val dy = d(item.position.y, e.posY, e.posY + e.width)
        val dz = d(item.position.z, e.posZ - e.width / 2.0, e.posZ + e.width / 2.0)
        dx.sq() + dy.sq() + dz.sq() <= item.radius.sq()
    }
    getAllEntities().filter(predicate).sortedBy { it.getDistanceSq(item.position.x, item.position.y, item.position.z) }.take(maxTargetCount)
}))

fun <E : Entity> WorldEntities<E>.doEffect() = spawnParticleTargets(world, entities.map { it.positionVector })
