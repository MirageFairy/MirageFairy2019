package miragefairy2019.mod.api.fairystick;

import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairystick.contents.FairyStickCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraftRegistry
{

	private static List<IFairyStickCraftRecipe> recipes;

	public static void registerRecipe(IFairyStickCraftRecipe recipe)
	{
		recipes.add(recipe);
	}

	public static Optional<IFairyStickCraftResult> getResult(Optional<EntityPlayer> oPlayer, World world, BlockPos pos, ItemStack itemStackFairyStick)
	{
		IBlockState blockState = world.getBlockState(pos);

		recipe:
		for (IFairyStickCraftRecipe recipe : recipes) {
			FairyStickCraft fairyStickCraft = new FairyStickCraft(oPlayer, pos, blockState, itemStackFairyStick, world);

			for (IFairyStickCraftCondition condition : recipe.getConditions()) {
				if (!condition.test(fairyStickCraft)) continue recipe;
			}

			return Optional.of(fairyStickCraft);
		}

		return Optional.empty();
	}

}
