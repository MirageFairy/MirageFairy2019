package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyCraftingToolBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingFairyWand3 extends ItemFairyCraftingToolBase
{

	public ItemCraftingFairyWand3()
	{
		super(Composite.empty()
			.add(Components.MIRAGIUM, 1)
			.add(Components.MAGNETITE, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.craft), 1));
		this.setMaxDamage(64 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("スフィアから聞こえる、妖精の声");

		super.addInformation(itemStack, world, tooltip, flag);

	}

}
