package miragefairy2019.mod.modules.fairyweapon;

import java.util.Optional;

import com.google.common.collect.Multimap;

import miragefairy2019.mod.api.fairy.FairyType;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFairySword extends ItemFairyWeaponBase
{

	public ItemFairySword()
	{
		setMaxDamage(64);
	}

	public double getAdditionalAttackDamage(ItemStack itemStack)
	{
		return getFairyAttribute("AdditionalAttackDamage", itemStack);
	}

	public double getAdditionalAttackSpeed(ItemStack itemStack)
	{
		return getFairyAttribute("AdditionalAttackSpeed", itemStack);
	}

	public void setAdditionalAttackDamage(ItemStack itemStack, double additionalAttackDamage)
	{
		setFairyAttribute("AdditionalAttackDamage", itemStack, additionalAttackDamage);
	}

	public void setAdditionalAttackSpeed(ItemStack itemStack, double additionalAttackSpeed)
	{
		setFairyAttribute("AdditionalAttackSpeed", itemStack, additionalAttackSpeed);
	}

	//

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack itemStack)
	{
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, itemStack);
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", getAdditionalAttackDamage(itemStack), 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", getAdditionalAttackSpeed(itemStack), 0));
		}
		return multimap;
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (world.getTotalWorldTime() % 20 != 0) return;
		if (!(entity instanceof EntityLivingBase)) return;
		EntityPlayer player = (EntityPlayer) entity;

		Optional<Tuple<ItemStack, FairyType>> oTuple = findFairy(player);
		if (oTuple.isPresent()) {
			double a = oTuple.get().y.manaSet.sum(1, 1, 1, 1, 1, 1);
			double b = oTuple.get().y.cost / 100.0;

			double c = b * b;

			setAdditionalAttackDamage(itemStack, 6 * a / 50.0); // 3~6程度
			setAdditionalAttackSpeed(itemStack, -4 + Math.min(0.25 / c, 8)); // -3.2~-2.4
			// コスト50のときに斧より少し早い程度（-3.0）
			// コスト0のときに+3
		} else {
			setAdditionalAttackDamage(itemStack, 0);
			setAdditionalAttackSpeed(itemStack, 0);
		}
	}

}
