package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface ICombineResult {

    @Nonnull
    public ItemStack getCombinedItem();

    @Nonnull
    public ItemStack getRemainingItem();

}
