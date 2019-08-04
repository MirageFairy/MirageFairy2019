package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingFairyWand extends ItemFairyCraftingToolBase
{

	public ItemCraftingFairyWand()
	{
		this.setMaxDamage(16 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add("スフィアから聞こえる、妖精の声");
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));
		tooltip.add(TextFormatting.YELLOW + "Contains: Wood(2.000), Sphere of \"CRAFT\"");
	}

}
