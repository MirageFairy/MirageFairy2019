package miragefairy2019.lib

import net.minecraft.entity.Entity
import net.minecraft.util.math.AxisAlignedBB

infix fun AxisAlignedBB.collides(other: AxisAlignedBB) = this.intersects(other)
infix fun Entity.collides(other: Entity) = this.entityBoundingBox collides other.entityBoundingBox
