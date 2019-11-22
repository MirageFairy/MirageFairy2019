package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFairyWandCrafting2 extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting2()
	{
		composite = composite
			.add(Components.MIRAGIUM, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.craft));
		setMaxDamage(32 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("腕が4本欲しくなったときにどうぞ");

		super.addInformation(itemStack, world, tooltip, flag);

	}

}
