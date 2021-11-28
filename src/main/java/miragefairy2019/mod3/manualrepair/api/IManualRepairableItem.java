package miragefairy2019.mod3.manualrepair.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public interface IManualRepairableItem {

    public boolean canManualRepair(ItemStack itemStack);

    public NonNullList<Ingredient> getManualRepairSubstitute(ItemStack itemStack);

    public ItemStack getManualRepairedItem(ItemStack itemStack);

}
