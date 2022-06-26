package miragefairy2019.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public interface IMortarRecipe {

    @Nonnull
    public Ingredient getInput();

    @Nonnull
    public ItemStack getOutput();

}
