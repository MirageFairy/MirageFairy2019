package miragefairy2019.api;

import net.minecraft.inventory.IInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFairyCentrifugeCraftHandler {

    @Nullable
    public IFairyCentrifugeCraftRecipe test(@Nonnull IInventory inventory);

}
