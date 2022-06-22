package miragefairy2019.lib

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

/** ブロックやエンティティがあるかに関わらず、視線の先の座標を返します。 */
fun getSight(player: EntityPlayer, additionalReach: Double): Vec3d {

    // 最大距離
    val distance = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).attributeValue + additionalReach

    // 起点
    val rotationPitch = player.rotationPitch
    val rotationYaw = player.rotationYaw
    val x = player.posX
    val y = player.posY + player.getEyeHeight().toDouble()
    val z = player.posZ

    val k = Math.PI.toFloat() / 180.0F
    val f2 = MathHelper.cos(-rotationYaw * k - Math.PI.toFloat())
    val f3 = MathHelper.sin(-rotationYaw * k - Math.PI.toFloat())
    val f4 = -MathHelper.cos(-rotationPitch * k)

    // 視線ベクトル
    val x2 = f3 * f4
    val y2 = MathHelper.sin(-rotationPitch * k)
    val z2 = f2 * f4

    // 終点
    val vec2 = Vec3d(x, y, z).addVector(x2.toDouble() * distance, y2.toDouble() * distance, z2.toDouble() * distance)

    return vec2
}
