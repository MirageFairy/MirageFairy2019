package miragefairy2019.mod.api.fairylogdrop;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public interface IFairyLogDropRegistry {

    public void addRecipe(IFairyLogDropRecipe recipe);

    public ISuppliterator<IFairyLogDropRecipe> getRecipes();

    public Optional<ItemStack> drop(World world, BlockPos blockPos, Random random);

}
