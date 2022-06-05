package miragefairy2019.lib

import net.minecraft.util.EnumFacing

operator fun EnumFacing.div(other: EnumFacing): EnumFacing = when (this) {
    EnumFacing.UP, EnumFacing.DOWN -> this
    else -> EnumFacing.getHorizontal((this.horizontalIndex - other.horizontalIndex + 4) % 4)
}
