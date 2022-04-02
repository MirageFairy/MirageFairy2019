package miragefairy2019.mod.modules.fairyweapon.item

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
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.plus
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
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.white
import miragefairy2019.mod.modules.fairyweapon.findFairy
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.ALWAYS
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.DETAIL
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.NEVER
import miragefairy2019.mod3.artifacts.ClientPlayerProxy
import miragefairy2019.mod3.artifacts.PlayerProxy
import miragefairy2019.mod3.artifacts.playerAuraHandler
import miragefairy2019.mod3.artifacts.proxy
import miragefairy2019.mod3.artifacts.skillContainer
import miragefairy2019.mod3.magic.MagicStatus
import miragefairy2019.mod3.magic.MagicStatusFunctionArguments
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.api.IMagicStatus
import miragefairy2019.mod3.magic.api.IMagicStatusFormatter
import miragefairy2019.mod3.magic.api.IMagicStatusFunction
import miragefairy2019.mod3.magic.api.IMagicStatusFunctionArguments
import miragefairy2019.mod3.magic.displayName
import miragefairy2019.mod3.magic.factors
import miragefairy2019.mod3.magic.getDisplayValue
import miragefairy2019.mod3.magic.negative
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.magic.ranged
import miragefairy2019.mod3.main.Main.side
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.getSkillLevel
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.text.ITextComponent
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
    operator fun <T> IMagicStatus<T>.not(): T = function.getValue(MagicStatusFunctionArguments({ getSkillLevel(it) }, fairyType))
    fun getSkillLevel(mastery: IMastery) = playerProxy.skillContainer.getSkillLevel(mastery)
    val fairyType get() = item.getActualFairyType(playerProxy, partnerFairyType)
}

typealias Magic = MagicScope.() -> IMagicHandler

fun magic(magic: Magic) = magic

fun Magic?.getMagicHandler(magicScope: MagicScope) = this?.invoke(magicScope) ?: object : IMagicHandler {}


abstract class ItemFairyWeaponBase3(
    val weaponMana: Mana,
    val mastery: IMastery
) : ItemFairyWeapon() {
    companion object {
        enum class EnumVisibility { ALWAYS, DETAIL, NEVER }
        class MagicStatusWrapper<T>(var magicStatus: IMagicStatus<T>) : IMagicStatus<T> {
            @JvmField
            var visibility = NEVER
            fun setVisibility(it: EnumVisibility) = apply { this.visibility = it }
            override fun getName(): String = magicStatus.name
            override fun getFunction(): IMagicStatusFunction<T> = magicStatus.function
            override fun getFormatter(): IMagicStatusFormatter<T> = magicStatus.formatter
        }

        fun <T : Comparable<T>> MagicStatusWrapper<T>.setRange(range: ClosedRange<T>) = apply { magicStatus = magicStatus.ranged(range.start, range.endInclusive) }

        class MagicStatusFormatterScope<T> {
            private fun <T> f(block: (T) -> ITextComponent) = IMagicStatusFormatter<T> { function, arguments -> block(function.getValue(arguments)) }
            val string get() = f<T> { textComponent { format("%s", it) } }
            val int get() = f<Int> { textComponent { format("%d", it) } }
            val double0 get() = f<Double> { textComponent { format("%.0f", it) } }
            val double1 get() = f<Double> { textComponent { format("%.1f", it) } }
            val double2 get() = f<Double> { textComponent { format("%.2f", it) } }
            val double3 get() = f<Double> { textComponent { format("%.3f", it) } }
            val percent0 get() = f<Double> { textComponent { format("%.0f%%", it * 100) } }
            val percent1 get() = f<Double> { textComponent { format("%.1f%%", it * 100) } }
            val percent2 get() = f<Double> { textComponent { format("%.2f%%", it * 100) } }
            val percent3 get() = f<Double> { textComponent { format("%.3f%%", it * 100) } }
            val boolean get() = f<Boolean> { textComponent { if (it) "Yes"() else "No"() } }
            val tick get() = f<Double> { textComponent { format("%.2f sec", it / 20.0) } }
        }

        class MagicStatusFormulaScope(val arguments: IMagicStatusFunctionArguments) {
            fun getSkillLevel(mastery: IMastery) = arguments.getSkillLevel(mastery)
            val cost get() = arguments.cost
            operator fun Mana.not() = arguments.getManaValue(this)
            operator fun Erg.not() = arguments.getErgValue(this)
            operator fun <T> IMagicStatus<T>.not(): T = function.getValue(arguments)
        }

        /**
         * @return
         * スキルレベルが[minSkillLevel]に届かない場合、0。
         * スキルレベルが[minSkillLevel]から[maxSkillLevel]にかけて、[minValue]と[maxValue]の間を遷移。
         * スキルレベルが[maxSkillLevel]を超える場合、[maxValue]でリミット。
         */
        fun MagicStatusFormulaScope.skillFunction1(mastery: IMastery, minSkillLevel: Int, maxSkillLevel: Int, minValue: Double, maxValue: Double): Double {
            val skillLevel = getSkillLevel(mastery)
            return when {
                skillLevel < minSkillLevel -> 0.0
                skillLevel > maxSkillLevel -> maxValue
                else -> minValue + (maxValue - minValue) * ((skillLevel - minSkillLevel) / (maxSkillLevel - minSkillLevel))
            }
        }
    }


    // Magic Status

    private val magicStatusWrapperList = mutableListOf<MagicStatusWrapper<*>>()
    internal operator fun <T> String.invoke(getFormatter: MagicStatusFormatterScope<T>.() -> IMagicStatusFormatter<T>, function: MagicStatusFormulaScope.() -> T): MagicStatusWrapper<T> {
        val magicStatusWrapper = MagicStatusWrapper<T>(
            MagicStatus(
                this,
                IMagicStatusFunction<T> { MagicStatusFormulaScope(it).function() },
                MagicStatusFormatterScope<T>().getFormatter()
            )
        )
        magicStatusWrapperList += magicStatusWrapper
        return magicStatusWrapper
    }

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {
        val playerProxy = ClientPlayerProxy
        val actualFairyType = getActualFairyType(playerProxy, fairyType)
        magicStatusWrapperList.forEach {
            val show = when (it.visibility) {
                ALWAYS -> true
                DETAIL -> flag.isAdvanced
                NEVER -> false
            }
            if (show) {
                tooltip += formattedText {
                    fun <T> TextComponentScope.f(magicStatus: IMagicStatus<T>): TextComponentWrapper {
                        val list = magicStatus.function.factors.map { it() }.sandwich { ", "() }.flatten()
                        return if (list.isNotEmpty) " ("() + list + ")"() else empty
                    }
                    concat(
                        it.displayName(),
                        ": "(),
                        it.getDisplayValue(MagicStatusFunctionArguments({ ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it) }, actualFairyType))().white,
                        f(it)
                    ).blue
                }
            }
        }
    }


    // Magic

    open val magic: Magic? = null

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = magic.getMagicHandler(MagicScope(this, player.proxy, world, player, itemStack, fairyType))
        return ActionResult(magicHandler.onItemRightClick(hand), itemStack)
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!side.isClient) return // クライアントサイドでなければ中止
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity)?.second ?: EMPTY_FAIRY // 妖精取得
        if (!world.isRemote) return // クライアントワールドでなければ中止

        val magicHandler = magic.getMagicHandler(MagicScope(this, entity.proxy, world, entity, itemStack, fairyType))
        magicHandler.onUpdate(itemSlot, isSelected)
    }

    override fun hitEntity(itemStack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        super.hitEntity(itemStack, target, attacker)
        if (attacker !is EntityPlayer) return true // プレイヤー取得
        val fairyType = findFairy(itemStack, attacker)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = magic.getMagicHandler(MagicScope(this, attacker.proxy, attacker.world, attacker, itemStack, fairyType))
        magicHandler.hitEntity(target)
        return true
    }
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

fun ItemFairyWeaponBase3.createStrengthStatus(weaponStrength: Double, strengthErg: Erg) = "strength"({ double0.positive }) {
    (weaponStrength + !strengthErg + getSkillLevel(mastery) * 0.5) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !SHINE
        FIRE -> !FIRE
        WIND -> !WIND
        GAIA -> !GAIA
        AQUA -> !AQUA
        DARK -> !DARK
    }
}.setVisibility(ALWAYS)

fun ItemFairyWeaponBase3.createExtentStatus(weaponExtent: Double, extentErg: Erg) = "extent"({ double0.positive }) {
    (weaponExtent + !extentErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !GAIA + !WIND
        FIRE -> !GAIA + !WIND
        WIND -> !GAIA * 2
        GAIA -> !WIND * 2
        AQUA -> !GAIA + !WIND
        DARK -> !GAIA + !WIND
    }
}.setVisibility(ALWAYS)

fun ItemFairyWeaponBase3.createEnduranceStatus(weaponEndurance: Double, enduranceErg: Erg) = "endurance"({ double0.positive }) {
    (weaponEndurance + !enduranceErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !FIRE + !AQUA
        FIRE -> !AQUA * 2
        WIND -> !FIRE + !AQUA
        GAIA -> !FIRE + !AQUA
        AQUA -> !FIRE * 2
        DARK -> !FIRE + !AQUA
    }
}.setVisibility(ALWAYS)

fun ItemFairyWeaponBase3.createProductionStatus(weaponProduction: Double, productionErg: Erg) = "production"({ double0.positive }) {
    (weaponProduction + !productionErg) * (cost / 50.0) + when (weaponMana) {
        SHINE -> !DARK * 2
        FIRE -> !SHINE + !DARK
        WIND -> !SHINE + !DARK
        GAIA -> !SHINE + !DARK
        AQUA -> !SHINE + !DARK
        DARK -> !SHINE * 2
    }
}.setVisibility(ALWAYS)

fun ItemFairyWeaponBase3.createCostStatus() = "cost"({ double0.negative }) { cost / (1.0 + getSkillLevel(mastery) * 0.002) }.setVisibility(ALWAYS)
