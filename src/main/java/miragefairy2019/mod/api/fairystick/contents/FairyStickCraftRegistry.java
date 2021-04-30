package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRecipe;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftRegistry;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftResult;
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
	public void registerRecipe(IFairyStickCraftRecipe recipe)
	{
		recipes.add(recipe);
	}

	@Override
	public Optional<IFairyStickCraftResult> getResult(Optional<EntityPlayer> oPlayer, World world, BlockPos pos, ItemStack itemStackFairyStick)
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

}
