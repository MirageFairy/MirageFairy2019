package miragefairy2019.mod.common.fairystick;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRegistry;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraftRegistry implements IFairyStickCraftRegistry
{

	private List<IFairyStickCraftRecipe> recipes = new ArrayList<>();

	@Override
	public void addRecipe(IFairyStickCraftRecipe recipe)
	{
		recipes.add(recipe);
	}

	@Override
	public ISuppliterator<IFairyStickCraftRecipe> getRecipes()
	{
		return ISuppliterator.ofIterable(recipes);
	}

	@Override
	public Optional<IFairyStickCraftExecutor> getExecutor(Optional<EntityPlayer> oPlayer, World world, BlockPos blockPos, ItemStack itemStackFairyStick)
	{

		recipe:
		for (IFairyStickCraftRecipe recipe : recipes) {
			IFairyStickCraftEnvironment environment = new FairyStickCraftEnvironment(oPlayer, world, blockPos, itemStackFairyStick);
			IFairyStickCraftExecutor executor = new FairyStickCraftExecutor();

			for (IFairyStickCraftCondition condition : recipe.getConditions()) {
				if (!condition.test(environment, executor)) continue recipe;
			}

			return Optional.of(executor);
		}

		return Optional.empty();
	}

}
