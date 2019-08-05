package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;

import miragefairy2019.mod.api.fairy.FairyType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLightMagicWand extends ItemFairyWeaponBase
{

	public ItemLightMagicWand()
	{
		setMaxDamage(256 - 1);
	}

	//

	protected static class Status
	{

		public Status(FairyType fairyType)
		{

		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("優しい光が洞窟を照らす");

		// アイテムステータス
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));

		// 素材
		tooltip.add(TextFormatting.YELLOW + "Contains: Apatite(4.000), Miragium(2.000), Sphere of \"LIGHT\"");

		// 機能
		tooltip.add(TextFormatting.RED + "Right click to use magic");

		super.addInformation(itemStack, world, tooltip, flag);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, FairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformationFairyWeapon(itemStackFairyWeapon, itemStackFairy, fairyType, world, tooltip, flag);

		//Status status = new Status(fairyType);
		//tooltip.add(TextFormatting.BLUE + "Additional Attack Damage: " + String.format("%.1f", status.additionalAttackDamage) + " (Shine, Fire, Wind, Gaia, Aqua, Dark)");
		//tooltip.add(TextFormatting.BLUE + "Additional Attack Speed: " + String.format("%.1f", status.additionalAttackSpeed) + " (Cost)");
	}

	//

}
