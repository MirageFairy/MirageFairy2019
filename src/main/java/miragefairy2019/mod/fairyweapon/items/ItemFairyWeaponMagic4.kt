package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.erg
import miragefairy2019.lib.get
import miragefairy2019.lib.playerAuraHandler
import miragefairy2019.lib.plus
import miragefairy2019.lib.proxy
import miragefairy2019.lib.times
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.isNotEmpty
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.white
import miragefairy2019.mod.fairyweapon.findFairy
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.IMagicStatusContainer
import miragefairy2019.mod.fairyweapon.magic4.Magic
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicStatus
import miragefairy2019.mod.fairyweapon.magic4.displayName
import miragefairy2019.mod.fairyweapon.magic4.factors
import miragefairy2019.mod.fairyweapon.magic4.getDisplayValue
import miragefairy2019.mod.fairyweapon.magic4.getMagicHandler
import miragefairy2019.mod.skill.IMastery
import miragefairy2019.mod.skill.Mastery
import miragefairy2019.mod.skill.getSkillLevel
import miragefairy2019.mod.skill.skillContainer
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

open class ItemFairyWeaponMagic4 : ItemFairyWeapon(), IMagicStatusContainer {

    // Magic Status

    override val magicStatusList = mutableListOf<MagicStatus<*>>()

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairySpec: IFairySpec, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player ?: return
        magicStatusList.forEach { magicStatus ->
            val show = when (magicStatus.visibility) {
                EnumVisibility.ALWAYS -> true
                EnumVisibility.DETAIL -> flag.isAdvanced
                EnumVisibility.NEVER -> false
            }
            if (show) {
                val magicArguments = getMagicArguments(player, itemStackFairyWeapon, fairySpec)
                tooltip += formattedText {
                    val factors = magicStatus.factors.map { it() }.sandwich { ", "() }.flatten()
                    val tail = if (factors.isNotEmpty) " ("() + factors + ")"() else empty
                    (magicStatus.displayName() + ": "() + magicStatus.getDisplayValue(magicArguments)().white + tail).blue
                }
            }
        }
    }

    open fun getCoolTimeCategories(): List<String> = listOf()


    // Magic

    open fun getMagic(): Magic? = null

    private fun getMagicArguments(player: EntityPlayer, weaponItemStack: ItemStack, partnerFairySpec: IFairySpec) = object : MagicArguments {
        override val hasPartnerFairy get() = !partnerFairySpec.isEmpty
        override fun getMana(mana: Mana): Double {
            val a = partnerFairySpec.manaSet // パートナー妖精のマナ
            val b = a + player.proxy.playerAuraHandler.playerAura // プレイヤーオーラの加算
            val c = b * (1.0 + 0.005 * player.proxy.skillContainer.getSkillLevel(Mastery.root)) // スキルレベル補正：妖精マスタリ1につき1%増加
            return c[mana]
        }

        override fun getErg(erg: Erg) = partnerFairySpec.erg(erg)
        override val cost get() = partnerFairySpec.cost
        override val color get() = partnerFairySpec.color
        override fun getSkillLevel(mastery: IMastery) = player.proxy.skillContainer.getSkillLevel(mastery)
        override val weaponItem get() = this@ItemFairyWeaponMagic4
        override val weaponItemStack get() = weaponItemStack
        override val player get() = player
    }

    final override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairySpec = findFairy(itemStack, player)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(player, itemStack, fairySpec))
        return ActionResult(magicHandler.onItemRightClick(hand), itemStack)
    }

    override fun onUsingTick(itemStack: ItemStack, entityLivingBase: EntityLivingBase, count: Int) {
        if (entityLivingBase !is EntityPlayer) return // プレイヤー取得
        if (entityLivingBase.getHeldItem(entityLivingBase.activeHand) != itemStack) return // アイテム取得
        val fairySpec = findFairy(itemStack, entityLivingBase)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(entityLivingBase, itemStack, fairySpec))
        magicHandler.onUsingTick(count)
    }

    final override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairySpec = findFairy(itemStack, entity)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(entity, itemStack, fairySpec))
        magicHandler.onUpdate(itemSlot, isSelected)
        if (world.isRemote) {
            magicHandler.onClientUpdate(itemSlot, isSelected)
        } else {
            magicHandler.onServerUpdate(itemSlot, isSelected)
        }
    }

    final override fun hitEntity(itemStack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        super.hitEntity(itemStack, target, attacker)
        if (attacker !is EntityPlayer) return true // プレイヤー取得
        val fairySpec = findFairy(itemStack, attacker)?.second ?: EMPTY_FAIRY // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(attacker, itemStack, fairySpec))
        magicHandler.onHit(target)
        if (target.health <= 0) magicHandler.onKill(target)
        return true
    }

}
