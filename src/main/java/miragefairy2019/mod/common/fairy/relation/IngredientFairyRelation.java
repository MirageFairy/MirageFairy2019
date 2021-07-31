package miragefairy2019.mod.common.fairy.relation;

import miragefairy2019.mod.api.fairy.relation.IIngredientFairyRelation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class IngredientFairyRelation implements IIngredientFairyRelation {

    private final ItemStack itemStackFairy;
    private final Ingredient ingredient;
    private final double relevance;

    public IngredientFairyRelation(ItemStack itemStackFairy, Ingredient ingredient, double relevance) {
        this.itemStackFairy = itemStackFairy;
        this.ingredient = ingredient;
        this.relevance = relevance;
    }

    @Override
    public ItemStack getItemStackFairy() {
        return itemStackFairy;
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public double getRelevance() {
        return relevance;
    }

}
