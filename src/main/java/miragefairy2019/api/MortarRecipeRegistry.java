package miragefairy2019.api;

import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class MortarRecipeRegistry {

    @Nonnull
    public static final NonNullList<IMortarRecipeHandler> mortarRecipeHandlers = NonNullList.create();

}
