package miragefairy2019.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFairyCentrifugeCraftHandler {

    public double getNorma();

    @Nonnull
    public NonNullList<IFairyCentrifugeCraftInput> getInputs();

    @Nonnull
    public NonNullList<IFairyCentrifugeCraftOutput> getOutputs();

    @Nullable
    public IFairyCentrifugeCraftRecipe test(@Nonnull IInventory inventory);

}
