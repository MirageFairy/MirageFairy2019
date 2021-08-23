package miragefairy2019.modkt.impl.fairy

import miragefairy2019.libkt.color
import miragefairy2019.libkt.text
import miragefairy2019.modkt.api.fairy.IAbilityType
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.*

class AbilityType(private val name: String, private val textColor: TextFormatting) : IAbilityType {
    companion object {
        val attack = AbilityType("attack", DARK_RED)
        val craft = AbilityType("craft", GOLD)
        val fell = AbilityType("fell", DARK_GREEN)
        val light = AbilityType("light", YELLOW)
        val flame = AbilityType("flame", RED)
        val water = AbilityType("water", BLUE)
        val crystal = AbilityType("crystal", AQUA)
        val art = AbilityType("art", GREEN)
        val store = AbilityType("store", GOLD)
        val warp = AbilityType("warp", DARK_PURPLE)
        val shoot = AbilityType("shoot", GREEN)
        val breaking = AbilityType("breaking", DARK_RED)
        val chemical = AbilityType("chemical", DARK_AQUA)
        val slash = AbilityType("slash", DARK_RED)
        val food = AbilityType("food", YELLOW)
        val knowledge = AbilityType("knowledge", DARK_GREEN)
        val energy = AbilityType("energy", GOLD)
        val submission = AbilityType("submission", DARK_GRAY)
        val christmas = AbilityType("christmas", DARK_GREEN)
        val freeze = AbilityType("freeze", AQUA)
        val thunder = AbilityType("thunder", YELLOW)
        fun values(): Array<IAbilityType> = arrayOf(
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


val IAbilityType.displayName get() = text { translate("mirageFairy2019.ability.$name.name").color(textColor) }
