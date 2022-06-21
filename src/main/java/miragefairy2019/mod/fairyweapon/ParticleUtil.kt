package miragefairy2019.mod.fairyweapon

import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.sq
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.max
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

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

private var rotateY = 0.0

fun spawnParticleSphericalRange(world: World, positionCenter: Vec3d, radius: Double) {

    // 角度アニメーション更新
    rotateY += 7.4 / 180.0 * Math.PI
    if (rotateY > 2 * Math.PI) rotateY -= 2 * Math.PI

    // 円形パーティクル生成
    repeat(8) nextArm@{ i ->

        val yaw = rotateY + i * 0.25 * Math.PI // 横角度

        // 円形パーティクルの腕1部分の生成
        repeat(100) nextChance@{

            val pitch = (-0.5 + Math.random()) * Math.PI // 縦角度
            val offset = Vec3d(cos(pitch) * cos(yaw), sin(pitch), cos(pitch) * sin(yaw)).scale(radius)
            val positionParticle = positionCenter.add(offset) // パーティクル仮出現点

            if (world.getBlockState(BlockPos(positionParticle)).isFullBlock) return@nextChance // 仮出現点が地中なら失敗
            if (!world.getBlockState(BlockPos(positionParticle).down()).isFullBlock) return@nextChance // 仮出現点の真下が地面でなければ失敗

            // 修正パーティクル出現点
            // 高さを地面にくっつけるために、高さを地面の高さに固定した状態で横位置を調整する
            val y = floor(positionParticle.y) + 0.15 // 修正後の地面の高さ
            val offsetY = y - positionCenter.y // 修正後のZ差分
            val r1 = sqrt(offset.x.sq() + offset.z.sq()) // 元々のオフセットの横の距離
            if (r1.isNaN()) return@nextArm // 謎エラー対策
            val r2 = sqrt(radius.sq() - offsetY.sq()) // 修正後の横の距離
            if (r2.isNaN()) return@nextArm // 謎エラー対策
            val offsetX = offset.x / r1 * r2 // 修正後のX差分
            val offsetZ = offset.z / r1 * r2 // 修正後のZ差分

            // パーティクル生成
            world.spawnParticle(
                EnumParticleTypes.END_ROD,
                positionCenter.x + offsetX,
                positionCenter.y + offsetY,
                positionCenter.z + offsetZ,
                0.0,
                -0.08,
                0.0
            )

            return@nextArm
        }

    }

}
