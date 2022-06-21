package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyType
import miragefairy2019.api.Mana
import miragefairy2019.api.Mana.AQUA
import miragefairy2019.api.Mana.DARK
import miragefairy2019.api.Mana.FIRE
import miragefairy2019.api.Mana.GAIA
import miragefairy2019.api.Mana.SHINE
import miragefairy2019.api.Mana.WIND
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.ClientPlayerProxy
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.PlayerProxy
import miragefairy2019.lib.playerAuraHandler
import miragefairy2019.lib.plus
import miragefairy2019.lib.proxy
import miragefairy2019.lib.skillContainer
import miragefairy2019.lib.times
import miragefairy2019.libkt.TextComponentScope
import miragefairy2019.libkt.TextComponentWrapper
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.concat
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.isNotEmpty
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.white
import miragefairy2019.mod.Main.side
import miragefairy2019.mod.fairyweapon.deprecated.MagicStatusFunctionArguments
import miragefairy2019.mod.fairyweapon.deprecated.ranged
import miragefairy2019.mod.fairyweapon.findFairy
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.FormulaRendererSelector
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.MagicStatus
import miragefairy2019.mod.fairyweapon.magic4.OldFormulaScope
import miragefairy2019.mod.fairyweapon.magic4.displayName
import miragefairy2019.mod.fairyweapon.magic4.factors
import miragefairy2019.mod.fairyweapon.magic4.float0
import miragefairy2019.mod.fairyweapon.magic4.getDisplayValue
import miragefairy2019.mod.skill.ApiSkill
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.getSkillLevel
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly


class MagicScope(
    val item: ItemFairyWeaponBase3,
    val playerProxy: PlayerProxy,
    val world: World,
    val player: EntityPlayer,
    val itemStack: ItemStack,
    val partnerFairyType: IFairyType
) {
    operator fun <T> MagicStatusWrapper<T>.not() = !magicStatus
    operator fun <T> MagicStatus<T>.not(): T = formula.calculate(MagicStatusFunctionArguments(playerProxy, { getSkillLevel(it) }, fairyType))
    fun getSkillLevel(mastery: IMastery) = playerProxy.skillContainer.getSkillLevel(mastery)
    val fairyType get() = item.getActualFairyType(playerProxy, partnerFairyType)
}

typealias Magic = MagicScope.() -> MagicHandler

fun magic(magic: Magic) = magic

fun Magic?.getMagicHandler(magicScope: MagicScope) = this?.invoke(magicScope) ?: MagicHandler()


class MagicStatusWrapper<T>(var magicStatus: MagicStatus<T>)

fun <T> MagicStatusWrapper<T>.setVisibility(visibility: EnumVisibility) = apply { magicStatus = MagicStatus(magicStatus.name, magicStatus.formula, magicStatus.renderer, visibility) }
fun <T : Comparable<T>> MagicStatusWrapper<T>.setRange(range: ClosedRange<T>) = apply { magicStatus = magicStatus.ranged(range.start, range.endInclusive) }


abstract class ItemFairyWeaponBase3(
    val weaponMana: Mana,
    val mastery: IMastery
) : ItemFairyWeapon() {

    // Magic Status

    val magicStatusWrapperList = mutableListOf<MagicStatusWrapper<*>>()

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {
        val playerProxy = ClientPlayerProxy
        val actualFairyType = getActualFairyType(playerProxy, fairyType)
        magicStatusWrapperList.forEach {
            val show = when (it.magicStatus.visibility) {
                EnumVisibility.ALWAYS -> true
                EnumVisibility.DETAIL -> flag.isAdvanced
                EnumVisibility.NEVER -> false
            }
            if (show) {
                tooltip += formattedText {
                    fun <T> TextComponentScope.f(magicStatus: MagicStatus<T>): TextComponentWrapper {
                        val list = magicStatus.factors.map { it() }.sandwich { ", "() }.flatten()
                        return if (list.isNotEmpty) " ("() + list + ")"() else empty
                    }
                    concat(
                        it.magicStatus.displayName(),
                        ": "(),
                        it.magicStatus.getDisplayValue(MagicStatusFunctionArguments(playerProxy, { mastery -> ApiSkill.skillManager.getClientSkillContainer().getSkillLevel(mastery) }, actualFairyType))().white,
                        f(it.magicStatus)
                    ).blue
                }
            }
        }
    }


    // Magic

    open fun getMagic(): Magic? = null

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(MagicScope(this, player.proxy, world, player, itemStack, fairyType))
        return ActionResult(magicHandler.onItemRightClick(hand), itemStack)
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!side.isClient) return // クライアントサイドでなければ中止
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity)?.second ?: EMPTY_FAIRY // 妖精取得
        if (!world.isRemote) return // クライアントワールドでなければ中止

        val magicHandler = getMagic().getMagicHandler(MagicScope(this, entity.proxy, world, entity, itemStack, fairyType))
        magicHandler.onUpdate(itemSlot, isSelected)
    }

    override fun hitEntity(itemStack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        super.hitEntity(itemStack, target, attacker)
        if (attacker !is EntityPlayer) return true // プレイヤー取得
        val fairyType = findFairy(itemStack, attacker)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(MagicScope(this, attacker.proxy, attacker.world, attacker, itemStack, fairyType))
        magicHandler.hitEntity(target)
        return true
    }
}

fun <T> ItemFairyWeaponBase3.status(
    name: String,
    formula: OldFormulaScope.() -> T,
    formulaRendererGetter: FormulaRendererSelector<T>.() -> FormulaRenderer<T>
): MagicStatusWrapper<T> {
    val magicStatusWrapper = MagicStatusWrapper(
        MagicStatus(
            name,
            Formula { OldFormulaScope(it).formula() },
            FormulaRendererSelector<T>().formulaRendererGetter(),
            EnumVisibility.NEVER
        )
    )
    magicStatusWrapperList += magicStatusWrapper
    return magicStatusWrapper
}


// Actual Fairy Type

fun ItemFairyWeaponBase3.getActualFairyType(playerProxy: PlayerProxy, fairyTypePartner: IFairyType) = object : IFairyType {
    override fun isEmpty() = fairyTypePartner.isEmpty
    override fun getMotif() = fairyTypePartner.motif
    override fun getDisplayName() = fairyTypePartner.displayName
    override fun getColor() = fairyTypePartner.color
    override fun getCost() = fairyTypePartner.cost
    override fun getManaSet(): ManaSet {
        val a1 = fairyTypePartner.manaSet
        val a2 = playerProxy.playerAuraHandler.playerAura * (fairyTypePartner.cost / 50.0)
        val b1 = 0.001 * playerProxy.skillContainer.getSkillLevel(mastery)
        return (a1 + a2) * (1.0 + b1)
    }

    override fun getErgSet() = fairyTypePartner.ergSet
}


// Statuses

fun ItemFairyWeaponBase3.createStrengthStatus(weaponStrength: Double, strengthErg: Erg) = status("strength", {
    (weaponStrength + !strengthErg + !mastery * 0.5) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !SHINE
        FIRE -> !FIRE
        WIND -> !WIND
        GAIA -> !GAIA
        AQUA -> !AQUA
        DARK -> !DARK
    }
}) { float0 }.setVisibility(EnumVisibility.ALWAYS)

fun ItemFairyWeaponBase3.createExtentStatus(weaponExtent: Double, extentErg: Erg) = status("extent", {
    (weaponExtent + !extentErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !GAIA + !WIND
        FIRE -> !GAIA + !WIND
        WIND -> !GAIA * 2
        GAIA -> !WIND * 2
        AQUA -> !GAIA + !WIND
        DARK -> !GAIA + !WIND
    }
}) { float0 }.setVisibility(EnumVisibility.ALWAYS)

fun ItemFairyWeaponBase3.createEnduranceStatus(weaponEndurance: Double, enduranceErg: Erg) = status("endurance", {
    (weaponEndurance + !enduranceErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !FIRE + !AQUA
        FIRE -> !AQUA * 2
        WIND -> !FIRE + !AQUA
        GAIA -> !FIRE + !AQUA
        AQUA -> !FIRE * 2
        DARK -> !FIRE + !AQUA
    }
}) { float0 }.setVisibility(EnumVisibility.ALWAYS)

fun ItemFairyWeaponBase3.createProductionStatus(weaponProduction: Double, productionErg: Erg) = status("production", {
    (weaponProduction + !productionErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !DARK * 2
        FIRE -> !SHINE + !DARK
        WIND -> !SHINE + !DARK
        GAIA -> !SHINE + !DARK
        AQUA -> !SHINE + !DARK
        DARK -> !SHINE * 2
    }
}) { float0 }.setVisibility(EnumVisibility.ALWAYS)

fun ItemFairyWeaponBase3.createCostStatus() = status("cost", { cost / (1.0 + !mastery * 0.002) }) { float0 }.setVisibility(EnumVisibility.ALWAYS)
