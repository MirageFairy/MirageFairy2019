package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFairyItem {

    @Nullable
    public IFairyType getMirageFairy(@Nonnull ItemStack itemStack);

}
