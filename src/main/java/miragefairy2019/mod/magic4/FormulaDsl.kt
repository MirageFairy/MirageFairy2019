package miragefairy2019.mod.formula4

import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.magic4.Formula
import miragefairy2019.mod.magic4.FormulaArguments
import miragefairy2019.mod.magic4.FormulaRenderer
import miragefairy2019.mod.magic4.MagicStatus
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.IErgSet
import miragefairy2019.mod3.erg.displayName
import miragefairy2019.api.Mana
import miragefairy2019.lib.IManaSet
import miragefairy2019.lib.displayName
import miragefairy2019.lib.getMana
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
    operator fun EnumErgType.not() = formulaArguments.getRawErg(this)
    operator fun IMastery.not() = formulaArguments.getSkillLevel(this)
    val cost get() = formulaArguments.cost
    val costFactor get() = cost / 50.0
}

class SimpleFormulaArguments(
    override val hasPartnerFairy: Boolean,
    private val manaSet: IManaSet,
    private val ergSet: IErgSet,
    override val cost: Double,
    override val color: Int,
    private val skillContainer: ISkillContainer
) : FormulaArguments {
    override fun getRawMana(manaType: Mana) = manaSet.getMana(manaType) / (cost / 50.0)
    override fun getRawErg(ergType: EnumErgType) = ergSet.getPower(ergType)
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
            override fun getRawMana(manaType: Mana) = 0.0.also { factorList.add(manaType.displayName) }
            override fun getRawErg(ergType: EnumErgType) = 0.0.also { factorList.add(ergType.displayName) }
            override val cost get() = 0.0.also { factorList.add(textComponent { translate("mirageFairy2019.formula.source.cost.name").darkGray }) }
            override val color get() = 0x000000
            override fun getSkillLevel(mastery: IMastery) = 0.also { factorList.add(textComponent { (!mastery.displayName).gold }) }
        })
        return factorList.distinctBy { it.unformattedText }
    }
