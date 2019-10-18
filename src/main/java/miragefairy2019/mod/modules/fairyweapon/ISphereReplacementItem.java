package miragefairy2019.mod.modules.fairyweapon;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public interface ISphereReplacementItem
{

	public boolean canRepair(ItemStack itemStack);

	public NonNullList<Ingredient> getRepaitmentSpheres(ItemStack itemStack);

	public ItemStack getRepairedItem(ItemStack itemStack);

}
