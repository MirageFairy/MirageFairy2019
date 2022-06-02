package miragefairy2019.mod.fairyweapon

import miragefairy2019.mod.fairyweapon.FairyWeaponUtils.spawnParticleSphericalRange
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.ceil
import kotlin.math.floor

open class MagicSelector(val world: World)

class MagicSelectorPosition(world: World, val position: Vec3d) : MagicSelector(world) {
    fun getMagicSelectorCircle(radius: Double) = MagicSelectorCircle(world, position, radius)
    fun getMagicSelectorSphere(radius: Double) = MagicSelectorSphere(world, position, radius)
    fun doEffect(color: Int) = spawnParticle(world, position, color)
}

class MagicSelectorCircle(world: World, val position: Vec3d, val radius: Double) : MagicSelector(world) {
    fun doEffect() = spawnParticleSphericalRange(world, position, radius)
    class BlockEntry(val blockPos: BlockPos, val distanceSquared: Double)

    val blockPosList
        get() = (floor(position.x - radius).toInt()..ceil(position.x + radius).toInt()).flatMap { x ->
            val y = position.y.toInt()
            (floor(position.z - radius).toInt()..ceil(position.z + radius).toInt()).mapNotNull { z ->
                val dx = (x + 0.5) - position.x
                val dz = (z + 0.5) - position.z
                val distanceSquared = dx * dx + dz * dz
                if (distanceSquared <= radius * radius) BlockEntry(BlockPos(x, y, z), distanceSquared) else null
            }
        }
}

class MagicSelectorSphere(world: World, val position: Vec3d, val radius: Double) : MagicSelector(world) {
    fun doEffect() = spawnParticleSphericalRange(world, position, radius)
}

/**
 * レイトレースを行い、何もヒットしなかった場合は空中の座標を得ます。
 * @param rayTraceResult
 * ブロックとエンティティのうち近い方にヒットしたリザルト。
 * 空中の場合、null。
 */
class MagicSelectorRayTrace private constructor(world: World, val rayTraceResult: RayTraceResult?, val position: Vec3d) : MagicSelector(world) {
    companion object {
        fun createIgnoreEntity(world: World, player: EntityPlayer, additionalReach: Double): MagicSelectorRayTrace {
            val rayTraceResult = MagicSelectorUtils.rayTrace(world, player, false, additionalReach)
            val position = rayTraceResult?.hitVec ?: getSight(player, additionalReach)
            return MagicSelectorRayTrace(world, rayTraceResult, position)
        }

        fun <E : Entity> createWith(world: World, player: EntityPlayer, additionalReach: Double, classEntity: Class<E>, filterEntity: (E) -> Boolean): MagicSelectorRayTrace {
            val rayTraceResult = FairyWeaponUtils.rayTrace<E>(world, player, false, additionalReach, classEntity) { filterEntity(it!!) }
            val position = rayTraceResult?.hitVec ?: getSight(player, additionalReach)
            return MagicSelectorRayTrace(world, rayTraceResult, position)
        }
    }

    val magicSelectorPosition get() = MagicSelectorPosition(world, position)
    val isHit get() = rayTraceResult != null
    val hitBlockPos get() = rayTraceResult?.blockPos
    val blockPos get() = hitBlockPos ?: BlockPos(position)
    val hitEntity get() = rayTraceResult?.entityHit
}
