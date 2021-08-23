package miragefairy2019.modkt.impl.fairy

import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.fairy.IErgType
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.*

class ErgType(private val name: String, private val textColor: TextFormatting) : IErgType {
    companion object {
        val attack = ErgType("attack", DARK_RED)
        val craft = ErgType("craft", GOLD)
        val fell = ErgType("fell", DARK_GREEN)
        val light = ErgType("light", YELLOW)
        val flame = ErgType("flame", RED)
        val water = ErgType("water", BLUE)
        val crystal = ErgType("crystal", AQUA)
        val art = ErgType("art", GREEN)
        val store = ErgType("store", GOLD)
        val warp = ErgType("warp", DARK_PURPLE)
        val shoot = ErgType("shoot", GREEN)
        val breaking = ErgType("breaking", DARK_RED)
        val chemical = ErgType("chemical", DARK_AQUA)
        val slash = ErgType("slash", DARK_RED)
        val food = ErgType("food", YELLOW)
        val knowledge = ErgType("knowledge", DARK_GREEN)
        val energy = ErgType("energy", GOLD)
        val submission = ErgType("submission", DARK_GRAY)
        val christmas = ErgType("christmas", DARK_GREEN)
        val freeze = ErgType("freeze", AQUA)
        val thunder = ErgType("thunder", YELLOW)
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
