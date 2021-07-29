package miragefairy2019.mod.common.fairy.relation;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairy.relation.IFairyRelationRegistry;
import miragefairy2019.mod.api.fairy.relation.IIngredientFairyRelation;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class FairyRelationRegistry implements IFairyRelationRegistry
{

	private List<IIngredientFairyRelation> list = new ArrayList<>();

	@Override
	public void registerIngredientFairyRelation(double relevance, ItemStack itemStackFairy, Ingredient ingredient)
	{
		list.add(new IngredientFairyRelation(itemStackFairy, ingredient, relevance));
	}

	@Override
	public ISuppliterator<IIngredientFairyRelation> getIngredientFairyRelations()
	{
		return ISuppliterator.ofIterable(list);
	}

}
