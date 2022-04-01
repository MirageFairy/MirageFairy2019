package miragefairy2019.mod.formula4

import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.displayName
import miragefairy2019.lib.get
import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.magic4.Formula
import miragefairy2019.mod.magic4.FormulaArguments
import miragefairy2019.mod.magic4.FormulaRenderer
import miragefairy2019.mod.magic4.MagicStatus
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.api.ISkillContainer
import miragefairy2019.mod3.skill.displayName
import miragefairy2019.mod3.skill.getSkillLevel
import net.minecraft.util.text.ITextComponent

interface MagicStatusContainer {
    val magicStatusList: MutableList<MagicStatus<*>>
}

class FormulaScope(private val formulaArguments: FormulaArguments) {
    operator fun Mana.not() = formulaArguments.getRawMana(this)
    operator fun Erg.not() = formulaArguments.getRawErg(this)
    operator fun IMastery.not() = formulaArguments.getSkillLevel(this)
    val cost get() = formulaArguments.cost
    val costFactor get() = cost / 50.0
}

class SimpleFormulaArguments(
    override val hasPartnerFairy: Boolean,
    private val manaSet: ManaSet,
    private val ergSet: ErgSet,
    override val cost: Double,
    override val color: Int,
    private val skillContainer: ISkillContainer
) : FormulaArguments {
    override fun getRawMana(mana: Mana) = manaSet[mana] / (cost / 50.0)
    override fun getRawErg(erg: Erg) = ergSet[erg]
    override fun getSkillLevel(mastery: IMastery) = skillContainer.getSkillLevel(mastery)
}

class FormulaRendererSelector<T>

fun <T> MagicStatusContainer.status(
    name: String,
    formula: FormulaScope.() -> T,
    formulaRendererGetter: FormulaRendererSelector<T>.() -> FormulaRenderer<T>
): MagicStatus<T> {
    val magicStatus = MagicStatus(
        name,
        object : Formula<T> {
            override fun calculate(formulaArguments: FormulaArguments) = FormulaScope(formulaArguments).formula()
        },
        FormulaRendererSelector<T>().formulaRendererGetter()
    )
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
            override fun getRawMana(mana: Mana) = 0.0.also { factorList.add(mana.displayName) }
            override fun getRawErg(erg: Erg) = 0.0.also { factorList.add(erg.displayName) }
            override val cost get() = 0.0.also { factorList.add(textComponent { translate("mirageFairy2019.formula.source.cost.name").darkGray }) }
            override val color get() = 0x000000
            override fun getSkillLevel(mastery: IMastery) = 0.also { factorList.add(textComponent { mastery.displayName().gold }) }
        })
        return factorList.distinctBy { it.unformattedText }
    }
