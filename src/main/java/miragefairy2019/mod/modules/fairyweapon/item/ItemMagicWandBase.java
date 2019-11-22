package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicWandBase extends ItemFairyWeaponBase
{

	public ItemMagicWandBase()
	{
		composite = composite
			.add(Components.MIRAGIUM, 1)
			.add(Components.FLUORITE, 1)
			.add(Components.MIRAGIUM, 4)
			.add(Components.fairyAbilityType(EnumAbilityType.knowledge));
		setMaxDamage(128 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("風の心 探求");

		super.addInformation(itemStack, world, tooltip, flag);

	}

}
