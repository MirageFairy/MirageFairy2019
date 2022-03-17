package miragefairy2019.api;

import miragefairy2019.mod3.mana.api.IManaSet;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFoodAuraItem {

    @Nullable
    public IManaSet getFoodAura(@Nonnull ItemStack itemStack);

}
