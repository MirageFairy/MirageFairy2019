package miragefairy2019.mod.api.fairy.relation;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public interface IIngredientFairyRelation
{

	public ItemStack getItemStackFairy();

	public Ingredient getIngredient();

	public double getRelevance();

}
