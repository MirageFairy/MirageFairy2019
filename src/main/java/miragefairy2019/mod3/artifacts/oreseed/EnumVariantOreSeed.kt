package miragefairy2019.mod3.artifacts.oreseed

import net.minecraft.util.IStringSerializable

enum class EnumVariantOreSeed(val metadata: Int, val resourceName: String, val unlocalizedName: String, val shape: EnumOreSeedShape) : IStringSerializable {
    TINY(0, "tiny", "tiny", EnumOreSeedShape.TINY),
    LAPIS(1, "lapis", "lapis", EnumOreSeedShape.LAPIS),
    DIAMOND(2, "diamond", "diamond", EnumOreSeedShape.DIAMOND),
    IRON(3, "iron", "iron", EnumOreSeedShape.IRON),
    MEDIUM(4, "medium", "medium", EnumOreSeedShape.MEDIUM),
    LARGE(5, "large", "large", EnumOreSeedShape.LARGE),
    COAL(6, "coal", "coal", EnumOreSeedShape.COAL),
    HUGE(7, "huge", "huge", EnumOreSeedShape.HUGE),

    STRING(8, "string", "string", EnumOreSeedShape.STRING),
    HORIZONTAL(9, "horizontal", "horizontal", EnumOreSeedShape.HORIZONTAL),
    VERTICAL(10, "vertical", "vertical", EnumOreSeedShape.VERTICAL),
    POINT(11, "point", "point", EnumOreSeedShape.POINT),
    STAR(12, "star", "star", EnumOreSeedShape.STAR),
    RING(13, "ring", "ring", EnumOreSeedShape.RING),
    PYRAMID(14, "pyramid", "pyramid", EnumOreSeedShape.PYRAMID),
    CUBE(15, "cube", "cube", EnumOreSeedShape.CUBE),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName

    companion object {
        private val lookup = arrayOfNulls<EnumVariantOreSeed>(values().size).also { array -> values().forEach { array[it.metadata] = it } }
        fun byMetadata(metadata: Int) = lookup[if (metadata >= 0 && metadata < lookup.size) metadata else 0]!!
    }
}
