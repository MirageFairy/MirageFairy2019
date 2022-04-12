package miragefairy2019.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public interface IFairyCentrifugeCraftRecipe {

    /**
     * @param index 0..2
     */
    @Nullable
    public IFairyCentrifugeCraftProcess getProcess(int index);

    @Nullable
    public NonNullList<ItemStack> craft(@Nonnull Random random, double fortune);

}
