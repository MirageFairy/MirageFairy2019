package miragefairy2019.mod.beanstalk

enum class Facing(val x: Int, val y: Int, private val oppositeGetter: () -> Facing) {
    DOWN(0, 0, { UP }),
    UP(180, 0, { DOWN }),
    NORTH(90, 180, { SOUTH }),
    SOUTH(90, 0, { NORTH }),
    WEST(90, 90, { EAST }),
    EAST(90, 270, { WEST }),
    ;

    val opposite get() = oppositeGetter()
    override fun toString() = name.toLowerCase()
}
