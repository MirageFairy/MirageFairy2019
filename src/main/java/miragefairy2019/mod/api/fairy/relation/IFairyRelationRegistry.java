package miragefairy2019.mod.api.fairy.relation;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public interface IFairyRelationRegistry
{

	public void registerIngredientFairyRelation(double relevance, ItemStack itemStackFairy, Ingredient ingredient);

	public ISuppliterator<IIngredientFairyRelation> getIngredientFairyRelations();

}
