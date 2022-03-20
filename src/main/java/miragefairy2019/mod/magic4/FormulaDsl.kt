package miragefairy2019.mod.formula4

import miragefairy2019.libkt.darkGray
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.magic4.api.Formula
import miragefairy2019.mod.magic4.api.FormulaArguments
import miragefairy2019.mod.magic4.api.FormulaRenderer
import miragefairy2019.mod.magic4.api.MagicStatus
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.IErgSet
import miragefairy2019.mod3.erg.displayName
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.api.IManaSet
import miragefairy2019.mod3.mana.displayName
import miragefairy2019.mod3.mana.getMana
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.api.ISkillContainer
import miragefairy2019.mod3.skill.displayName
import miragefairy2019.mod3.skill.getSkillLevel
import net.minecraft.util.text.ITextComponent

interface MagicStatusContainer {
    val magicStatusList: MutableList<MagicStatus<*>>
}

class FormulaScope(private val formulaArguments: FormulaArguments) {
    operator fun EnumManaType.unaryPlus() = formulaArguments.getMana(this)
    operator fun EnumErgType.not() = formulaArguments.getErg(this)

    /** @return コストによる補正を受けないマナの値 */
    operator fun EnumManaType.not() = formulaArguments.getMana(this) / costFactor

    /** @return コストによる補正を受けたエルグの値 */
    operator fun EnumErgType.unaryPlus() = formulaArguments.getErg(this) * costFactor

    val cost get() = formulaArguments.cost
    val costFactor get() = cost / 50.0
    operator fun IMastery.not() = formulaArguments.getSkillLevel(this)
}

class SimpleFormulaArguments(
    override val hasPartnerFairy: Boolean,
    private val manaSet: IManaSet,
    private val ergSet: IErgSet,
    override val cost: Double,
    private val skillContainer: ISkillContainer
) : FormulaArguments {
    override fun getMana(manaType: EnumManaType) = manaSet.getMana(manaType)
    override fun getErg(ergType: EnumErgType) = ergSet.getPower(ergType)
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
            override fun getMana(manaType: EnumManaType) = 0.0.also { factorList.add(manaType.displayName) }
            override fun getErg(ergType: EnumErgType) = 0.0.also { factorList.add(ergType.displayName) }
            override val cost get() = 0.0.also { factorList.add(textComponent { translate("mirageFairy2019.formula.source.cost.name").darkGray }) }
            override fun getSkillLevel(mastery: IMastery) = 0.also { factorList.add(textComponent { (!mastery.displayName).gold }) }
        })
        return factorList.distinctBy { it.unformattedText }
    }
