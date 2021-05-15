package miragefairy2019.mod.common.fairylogdrop;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropRecipe;
import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropRegistry;
import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class FairyLogDropRegistry implements IFairyLogDropRegistry
{

	private List<IFairyLogDropRecipe> recipes = new ArrayList<>();

	@Override
	public void addRecipe(IFairyLogDropRecipe recipe)
	{
		recipes.add(recipe);
	}

	@Override
	public ISuppliterator<IFairyLogDropRecipe> getRecipes()
	{
		return ISuppliterator.ofIterable(recipes);
	}

	@Override
	public Optional<ItemStack> drop(IBlockAccess blockAccess, BlockPos blockPos, Random random)
	{
		return WeightedRandom.getRandomItem(random, getRecipes()
			.filter(r -> r.getConditions()
				.filter(c -> !c.test(blockAccess, blockPos))
				.isEmpty())
			.map(r -> new WeightedRandom.Item<>(r, r.getRate()))
			.toList())
			.map(r -> r.getItemStackOutput());
	}

}
