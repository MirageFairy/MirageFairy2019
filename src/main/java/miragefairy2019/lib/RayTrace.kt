package miragefairy2019.lib

import miragefairy2019.libkt.axisAlignedBBOf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World


sealed class RayTraceWrapper {
    abstract val rayTraceResult: RayTraceResult?
    abstract val targetPosition: Vec3d
    abstract val isHit: Boolean
}

sealed class HitRayTraceWrapper(
    override val rayTraceResult: RayTraceResult
) : RayTraceWrapper() {
    override val targetPosition: Vec3d get() = rayTraceResult.hitVec
    override val isHit = true
}

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

class MissRayTraceWrapper(
    override val targetPosition: Vec3d
) : RayTraceWrapper() {
    override val rayTraceResult: Nothing? = null
    override val isHit = false
}

fun RayTraceResult.toRayTraceWrapper() = when (this.typeOfHit) {
    RayTraceResult.Type.BLOCK -> BlockRayTraceWrapper(this)
    RayTraceResult.Type.ENTITY -> EntityRayTraceWrapper(this)
    RayTraceResult.Type.MISS -> MissRayTraceWrapper(this.hitVec)
    else -> error("Invalid type: ${this.typeOfHit}")
}


fun <E : Entity> rayTrace(
    world: World,
    player: EntityPlayer,
    useLiquids: Boolean,
    additionalReach: Double,
    classEntity: Class<E>,
    filterEntity: (E) -> Boolean
): RayTraceWrapper {
    val reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue + additionalReach
    val eyePosition = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ)
    val targetPosition = getTargetPosition(eyePosition, player.rotationPitch, player.rotationYaw, reachDistance)

    val entries = mutableListOf<Pair<RayTraceWrapper, Double>>()

    // ブロックのレイトレース
    val blockRayTraceResult = world.rayTraceBlocks(eyePosition, targetPosition, useLiquids, !useLiquids, false)
    if (blockRayTraceResult != null) entries += Pair(BlockRayTraceWrapper(blockRayTraceResult), eyePosition.squareDistanceTo(blockRayTraceResult.hitVec))

    // エンティティのレイトレース
    val boundingBox = axisAlignedBBOf(eyePosition, targetPosition)
    val entities: List<E> = world.getEntitiesWithinAABB(classEntity, boundingBox) { it != null && filterEntity(it) }
    entities.forEach { entity ->
        if (entity == player) return@forEach
        val rayTraceResult = entity.entityBoundingBox.calculateIntercept(eyePosition, targetPosition) ?: return@forEach
        entries += Pair(EntityRayTraceWrapper(RayTraceResult(entity, rayTraceResult.hitVec)), eyePosition.squareDistanceTo(rayTraceResult.hitVec))
    }

    return entries.minBy { it.second }?.first ?: MissRayTraceWrapper(targetPosition)
}

fun rayTraceBlock(
    world: World,
    player: EntityPlayer,
    useLiquids: Boolean,
    additionalReach: Double
): RayTraceWrapper {
    val reachDistance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue + additionalReach
    val eyePosition = Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ)
    val targetPosition = getTargetPosition(eyePosition, player.rotationPitch, player.rotationYaw, reachDistance)

    // ブロックのレイトレース
    val blockRayTraceResult = world.rayTraceBlocks(eyePosition, targetPosition, useLiquids, !useLiquids, false) ?: return MissRayTraceWrapper(targetPosition)

    return BlockRayTraceWrapper(blockRayTraceResult)
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
