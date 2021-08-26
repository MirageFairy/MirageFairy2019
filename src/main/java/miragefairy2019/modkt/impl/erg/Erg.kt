package miragefairy2019.modkt.impl.fairy

import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.erg.IErgEntry
import miragefairy2019.modkt.api.erg.IErgSet
import miragefairy2019.modkt.api.erg.IErgType
import net.minecraft.util.text.TextFormatting

class ErgSet(private val iterable: Iterable<IErgEntry>) : IErgSet {
    private val list = iterable.toList()
    private val map = list.associateBy { it.type }
    override fun getEntries() = list
    override fun getPower(type: IErgType) = map[type]?.power ?: 0.0
}

class ErgEntry(private val type: IErgType, private val power: Double) : IErgEntry {
    override fun getPower() = power
    override fun getType() = type
}

class ErgType(private val name: String, private val textColor: TextFormatting) : IErgType {
    companion object {
        val attack = ErgType("attack", TextFormatting.DARK_RED)
        val craft = ErgType("craft", TextFormatting.GOLD)
        val fell = ErgType("fell", TextFormatting.DARK_GREEN)
        val light = ErgType("light", TextFormatting.YELLOW)
        val flame = ErgType("flame", TextFormatting.RED)
        val water = ErgType("water", TextFormatting.BLUE)
        val crystal = ErgType("crystal", TextFormatting.AQUA)
        val art = ErgType("art", TextFormatting.GREEN)
        val store = ErgType("store", TextFormatting.GOLD)
        val warp = ErgType("warp", TextFormatting.DARK_PURPLE)
        val shoot = ErgType("shoot", TextFormatting.GREEN)
        val breaking = ErgType("breaking", TextFormatting.DARK_RED)
        val chemical = ErgType("chemical", TextFormatting.DARK_AQUA)
        val slash = ErgType("slash", TextFormatting.DARK_RED)
        val food = ErgType("food", TextFormatting.YELLOW)
        val knowledge = ErgType("knowledge", TextFormatting.DARK_GREEN)
        val energy = ErgType("energy", TextFormatting.GOLD)
        val submission = ErgType("submission", TextFormatting.DARK_GRAY)
        val christmas = ErgType("christmas", TextFormatting.DARK_GREEN)
        val freeze = ErgType("freeze", TextFormatting.AQUA)
        val thunder = ErgType("thunder", TextFormatting.YELLOW)
        fun values(): Array<IErgType> = arrayOf(
                attack,
                craft,
                fell,
                light,
                flame,
                water,
                crystal,
                art,
                store,
                warp,
                shoot,
                breaking,
                chemical,
                slash,
                food,
                knowledge,
                energy,
                submission,
                christmas,
                freeze,
                thunder
        )
    }

    override fun getName() = name
    override fun getTextColor() = textColor
}


val IErgType.displayName get() = text { translate("mirageFairy2019.erg.$name.name").color(textColor) }
