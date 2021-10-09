package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.TextComponentScope
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.red
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.white
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.api.IMagicStatus
import miragefairy2019.mod3.magic.api.IMagicStatusFormatter
import miragefairy2019.mod3.magic.api.IMagicStatusFunction
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.getSkillLevel
import miragefairy2019.modkt.api.erg.IErgType
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.api.mana.IManaType
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.fairy.FairyTypeAdapter
import miragefairy2019.modkt.impl.getMana
import miragefairy2019.modkt.impl.magicstatus.MagicStatus
import miragefairy2019.modkt.impl.magicstatus.displayName
import miragefairy2019.modkt.impl.magicstatus.factors
import miragefairy2019.modkt.impl.magicstatus.ranged
import miragefairy2019.modkt.impl.plus
import miragefairy2019.modkt.impl.times
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
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
        val mastery: IMastery
) : ItemFairyWeaponBase() {
    companion object {
        private const val prefix = "miragefairy2019.gui.magic"

        class
        MagicScope(val skillLevel: Int, val world: World, val player: EntityPlayer, val itemStack: ItemStack, val fairyType: IFairyType) {
            operator fun <T> IMagicStatus<T>.not(): T = function.getValue(fairyType)
        }


        enum class EnumVisibility { ALWAYS, DETAIL, NEVER }
        class MagicStatusWrapper<T>(var magicStatus: IMagicStatus<T>) : IMagicStatus<T> {
            @JvmField
            var visibility = EnumVisibility.NEVER
            fun setVisibility(it: EnumVisibility) = apply { this.visibility = it }
            override fun getName(): String = magicStatus.name
            override fun getFunction(): IMagicStatusFunction<T> = magicStatus.function
            override fun getFormatter(): IMagicStatusFormatter<T> = magicStatus.formatter
        }

        fun <T : Comparable<T>> MagicStatusWrapper<T>.setRange(range: ClosedRange<T>) = apply { magicStatus = magicStatus.ranged(range.start, range.endInclusive) }


        class MagicStatusFormatterScope<T> {
            private fun <T> f(block: (T) -> ITextComponent) = IMagicStatusFormatter<T> { function, fairyType -> block(function.getValue(fairyType)) }
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

        class MagicStatusFormulaScope(val fairyType: IFairyType) {
            val cost get() = fairyType.cost
            operator fun IManaType.not() = fairyType.manaSet.getMana(this)
            operator fun IErgType.not() = fairyType.ergSet.getPower(this)
            operator fun <T> IMagicStatus<T>.not(): T = function.getValue(fairyType)
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
        val magicStatusWrapper = MagicStatusWrapper<T>(MagicStatus(this,
                IMagicStatusFunction<T> { MagicStatusFormulaScope(it).function() },
                MagicStatusFormatterScope<T>().getFormatter()))
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
        fun <T> TextComponentScope.f2(magicStatus: IMagicStatus<T>) = !magicStatus.formatter.getDisplayValue(magicStatus.function, actualFairyType)
        fun <T> TextComponentScope.f(magicStatus: IMagicStatus<T>): List<ITextComponent> {
            val list = magicStatus.function.factors.map { !it }.sandwich { !", " }.flatten()
            return if (list.isNotEmpty()) !" (" + list + !")" else empty
        }
        magicStatusWrapperList.forEach {
            if (when (it.visibility) {
                        EnumVisibility.ALWAYS -> true
                        EnumVisibility.DETAIL -> flag.isAdvanced
                        EnumVisibility.NEVER -> false
                    }) {
                tooltip += formattedText { (!it.displayName + !": " + f2(it).white + f(it)).blue }
            }
        }
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (world.isRemote) {
            val actualFairyType = getActualFairyTypeClient(fairyType)
            return ActionResult(getMagicHandler(MagicScope(ApiSkill.skillManager.clientSkillContainer.getSkillLevel(mastery), world, player, itemStack, actualFairyType)).onItemRightClick(hand), itemStack)
        } else {
            val actualFairyType = getActualFairyTypeServer(player, fairyType)
            return ActionResult(getMagicHandler(MagicScope(ApiSkill.skillManager.getServerSkillContainer(player).getSkillLevel(mastery), world, player, itemStack, actualFairyType)).onItemRightClick(hand), itemStack)
        }
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!ApiMain.side().isClient) return // クライアントサイドでなければ中止
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (world.isRemote) {
            val actualFairyType = getActualFairyTypeClient(fairyType)
            getMagicHandler(MagicScope(ApiSkill.skillManager.clientSkillContainer.getSkillLevel(mastery), world, entity, itemStack, actualFairyType)).onUpdate(itemSlot, isSelected)
        }
    }
}
