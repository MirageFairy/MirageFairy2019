package miragefairy2019.api;

import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public interface IMortarRecipeHandler {

    @Nonnull
    public NonNullList<IMortarRecipe> getRecipes();

}
