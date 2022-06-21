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
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.erg
import miragefairy2019.lib.get
import miragefairy2019.lib.playerAuraHandler
import miragefairy2019.lib.plus
import miragefairy2019.lib.proxy
import miragefairy2019.lib.skillContainer
import miragefairy2019.lib.times
import miragefairy2019.libkt.TextComponentScope
import miragefairy2019.libkt.TextComponentWrapper
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.bold
import miragefairy2019.libkt.concat
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.isNotEmpty
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.white
import miragefairy2019.mod.Main.side
import miragefairy2019.mod.fairyweapon.findFairy
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.Formula
import miragefairy2019.mod.fairyweapon.magic4.FormulaRenderer
import miragefairy2019.mod.fairyweapon.magic4.FormulaRendererSelector
import miragefairy2019.mod.fairyweapon.magic4.FormulaScope
import miragefairy2019.mod.fairyweapon.magic4.IMagicStatusContainer
import miragefairy2019.mod.fairyweapon.magic4.Magic
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicStatus
import miragefairy2019.mod.fairyweapon.magic4.MagicStatusBuilder
import miragefairy2019.mod.fairyweapon.magic4.displayName
import miragefairy2019.mod.fairyweapon.magic4.factors
import miragefairy2019.mod.fairyweapon.magic4.float0
import miragefairy2019.mod.fairyweapon.magic4.getDisplayValue
import miragefairy2019.mod.fairyweapon.magic4.getMagicHandler
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.getSkillLevel
import net.minecraft.client.Minecraft
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


fun <T> MagicStatusBuilder<T>.setVisibility(visibility: EnumVisibility) = apply { this.visibility = visibility }
fun <T : Comparable<T>> MagicStatusBuilder<T>.setRange(range: ClosedRange<T>) = apply {
    formula = Formula { formula.calculate(it).coerceIn(range.start, range.endInclusive) }
    renderer = FormulaRenderer { arguments, function ->
        val value = function.calculate(arguments)
        val displayValue = renderer.render(arguments, function)
        when (value) {
            range.start -> textComponent { displayValue().bold }
            range.endInclusive -> textComponent { displayValue().bold }
            else -> displayValue
        }
    }
}


abstract class ItemFairyWeaponBase3(
    val weaponMana: Mana,
    val mastery: IMastery
) : ItemFairyWeapon(), IMagicStatusContainer {

    // Magic Status

    override val magicStatusList = mutableListOf<MagicStatus<*>>()

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player ?: return
        magicStatusList.forEach {
            val show = when (it.visibility) {
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

                    val formulaArguments = getMagicArguments(player, itemStackFairyWeapon, fairyType)
                    concat(
                        it.displayName(),
                        ": "(),
                        it.getDisplayValue(formulaArguments)().white,
                        f(it)
                    ).blue
                }
            }
        }
    }


    // Magic

    open fun getMagic(): Magic? = null

    private fun getMagicArguments(player: EntityPlayer, weaponItemStack: ItemStack, partnerFairyType: IFairyType) = object : MagicArguments {
        override val hasPartnerFairy get() = !partnerFairyType.isEmpty
        override fun getRawMana(mana: Mana): Double {
            val a = partnerFairyType.manaSet // パートナー妖精のマナ
            val b = a + player.proxy.playerAuraHandler.playerAura * (partnerFairyType.cost / 50.0) // プレイヤーオーラの加算
            val c = b * (1.0 + 0.001 * player.proxy.skillContainer.getSkillLevel(this.weaponItem.mastery)) // スキルレベル補正
            return c[mana]
        }

        override fun getRawErg(erg: Erg) = partnerFairyType.erg(erg)
        override val cost get() = partnerFairyType.cost
        override val color get() = partnerFairyType.color
        override fun getSkillLevel(mastery: IMastery) = player.proxy.skillContainer.getSkillLevel(mastery)
        override val weaponItem get() = this@ItemFairyWeaponBase3
        override val weaponItemStack get() = weaponItemStack
        override val player get() = player
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(player, itemStack, fairyType))
        return ActionResult(magicHandler.onItemRightClick(hand), itemStack)
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!side.isClient) return // クライアントサイドでなければ中止
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity)?.second ?: EMPTY_FAIRY // 妖精取得
        if (!world.isRemote) return // クライアントワールドでなければ中止

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(entity, itemStack, fairyType))
        magicHandler.onUpdate(itemSlot, isSelected)
    }

    override fun hitEntity(itemStack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        super.hitEntity(itemStack, target, attacker)
        if (attacker !is EntityPlayer) return true // プレイヤー取得
        val fairyType = findFairy(itemStack, attacker)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(attacker, itemStack, fairyType))
        magicHandler.hitEntity(target)
        return true
    }
}

fun <T> ItemFairyWeaponBase3.status(
    name: String,
    formula: FormulaScope.() -> T,
    formulaRendererGetter: FormulaRendererSelector<T>.() -> FormulaRenderer<T>,
    configurator: MagicStatusBuilder<T>.() -> Unit = {}
): MagicStatus<T> {
    val magicStatus = MagicStatusBuilder(
        name,
        Formula { FormulaScope(it).formula() },
        FormulaRendererSelector<T>().formulaRendererGetter(),
        EnumVisibility.NEVER
    ).also { it.configurator() }.build()
    magicStatusList += magicStatus
    return magicStatus
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
}, { float0 }) { setVisibility(EnumVisibility.ALWAYS) }

fun ItemFairyWeaponBase3.createExtentStatus(weaponExtent: Double, extentErg: Erg) = status("extent", {
    (weaponExtent + !extentErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !GAIA + !WIND
        FIRE -> !GAIA + !WIND
        WIND -> !GAIA * 2
        GAIA -> !WIND * 2
        AQUA -> !GAIA + !WIND
        DARK -> !GAIA + !WIND
    }
}, { float0 }) { setVisibility(EnumVisibility.ALWAYS) }

fun ItemFairyWeaponBase3.createEnduranceStatus(weaponEndurance: Double, enduranceErg: Erg) = status("endurance", {
    (weaponEndurance + !enduranceErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !FIRE + !AQUA
        FIRE -> !AQUA * 2
        WIND -> !FIRE + !AQUA
        GAIA -> !FIRE + !AQUA
        AQUA -> !FIRE * 2
        DARK -> !FIRE + !AQUA
    }
}, { float0 }) { setVisibility(EnumVisibility.ALWAYS) }

fun ItemFairyWeaponBase3.createProductionStatus(weaponProduction: Double, productionErg: Erg) = status("production", {
    (weaponProduction + !productionErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !DARK * 2
        FIRE -> !SHINE + !DARK
        WIND -> !SHINE + !DARK
        GAIA -> !SHINE + !DARK
        AQUA -> !SHINE + !DARK
        DARK -> !SHINE * 2
    }
}, { float0 }) { setVisibility(EnumVisibility.ALWAYS) }

fun ItemFairyWeaponBase3.createCostStatus() = status("cost", { cost / (1.0 + !mastery * 0.002) }, { float0 }) { setVisibility(EnumVisibility.ALWAYS) }
