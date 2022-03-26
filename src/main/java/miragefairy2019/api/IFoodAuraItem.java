package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFoodAuraItem {

    @Nullable
    public ManaSet getFoodAura(@Nonnull ItemStack itemStack);

}
