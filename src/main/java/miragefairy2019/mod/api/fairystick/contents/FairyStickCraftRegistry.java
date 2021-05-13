package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRegistry;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
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
			FairyStickCraft fairyStickCraft = new FairyStickCraft(
				oPlayer,
				blockPos,
				itemStackFairyStick,
				world,
				world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(blockPos).grow(1)));

			for (IFairyStickCraftCondition condition : recipe.getConditions()) {
				if (!condition.test(fairyStickCraft.getEnvironment(), fairyStickCraft.getEventBus())) continue recipe;
			}

			return Optional.of(fairyStickCraft.getExecutor());
		}

		return Optional.empty();
	}

}
