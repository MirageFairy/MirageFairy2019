package miragefairy2019.libkt

import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos

data class DimensionalPos(val dimension: Int, val x: Int, val y: Int, val z: Int) {
    companion object {
        fun parse(expression: String) = expression.split(",").let { DimensionalPos(it[0].toInt(), it[1].toInt(), it[2].toInt(), it[3].toInt()) }
    }

    constructor(dimension: Int, blockPos: BlockPos) : this(dimension, blockPos.x, blockPos.y, blockPos.z)

    val pos get() = BlockPos(x, y, z)
    val expression get() = "$dimension,$x,$y,$z"
    override fun toString() = expression
}

val TileEntity.dimensionalPos get() = DimensionalPos(world.provider.dimension, pos)
