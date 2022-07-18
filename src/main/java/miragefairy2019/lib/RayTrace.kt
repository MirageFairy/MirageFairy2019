package miragefairy2019.lib

import miragefairy2019.libkt.axisAlignedBBOf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World


sealed class RayTraceWrapper(val targetPosition: Vec3d)

sealed class HitRayTraceWrapper(val rayTraceResult: RayTraceResult) : RayTraceWrapper(rayTraceResult.hitVec)

class BlockRayTraceWrapper(rayTraceResult: RayTraceResult) : HitRayTraceWrapper(rayTraceResult) {
    init {
        require(rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
    }
}

class EntityRayTraceWrapper(rayTraceResult: RayTraceResult) : HitRayTraceWrapper(rayTraceResult) {
    init {
        require(rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY)
    }
}

class MissRayTraceWrapper(targetPosition: Vec3d) : RayTraceWrapper(targetPosition)

fun RayTraceResult.toRayTraceWrapper() = when (this.typeOfHit) {
    RayTraceResult.Type.BLOCK -> BlockRayTraceWrapper(this)
    RayTraceResult.Type.ENTITY -> EntityRayTraceWrapper(this)
    else -> error("Invalid type: ${this.typeOfHit}")
}


fun <E : Entity> rayTrace(
    world: World,
    player: EntityPlayer,
    useLiquids: Boolean,
    additionalReach: Double,
    classEntity: Class<E>,
    filterEntity: (E) -> Boolean
): RayTraceResult? {
    val reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue + additionalReach
    val eyePosition = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ)
    val targetPosition = getTargetPosition(eyePosition, player.rotationPitch, player.rotationYaw, reachDistance)

    val entries = mutableListOf<Pair<RayTraceResult, Double>>()

    // ブロックのレイトレース
    val blockRayTraceResult = world.rayTraceBlocks(eyePosition, targetPosition, useLiquids, !useLiquids, false)
    if (blockRayTraceResult != null) entries += Pair(blockRayTraceResult, eyePosition.squareDistanceTo(blockRayTraceResult.hitVec))

    // エンティティのレイトレース
    val boundingBox = axisAlignedBBOf(eyePosition, targetPosition)
    val entities: List<E> = world.getEntitiesWithinAABB(classEntity, boundingBox) { it != null && filterEntity(it) }
    entities.forEach { entity ->
        if (entity == player) return@forEach
        val rayTraceResult = entity.entityBoundingBox.calculateIntercept(eyePosition, targetPosition) ?: return@forEach
        entries += Pair(RayTraceResult(entity, rayTraceResult.hitVec), eyePosition.squareDistanceTo(rayTraceResult.hitVec))
    }

    return entries.minBy { it.second }?.first
}

fun rayTraceIgnoreEntity(
    world: World,
    player: EntityPlayer,
    useLiquids: Boolean,
    additionalReach: Double
): RayTraceResult? {
    val reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue + additionalReach
    val eyePosition = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ)
    val targetPosition = getTargetPosition(eyePosition, player.rotationPitch, player.rotationYaw, reachDistance)

    // ブロックのレイトレース
    return world.rayTraceBlocks(eyePosition, targetPosition, useLiquids, !useLiquids, false)
}

/** ブロックやエンティティがあるかに関わらず、視線の先の座標を返します。 */
fun getSight(
    player: EntityPlayer,
    additionalReach: Double
): Vec3d {
    val reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue + additionalReach
    val eyePosition = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ)
    return getTargetPosition(eyePosition, player.rotationPitch, player.rotationYaw, reachDistance)
}

fun getTargetPosition(eyePosition: Vec3d, rotationPitch: Float, rotationYaw: Float, distance: Double): Vec3d {
    val k = Math.PI.toFloat() / 180.0f
    val x1 = MathHelper.sin(-rotationYaw * k - Math.PI.toFloat())
    val z1 = MathHelper.cos(-rotationYaw * k - Math.PI.toFloat())
    val xz1 = -MathHelper.cos(-rotationPitch * k)
    return eyePosition.addVector(
        x1 * xz1 * distance,
        MathHelper.sin(-rotationPitch * k) * distance,
        z1 * xz1 * distance
    )
}