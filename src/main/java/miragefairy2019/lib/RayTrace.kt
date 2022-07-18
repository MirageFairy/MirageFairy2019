package miragefairy2019.lib

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

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

    // ブロックのレイトレース
    val blockRayTraceResult = rayTraceIgnoreEntity(world, player, useLiquids, additionalReach)
    val blockSquareDistance: Double = if (blockRayTraceResult != null) eyePosition.squareDistanceTo(blockRayTraceResult.hitVec) else 0.0

    // エンティティのレイトレース
    val entityRayTraceResult: RayTraceResult?
    val entitySquareDistance: Double
    run {
        val entities: List<E> = world.getEntitiesWithinAABB(
            classEntity,
            AxisAlignedBB(
                eyePosition.x,
                eyePosition.y,
                eyePosition.z,
                targetPosition.x,
                targetPosition.y,
                targetPosition.z
            )
        ) { it != null && filterEntity(it) }

        val pair: Pair<RayTraceResult, Double>? = entities
            .mapNotNull { entity ->
                if (entity == player) return@mapNotNull null
                val aabb: AxisAlignedBB = entity.entityBoundingBox
                val rayTraceResult = aabb.calculateIntercept(eyePosition, targetPosition) ?: return@mapNotNull null
                Pair(RayTraceResult(entity, rayTraceResult.hitVec), eyePosition.squareDistanceTo(rayTraceResult.hitVec))
            }
            .minBy { it.second }
        entityRayTraceResult = pair?.first
        entitySquareDistance = pair?.second ?: 0.0
    }

    return when {
        blockRayTraceResult != null && entityRayTraceResult != null -> when {
            blockSquareDistance < entitySquareDistance -> blockRayTraceResult
            else -> entityRayTraceResult
        }
        blockRayTraceResult != null -> blockRayTraceResult
        entityRayTraceResult != null -> entityRayTraceResult
        else -> null
    }
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
    val blockRayTraceResult = world.rayTraceBlocks(eyePosition, targetPosition, useLiquids, !useLiquids, false)

    return blockRayTraceResult
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