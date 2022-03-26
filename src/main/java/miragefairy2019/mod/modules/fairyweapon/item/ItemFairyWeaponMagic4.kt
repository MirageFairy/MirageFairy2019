package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.blue
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.white
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.formula4.MagicStatusContainer
import miragefairy2019.mod.formula4.displayName
import miragefairy2019.mod.formula4.factors
import miragefairy2019.mod.formula4.getDisplayValue
import miragefairy2019.mod.magic4.Magic
import miragefairy2019.mod.magic4.MagicArguments
import miragefairy2019.mod.magic4.MagicStatus
import miragefairy2019.mod.magic4.getMagicHandler
import miragefairy2019.mod.modules.fairyweapon.findFairy
import miragefairy2019.mod3.artifacts.playerAuraHandler
import miragefairy2019.mod3.artifacts.proxy
import miragefairy2019.mod3.artifacts.skillContainer
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.fairy.api.IFairyType
import miragefairy2019.mod3.fairy.erg
import miragefairy2019.api.Mana
import miragefairy2019.mod3.mana.div
import miragefairy2019.mod3.mana.getMana
import miragefairy2019.mod3.mana.plus
import miragefairy2019.mod3.mana.times
import miragefairy2019.mod3.skill.EnumMastery
import miragefairy2019.mod3.skill.api.IMastery
import miragefairy2019.mod3.skill.getSkillLevel
import mirrg.kotlin.separate
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemFairyWeaponMagic4 : ItemFairyWeapon(), MagicStatusContainer {

    // Magic Status
    override val magicStatusList = mutableListOf<MagicStatus<*>>()

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player ?: return
        magicStatusList.forEach {
            val magicArguments = getMagicArguments(player, itemStackFairyWeapon, fairyType)
            tooltip += formattedText { (!it.displayName + !": " + (!it.getDisplayValue(magicArguments)).white + !" (" + it.factors.separate(!", ") + !")").blue }
        }
    }


    // Magic

    open fun getMagic(): Magic? = null

    fun getMagicArguments(player: EntityPlayer, weaponItemStack: ItemStack, partnerFairyType: IFairyType) = object : MagicArguments {
        override val hasPartnerFairy: Boolean get() = !partnerFairyType.isEmpty
        override fun getRawMana(manaType: Mana): Double {
            val a = partnerFairyType.manaSet / (cost / 50.0) // パートナー妖精のマナ
            val b = a + player.proxy.playerAuraHandler.playerAura // プレイヤーオーラの加算
            val c = b * (1.0 + 0.005 * player.proxy.skillContainer.getSkillLevel(EnumMastery.root)) // スキルレベル補正：妖精マスタリ1につき1%増加
            return c.getMana(manaType)
        }

        override fun getRawErg(ergType: EnumErgType) = partnerFairyType.erg(ergType)
        override val cost get() = partnerFairyType.cost
        override val color get() = partnerFairyType.color
        override fun getSkillLevel(mastery: IMastery) = player.proxy.skillContainer.getSkillLevel(mastery)
        override val weaponItem get() = this@ItemFairyWeaponMagic4
        override val weaponItemStack get() = weaponItemStack
        override val player get() = player
    }

    final override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand) // アイテム取得
        val fairyType = findFairy(itemStack, player)?.second ?: ApiFairy.empty() // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(player, itemStack, fairyType))
        return ActionResult(magicHandler.onItemRightClick(hand), itemStack)
    }

    final override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得
        val fairyType = findFairy(itemStack, entity)?.second ?: ApiFairy.empty() // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(entity, itemStack, fairyType))
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
        val fairyType = findFairy(itemStack, attacker)?.second ?: ApiFairy.empty() // 妖精取得

        val magicHandler = getMagic().getMagicHandler(getMagicArguments(attacker, itemStack, fairyType))
        magicHandler.hitEntity(target)
        return true
    }

}
