package miragefairy2019.mod.fairyweapon.magic4

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.displayName
import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.displayName
import net.minecraft.util.text.ITextComponent

interface IMagicStatusContainer {
    val magicStatusList: MutableList<MagicStatus<*>>
}

class FormulaScope(private val formulaArguments: FormulaArguments) {
    operator fun Mana.not() = formulaArguments.getMana(this)
    operator fun Erg.not() = formulaArguments.getErg(this)
    operator fun IMastery.not() = formulaArguments.getSkillLevel(this)
    val costFactor get() = formulaArguments.cost / 50.0
    operator fun <T> (FormulaArguments.() -> T).not() = this(formulaArguments)
}

class FormulaRendererSelector<T>

fun <T> IMagicStatusContainer.status(
    name: String,
    formula: FormulaScope.() -> T,
    formulaRendererGetter: FormulaRendererSelector<T>.() -> FormulaRenderer<T>,
    configurator: MagicStatusBuilder<T>.() -> Unit = {}
): MagicStatus<T> {
    val magicStatus = MagicStatusBuilder(
        name,
        Formula { FormulaScope(it).formula() },
        FormulaRendererSelector<T>().formulaRendererGetter(),
        EnumVisibility.ALWAYS
    ).also { it.configurator() }.build()
    magicStatusList += magicStatus
    return magicStatus
}


val <T> MagicStatus<T>.displayName get() = textComponent { translate("mirageFairy2019.magic.status.$name.name") }
fun <T> MagicStatus<T>.getDisplayValue(formulaArguments: FormulaArguments) = renderer.render(formulaArguments, formula)
val <T> MagicStatus<T>.factors: List<ITextComponent>
    get() {
        val factorList = mutableListOf<ITextComponent>()
        formula.calculate(object : FormulaArguments {
            override val hasPartnerFairy: Boolean get() = true
            override fun getMana(mana: Mana) = 0.0.also { factorList.add(mana.displayName) }
            override fun getErg(erg: Erg) = 0.0.also { factorList.add(erg.displayName) }
            override val cost get() = 0.0.also { factorList.add(textComponent { translate("mirageFairy2019.formula.source.cost.name").darkGray }) }
            override val color get() = 0x000000
            override fun getSkillLevel(mastery: IMastery) = 0.also { factorList.add(textComponent { mastery.displayName().gold }) }
        })
        return factorList.distinctBy { it.unformattedText }
    }
