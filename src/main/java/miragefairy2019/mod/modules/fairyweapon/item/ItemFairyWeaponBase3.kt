package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.api.IMagicStatus
import miragefairy2019.mod3.magic.api.IMagicStatusFormatter
import miragefairy2019.mod3.magic.api.IMagicStatusFunction
import miragefairy2019.mod3.skill.api.ApiSkill
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.getSkillLevel
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.api.playeraura.ApiPlayerAura
import miragefairy2019.modkt.impl.magicstatus.MagicStatus
import miragefairy2019.modkt.impl.magicstatus.displayName
import miragefairy2019.modkt.impl.magicstatus.factors
import miragefairy2019.modkt.impl.magicstatus.getActualFairyType
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting.BLUE
import net.minecraft.util.text.TextFormatting.WHITE
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

abstract class ItemFairyWeaponBase3(
        val mastery: IMastery
) : ItemFairyWeaponBase() {
    companion object {
        private const val prefix = "miragefairy2019.gui.magic"

        class MagicScope(val skillLevel: Int, val world: World, val player: EntityPlayer, val itemStack: ItemStack, val fairyType: IFairyType) {
            operator fun <T> IMagicStatus<T>.not(): T = function.getValue(fairyType)
        }

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
    }

    // Magic

    private var magicProvider: (MagicScope.() -> IMagicHandler)? = null
    internal fun magic(magicProvider: MagicScope.() -> IMagicHandler) {
        if (this.magicProvider != null) throw Exception("Multiple magic definition")
        this.magicProvider = magicProvider
    }

    private fun getMagicHandler(magicScope: MagicScope) = magicProvider?.invoke(magicScope) ?: object : IMagicHandler {}

    // Magic Status

    private val magicStatusList = mutableListOf<IMagicStatus<*>>()
    internal fun <T> register(magicStatus: IMagicStatus<T>): IMagicStatus<T> = magicStatus.also { magicStatusList += it }

    internal operator fun <T> String.invoke(function: IFairyType.() -> T, fFormatter: MagicStatusFormatterScope<T>.() -> IMagicStatusFormatter<T>): MagicStatus<T> {
        return MagicStatus(this, IMagicStatusFunction<T> { it.function() }, MagicStatusFormatterScope<T>().fFormatter())
    }

    // Overrides

    @SideOnly(Side.CLIENT)
    override fun addInformationFunctions(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { translate("$prefix.message.rightClick").red }
        super.addInformationFunctions(itemStack, world, tooltip, flag)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val actualFairyType = getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler.playerAura)
        fun <T> getStatusText(magicStatus: IMagicStatus<T>) = buildText {
            text(magicStatus.displayName)
            text(": ")
            text {
                text(magicStatus.formatter.getDisplayValue(magicStatus.function, actualFairyType))
            }.color(WHITE)
            if (flag.isAdvanced) {
                val list = magicStatus.function.factors.map { listOf(it) }.stream().reduce { a, b -> listOf(a, listOf(buildText { text(", ") }), b).flatten() }.orElse(null)
                if (list != null) {
                    text(" (")
                    list.forEach { text(it) }
                    text(")")
                }
            }
        }.color(BLUE)
        magicStatusList.forEach { tooltip.add(getStatusText(it).formattedText) }
    }

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (world.isRemote) {
            val actualFairyType = getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler.playerAura)
            return ActionResult(getMagicHandler(MagicScope(ApiSkill.skillManager.clientSkillContainer.getSkillLevel(mastery), world, player, itemStack, actualFairyType)).onItemRightClick(hand), itemStack)
        } else {
            val actualFairyType = getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player as EntityPlayerMP).playerAura)
            return ActionResult(getMagicHandler(MagicScope(ApiSkill.skillManager.getServerSkillContainer(player).getSkillLevel(mastery), world, player, itemStack, actualFairyType)).onItemRightClick(hand), itemStack)
        }
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!ApiMain.side().isClient) return // クライアントサイドでなければ中止
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity).orElse(null)?.let { it.y!! } ?: ApiFairy.empty() // 妖精取得
        if (world.isRemote) {
            val actualFairyType = getActualFairyType(fairyType, ApiPlayerAura.playerAuraManager.clientPlayerAuraHandler.playerAura)
            getMagicHandler(MagicScope(ApiSkill.skillManager.clientSkillContainer.getSkillLevel(mastery), world, entity, itemStack, actualFairyType)).onUpdate(itemSlot, isSelected)
        }
    }
}
