package miragefairy2019.mod.modules.fairycrystal

import miragefairy2019.libkt.blue
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.mod.api.ApiFairyCrystal
import miragefairy2019.mod.lib.multi.ItemVariant
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.ISkillContainer
import miragefairy2019.mod3.skill.displayName
import miragefairy2019.mod3.skill.getSkillLevel
import mirrg.boron.util.suppliterator.ISuppliterator
import mirrg.kotlin.formatAs
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.world.World

abstract class VariantFairyCrystalBase(
    val registryName: String,
    val unlocalizedName: String,
    val oreName: String
) : ItemVariant() {
    fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val mastery = EnumMastery.fairySummoning
        val skillContainer = ApiSkill.skillManager.clientSkillContainer
        tooltip += formattedText { (!"スキル: " + !mastery.displayName + !" (${skillContainer.getSkillLevel(mastery)})").gold } // TODO translate
        tooltip += formattedText { (!"レア判定ブースト: ${getRareBoost(skillContainer) * 100.0 formatAs "%.2f%%"}").blue } // TODO translate
    }

    open fun getRareBoost(skillContainer: ISkillContainer): Double {
        val a = itemRareBoost
        val b = 1.0 + skillContainer.getSkillLevel(EnumMastery.fairySummoning) * 0.01
        return a * b
    }

    open val dropRank get() = 0
    open val itemRareBoost get() = 1.0
    open val dropper
        get() = object : FairyCrystalDropper() {
            override fun getDropList() = ISuppliterator.ofIterable(ApiFairyCrystal.dropsFairyCrystal)
        }
}
