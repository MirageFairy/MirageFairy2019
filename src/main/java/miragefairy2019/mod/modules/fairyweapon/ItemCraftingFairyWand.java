package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingFairyWand extends ItemFairyWeaponBase
{

	public ItemCraftingFairyWand()
	{
		this.setMaxDamage(16 - 1);
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack)
	{
		return !(itemStack.getItemDamage() >= itemStack.getMaxDamage());
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		itemStack = itemStack.copy();
		if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) return ItemStack.EMPTY;
		itemStack.setItemDamage(itemStack.getItemDamage() + 1);
		return itemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add("スフィアから聞こえる、妖精の音");
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));
		tooltip.add(TextFormatting.YELLOW + "Contains: Wood(2.000), Sphere of \"CRAFT\"");
	}

}
