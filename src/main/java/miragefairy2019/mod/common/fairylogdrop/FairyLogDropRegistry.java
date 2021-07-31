package miragefairy2019.mod.common.fairylogdrop;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropRecipe;
import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropRegistry;
import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FairyLogDropRegistry implements IFairyLogDropRegistry {

    private List<IFairyLogDropRecipe> recipes = new ArrayList<>();

    @Override
    public void addRecipe(IFairyLogDropRecipe recipe) {
        recipes.add(recipe);
    }

    @Override
    public ISuppliterator<IFairyLogDropRecipe> getRecipes() {
        return ISuppliterator.ofIterable(recipes);
    }

    @Override
    public Optional<ItemStack> drop(World world, BlockPos blockPos, Random random) {
        return WeightedRandom.getRandomItem(random, getRecipes()
                .filter(r -> r.getConditions()
                        .filter(c -> !c.test(world, blockPos))
                        .isEmpty())
                .map(r -> new WeightedRandom.Item<>(r, r.getRate()))
                .toList())
                .map(r -> r.getItemStackOutput());
    }

}
