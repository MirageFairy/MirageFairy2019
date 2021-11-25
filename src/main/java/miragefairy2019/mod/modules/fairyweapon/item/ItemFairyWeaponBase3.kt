package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.TextComponentScope
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.red
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.white
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.ALWAYS
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.DETAIL
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase3.Companion.EnumVisibility.NEVER
import miragefairy2019.mod3.erg.api.IErgType
import miragefairy2019.mod3.fairy.api.IFairyType
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
import miragefairy2019.mod3.main.api.ApiMain.side
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.api.EnumManaType.AQUA
import miragefairy2019.mod3.mana.api.EnumManaType.DARK
import miragefairy2019.mod3.mana.api.EnumManaType.FIRE
import miragefairy2019.mod3.mana.api.EnumManaType.GAIA
import miragefairy2019.mod3.mana.api.EnumManaType.SHINE
import miragefairy2019.mod3.mana.api.EnumManaType.WIND
import miragefairy2019.mod3.mana.api.IManaSet
import miragefairy2019.mod3.mana.getMana
import miragefairy2019.mod3.mana.plus
import miragefairy2019.mod3.mana.times
import miragefairy2019.mod3.playeraura.api.ApiPlayerAura
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.getSkillLevel
import miragefairy2019.modkt.impl.fairy.FairyTypeAdapter
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

abstract class ItemFairyWeaponBase3(
    val weaponManaType: EnumManaType,
    val mastery: IMastery,
    val weaponStrength: Double?,
    val weaponExtent: Double?,
    val weaponEndurance: Double?,
    val weaponProduction: Double?,
    val strengthErg: IErgType?,
    val extentErg: IErgType?,
    val enduranceErg: IErgType?,
    val productionErg: IErgType?
) : ItemFairyWeaponBase() {
    companion object {
        private const val prefix = "miragefairy2019.gui.magic"


        class MagicScope(val getSkillLevel: (IMastery) -> Int, val world: World, val player: EntityPlayer, val itemStack: ItemStack, val fairyType: IFairyType) {
            operator fun <T> IMagicStatus<T>.not(): T = function.getValue(MagicStatusFunctionArguments({ getSkillLevel(it) }, fairyType))
        }


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
            val boolean get() = f<Boolean> { textComponent { if (it) !"Yes" else !"No" } }
            val tick get() = f<Double> { textComponent { format("%.2f sec", it / 20.0) } }
        }

        class MagicStatusFormulaScope(val arguments: IMagicStatusFunctionArguments) {
            fun getSkillLevel(mastery: IMastery) = arguments.getSkillLevel(mastery)
            val cost get() = arguments.fairyType.cost
            operator fun EnumManaType.not() = arguments.fairyType.manaSet.getMana(this)
            operator fun IErgType.not() = arguments.fairyType.ergSet.getPower(this)
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

    // Magic

    private var magicProvider: (MagicScope.() -> IMagicHandler)? = null
    internal fun magic(magicProvider: MagicScope.() -> IMagicHandler) {
        if (this.magicProvider != null) throw Exception("Multiple magic definition")
        this.magicProvider = magicProvider
    }

    private fun getMagicHandler(magicScope: MagicScope) = magicProvider?.invoke(magicScope) ?: object : IMagicHandler {}

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

    // Actual Fairy Type

    // TODO 統合
    private fun getActualFairyTypeServer(player: EntityPlayer, fairyTypePartner: IFairyType): IFairyType = object : FairyTypeAdapter(fairyTypePartner) {
        override fun getManaSet(): IManaSet {
            val a1 = parent.manaSet
            val a2 = ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player as EntityPlayerMP).playerAura * (parent.cost / 50.0)
            val b1 = 0.001 * ApiSkill.skillManager.getServerSkillContainer(player).getSkillLevel(mastery)
            return (a1 + a2) * (1.0 + b1)
        }
    }

    // TODO 統合
    @SideOnly(Side.CLIENT)
    private fun getActualFairyTypeClient(fairyTypePartner: IFairyType): IFairyType = object : FairyTypeAdapter(fairyTypePartner) {
        override fun getManaSet(): IManaSet {
            val a1 = parent.manaSet
            val a2 = ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler.playerAura * (parent.cost / 50.0)
            val b1 = 0.001 * ApiSkill.skillManager.clientSkillContainer.getSkillLevel(mastery)
            return (a1 + a2) * (1.0 + b1)
        }
    }

    // Overrides

    @SideOnly(Side.CLIENT)
    override fun addInformationFunctions(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { translate("$prefix.message.rightClick").red }
        super.addInformationFunctions(itemStack, world, tooltip, flag)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val actualFairyType = getActualFairyTypeClient(fairyType)
        magicStatusWrapperList.forEach {
            if (when (it.visibility) {
                    ALWAYS -> true
                    DETAIL -> flag.isAdvanced
                    NEVER -> false
                }
            ) {
                tooltip += formattedText {
                    fun <T> TextComponentScope.f(magicStatus: IMagicStatus<T>): List<ITextComponent> {
                        val list = magicStatus.function.factors.map { !it }.sandwich { !", " }.flatten()
                        return if (list.isNotEmpty()) !" (" + list + !")" else empty
                    }
                    join(
                        !it.displayName,
                        !": ",
                        (!it.getDisplayValue(MagicStatusFunctionArguments({ ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it) }, actualFairyType))).white,
                        f(it)
                    ).blue
                }
            }
        }
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (world.isRemote) {
            val actualFairyType = getActualFairyTypeClient(fairyType)
            return ActionResult(getMagicHandler(MagicScope({ ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it) }, world, player, itemStack, actualFairyType)).onItemRightClick(hand), itemStack)
        } else {
            val actualFairyType = getActualFairyTypeServer(player, fairyType)
            return ActionResult(getMagicHandler(MagicScope({ ApiSkill.skillManager.getServerSkillContainer(player).getSkillLevel(it) }, world, player, itemStack, actualFairyType)).onItemRightClick(hand), itemStack)
        }
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!side.isClient) return // クライアントサイドでなければ中止
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (world.isRemote) {
            val actualFairyType = getActualFairyTypeClient(fairyType)
            getMagicHandler(MagicScope({ ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it) }, world, entity, itemStack, actualFairyType)).onUpdate(itemSlot, isSelected)
        }
    }

    override fun hitEntity(itemStack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        super.hitEntity(itemStack, target, attacker)
        if (attacker !is EntityPlayer) return true // プレイヤー取得
        val fairyType = findFairy(itemStack, attacker).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (attacker.world.isRemote) {
            val actualFairyType = getActualFairyTypeClient(fairyType)
            getMagicHandler(MagicScope({ ApiSkill.skillManager.clientSkillContainer.getSkillLevel(it) }, attacker.world, attacker, itemStack, actualFairyType)).hitEntity(target)
        } else {
            val actualFairyType = getActualFairyTypeServer(attacker, fairyType)
            getMagicHandler(MagicScope({ ApiSkill.skillManager.getServerSkillContainer(attacker).getSkillLevel(it) }, attacker.world, attacker, itemStack, actualFairyType)).hitEntity(target)
        }
        return true
    }

    // Statuses

    val strength = "strength"({ double0.positive }) {
        if (weaponStrength == null) 0.0 else (weaponStrength + (strengthErg?.let { !it } ?: 0.0) + getSkillLevel(mastery) * 0.5) * (cost / 50.0) + when (weaponManaType) {
            SHINE -> !SHINE
            FIRE -> !FIRE
            WIND -> !WIND
            GAIA -> !GAIA
            AQUA -> !AQUA
            DARK -> !DARK
        }
    }.setVisibility(ALWAYS)

    val extent = "extent"({ double0.positive }) {
        if (weaponExtent == null) 0.0 else (weaponExtent + (extentErg?.let { !it } ?: 0.0)) * (cost / 50.0) + when (weaponManaType) {
            SHINE -> !GAIA + !WIND
            FIRE -> !GAIA + !WIND
            WIND -> !GAIA * 2
            GAIA -> !WIND * 2
            AQUA -> !GAIA + !WIND
            DARK -> !GAIA + !WIND
        }
    }.setVisibility(ALWAYS)

    val endurance = "endurance"({ double0.positive }) {
        if (weaponEndurance == null) 0.0 else (weaponEndurance + (enduranceErg?.let { !it } ?: 0.0)) * (cost / 50.0) + when (weaponManaType) {
            SHINE -> !FIRE + !AQUA
            FIRE -> !AQUA * 2
            WIND -> !FIRE + !AQUA
            GAIA -> !FIRE + !AQUA
            AQUA -> !FIRE * 2
            DARK -> !FIRE + !AQUA
        }
    }.setVisibility(ALWAYS)

    val production = "production"({ double0.positive }) {
        if (weaponProduction == null) 0.0 else (weaponProduction + (productionErg?.let { !it } ?: 0.0)) * (cost / 50.0) + when (weaponManaType) {
            SHINE -> !DARK * 2
            FIRE -> !SHINE + !DARK
            WIND -> !SHINE + !DARK
            GAIA -> !SHINE + !DARK
            AQUA -> !SHINE + !DARK
            DARK -> !SHINE * 2
        }
    }.setVisibility(ALWAYS)

    val cost = "cost"({ double0.negative }) { cost / (1.0 + getSkillLevel(mastery) * 0.002) }.setVisibility(ALWAYS)
}
