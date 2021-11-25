package miragefairy2019.mod3.fairylogdrop.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public interface IFairyLogDropRegistry {

    public void addRecipe(IFairyLogDropRecipe recipe);

    public Iterable<IFairyLogDropRecipe> getRecipes();

    @Nullable
    public ItemStack drop(World world, BlockPos blockPos, Random random);

}
