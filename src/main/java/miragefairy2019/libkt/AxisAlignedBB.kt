package miragefairy2019.libkt

import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.Vec3d

fun axisAlignedBBOf(min: Vec3d, max: Vec3d) = AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z)
fun axisAlignedBBOf(point: Vec3d) = axisAlignedBBOf(point, point)
