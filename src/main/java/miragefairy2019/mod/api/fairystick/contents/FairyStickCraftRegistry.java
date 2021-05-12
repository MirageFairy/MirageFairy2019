package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRegistry;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
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
	public Optional<IFairyStickCraftExecutor> getExecutor(Optional<EntityPlayer> oPlayer, World world, BlockPos pos, ItemStack itemStackFairyStick)
	{
		IBlockState blockState = world.getBlockState(pos);

		recipe:
		for (IFairyStickCraftRecipe recipe : recipes) {
			FairyStickCraft fairyStickCraft = new FairyStickCraft(
				oPlayer,
				pos,
				blockState,
				itemStackFairyStick,
				world,
				world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos).grow(1)));

			for (IFairyStickCraftCondition condition : recipe.getConditions()) {
				if (!condition.test(fairyStickCraft)) continue recipe;
			}

			return Optional.of(fairyStickCraft);
		}

		return Optional.empty();
	}

	@Override
	public ISuppliterator<IFairyStickCraftRecipe> getRecipes()
	{
		return ISuppliterator.ofIterable(recipes);
	}

}
