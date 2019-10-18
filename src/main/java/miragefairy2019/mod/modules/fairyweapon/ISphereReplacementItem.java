package miragefairy2019.mod.modules.fairyweapon;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ISphereReplacementItem
{

	public boolean canRepair(ItemStack itemStack);

	public NonNullList<Predicate<ItemStack>> getRepaitmentSpheres(ItemStack itemStack);

	public ItemStack getRepairedItem(ItemStack itemStack);

}
