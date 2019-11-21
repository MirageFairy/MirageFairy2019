package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;

import com.google.common.collect.Multimap;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFairySword extends ItemMiragiumSword
{

	public ItemFairySword()
	{
		composite = composite
			.add(Components.APATITE, 1)
			.add(Components.FLUORITE, 1)
			.add(Components.SULFUR, 1)
			.add(Components.CINNABAR, 1)
			.add(Components.MAGNETITE, 1)
			.add(Components.MOONSTONE, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.attack));
		setMaxDamage(64 - 1);
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

	protected static class Status
	{

		public final double additionalAttackDamage;
		public final double additionalAttackSpeed;

		public Status(FairyType fairyType)
		{

			additionalAttackDamage = 6 * fairyType.manaSet.sum(1, 1, 1, 1, 1, 1) / 50.0; // 3~6程度

			double a = fairyType.cost / 100.0;
			additionalAttackSpeed = -4 + Math.min(0.25 / (a * a), 8); // -3.2~-2.4
			// コスト50のときに斧より少し早い程度（-3.0）
			// コスト0のときに+3

		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("デザインコンテスト武器");
		tooltip.add(TextFormatting.LIGHT_PURPLE + "Author: たぬん三世");

		super.addInformation(itemStack, world, tooltip, flag);

	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		tooltip.add(TextFormatting.RED + "Great damage when attacking");

		super.addInformationFunctions(itemStack, world, tooltip, flag);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, FairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformationFairyWeapon(itemStackFairyWeapon, itemStackFairy, fairyType, world, tooltip, flag);

		Status status = new Status(fairyType);
		tooltip.add(TextFormatting.BLUE + "Additional Attack Damage: " + String.format("%.1f", status.additionalAttackDamage) + " (Shine, Fire, Wind, Gaia, Aqua, Dark)");
		tooltip.add(TextFormatting.BLUE + "Additional Attack Speed: " + String.format("%.1f", status.additionalAttackSpeed) + " (Cost)");
	}

	//

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (world.getTotalWorldTime() % 20 != 0) return;
		if (!(entity instanceof EntityLivingBase)) return;
		EntityPlayer player = (EntityPlayer) entity;

		Tuple<ItemStack, FairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy != null) {
			Status status = new Status(fairy.y);
			setAdditionalAttackDamage(itemStack, status.additionalAttackDamage);
			setAdditionalAttackSpeed(itemStack, status.additionalAttackSpeed);
		} else {
			setAdditionalAttackDamage(itemStack, 0);
			setAdditionalAttackSpeed(itemStack, 0);
		}
	}

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

}
