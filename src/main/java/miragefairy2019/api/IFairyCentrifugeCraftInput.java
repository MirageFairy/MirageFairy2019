package miragefairy2019.api;

import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public interface IFairyCentrifugeCraftInput {

    @Nonnull
    public Ingredient getIngredient();

    public int getCount();

}
