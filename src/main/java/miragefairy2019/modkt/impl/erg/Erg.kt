package miragefairy2019.modkt.impl.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.modkt.api.erg.ErgTypes
import miragefairy2019.modkt.api.erg.IErgEntry
import miragefairy2019.modkt.api.erg.IErgSet
import miragefairy2019.modkt.api.erg.IErgType
import net.minecraft.util.text.TextFormatting

val moduleErg: Module = {
    onInstantiation {
        val values = mutableListOf<IErgType>()
        operator fun String.invoke(textColor: TextFormatting): ErgType {
            val ergType = ErgType(this, textColor)
            values += ergType
            return ergType
        }

        ErgTypes.attack = "attack"(TextFormatting.DARK_RED)
        ErgTypes.craft = "craft"(TextFormatting.GREEN)
        ErgTypes.harvest = "harvest"(TextFormatting.DARK_GREEN)
        ErgTypes.light = "light"(TextFormatting.YELLOW)
        ErgTypes.flame = "flame"(TextFormatting.RED)
        ErgTypes.water = "water"(TextFormatting.BLUE)
        ErgTypes.crystal = "crystal"(TextFormatting.AQUA)
        ErgTypes.sound = "sound"(TextFormatting.GRAY)
        ErgTypes.space = "space"(TextFormatting.DARK_PURPLE)
        ErgTypes.warp = "warp"(TextFormatting.DARK_PURPLE)
        ErgTypes.shoot = "shoot"(TextFormatting.GREEN)
        ErgTypes.destroy = "destroy"(TextFormatting.DARK_RED)
        ErgTypes.chemical = "chemical"(TextFormatting.DARK_AQUA)
        ErgTypes.slash = "slash"(TextFormatting.DARK_RED)
        ErgTypes.life = "life"(TextFormatting.LIGHT_PURPLE)
        ErgTypes.knowledge = "knowledge"(TextFormatting.DARK_GREEN)
        ErgTypes.energy = "energy"(TextFormatting.GOLD)
        ErgTypes.submission = "submission"(TextFormatting.DARK_GRAY)
        ErgTypes.christmas = "christmas"(TextFormatting.DARK_GREEN)
        ErgTypes.freeze = "freeze"(TextFormatting.AQUA)
        ErgTypes.thunder = "thunder"(TextFormatting.YELLOW)
        ErgTypes.levitate = "levitate"(TextFormatting.BLUE)

        ErgTypes.values = values
    }
}


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
    override fun getName() = name
    override fun getTextColor() = textColor
}


val IErgType.displayName get() = buildText { translate("mirageFairy2019.erg.$name.name").color(textColor) }
