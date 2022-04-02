package miragefairy2019.api;

import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class PickHandlerRegistry {

    @Nonnull
    public static final NonNullList<IPickHandler> pickHandlers = NonNullList.create();

}
