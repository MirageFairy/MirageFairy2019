package miragefairy2019.lib

@Suppress("unused")
enum class EnumFlammability(val value: Int) {

    /** 次のようなブロックに設定されています：原木、石炭ブロック */
    VERY_SLOW(5),

    SLOW(15),

    /** 次のようなブロックに設定されています：木材、本棚、麦俵、絨毯 */
    SLIGHTLY_SLOW(20),

    MEDIUM(30),

    SLIGHTLY_FAST(45),

    /** 次のようなブロックに設定されています：葉ブロック、羊毛 */
    FAST(60),

    /** 次のようなブロックに設定されています：TNT、草花 */
    VERY_FAST(100),

}

@Suppress("unused")
enum class EnumFireSpreadSpeed(val value: Int) {

    /** 次のようなブロックに設定されています：木材、原木、石炭ブロック */
    VERY_SLOW(5),

    /** 次のようなブロックに設定されています：TNT、ツタ */
    SLOW(15),

    SLIGHTLY_SLOW(20),

    /** 次のようなブロックに設定されています：葉ブロック、本棚、羊毛 */
    MEDIUM(30),

    SLIGHTLY_FAST(45),

    /** 次のようなブロックに設定されています：草花、麦俵、絨毯 */
    FAST(60),

    VERY_FAST(100),

}
