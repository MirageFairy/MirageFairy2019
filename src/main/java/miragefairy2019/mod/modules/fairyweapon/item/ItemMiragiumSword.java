package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMiragiumSword extends ItemFairyWeaponBase
{

	public ItemMiragiumSword()
	{
		composite = composite
			.add(Components.MIRAGIUM, 2)
			.add(Components.WOOD, 0.5)
			.add(Components.fairyAbilityType(EnumAbilityType.attack))
			.add(Components.fairyAbilityType(EnumAbilityType.slash));
		setMaxDamage(64 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("切断の力を何に使うかはあなた次第");

		super.addInformation(itemStack, world, tooltip, flag);

	}

}
