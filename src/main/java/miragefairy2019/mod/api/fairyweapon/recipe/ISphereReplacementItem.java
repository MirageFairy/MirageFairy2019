package miragefairy2019.mod.api.fairyweapon.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public interface ISphereReplacementItem {

    public boolean canSphereReplace(ItemStack itemStack);

    public NonNullList<Ingredient> getRepairmentSpheres(ItemStack itemStack);

    public ItemStack getSphereReplacedItem(ItemStack itemStack);

}
