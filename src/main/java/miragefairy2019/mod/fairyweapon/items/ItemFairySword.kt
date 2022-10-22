package miragefairy2019.mod.fairyweapon.items

import com.google.common.collect.Multimap
import miragefairy2019.api.IFairySpec
import miragefairy2019.lib.double
import miragefairy2019.lib.get
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.setDouble
import miragefairy2019.lib.sum
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.plus
import miragefairy2019.mod.fairyweapon.findFairy
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.min

// TODO magic4
class ItemFairySword : ItemFairyWeapon() {
    private fun getFairyAttribute(attributeName: String, itemStack: ItemStack) = itemStack.nbtProvider["Fairy"][attributeName].double ?: 0.0
    private fun setFairyAttribute(attributeName: String, itemStack: ItemStack, value: Double) = itemStack.nbtProvider["Fairy"][attributeName].setDouble(value)
    private fun getAdditionalAttackDamage(itemStack: ItemStack) = getFairyAttribute("AdditionalAttackDamage", itemStack)
    private fun getAdditionalAttackSpeed(itemStack: ItemStack) = getFairyAttribute("AdditionalAttackSpeed", itemStack)
    private fun setAdditionalAttackDamage(itemStack: ItemStack, additionalAttackDamage: Double) = setFairyAttribute("AdditionalAttackDamage", itemStack, additionalAttackDamage)
    private fun setAdditionalAttackSpeed(itemStack: ItemStack, additionalAttackSpeed: Double) = setFairyAttribute("AdditionalAttackSpeed", itemStack, additionalAttackSpeed)

    private class Status(fairySpec: IFairySpec) {
        val additionalAttackDamage = 6.0 * fairySpec.manaSet.sum(1.0, 1.0, 1.0, 1.0, 1.0, 1.0) * fairySpec.cost / 50.0 / 50.0 // 3~6程度
        val additionalAttackSpeed = run {
            val a = fairySpec.cost / 100.0
            -4 + min(0.25 / (a * a), 8.0) // -3.2~-2.4
            // コスト50のときに斧より少し早い程度（-3.0）
            // コスト0のときに+3
        }
    }

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairySpec: IFairySpec, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {
        val status = Status(fairySpec)
        tooltip += formattedText { ("Additional Attack Damage: "() + format("%.1f", status.additionalAttackDamage) + " (Shine, Fire, Wind, Gaia, Aqua, Dark)"()).blue }
        tooltip += formattedText { ("Additional Attack Speed: "() + format("%.1f", status.additionalAttackSpeed) + " (Cost)"()).blue }
    }


    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (world.totalWorldTime % 20L != 0L) return // 1秒ごとに更新
        if (entity !is EntityPlayer) return // プレイヤーのみ有効

        val fairy = findFairy(itemStack, entity)
        if (fairy != null) {
            val status = Status(fairy.second)
            setAdditionalAttackDamage(itemStack, status.additionalAttackDamage)
            setAdditionalAttackSpeed(itemStack, status.additionalAttackSpeed)
        } else {
            setAdditionalAttackDamage(itemStack, 0.0)
            setAdditionalAttackSpeed(itemStack, 0.0)
        }
    }

    override fun getAttributeModifiers(equipmentSlot: EntityEquipmentSlot, itemStack: ItemStack): Multimap<String, AttributeModifier> = super.getAttributeModifiers(equipmentSlot, itemStack).also { multimap ->
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", getAdditionalAttackDamage(itemStack), 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", getAdditionalAttackSpeed(itemStack), 0))
        }
    }
}
