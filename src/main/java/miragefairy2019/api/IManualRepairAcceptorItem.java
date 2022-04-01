package miragefairy2019.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public interface IManualRepairAcceptorItem {

    public boolean canManualRepair(@Nonnull ItemStack itemStack);

    @Nonnull
    public NonNullList<Ingredient> getManualRepairRequirements(@Nonnull ItemStack itemStack);

    @Nonnull
    public ItemStack getManualRepairedItem(@Nonnull ItemStack itemStack);

}
