package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAuraItem {

    @Nullable
    public ManaSet getAuraManaSet(@Nonnull ItemStack itemStack);

    @Nullable
    public ErgSet getAuraErgSet(@Nonnull ItemStack itemStack);

}
