package miragefairy2019.mod.modules.mirageflower.fairylogdrop.api;

import java.util.Optional;
import java.util.Random;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFairyLogDropRegistry
{

	public void addRecipe(IFairyLogDropRecipe recipe);

	public ISuppliterator<IFairyLogDropRecipe> getRecipes();

	public Optional<ItemStack> drop(World world, BlockPos blockPos, Random random);

}
