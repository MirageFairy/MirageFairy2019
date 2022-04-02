package miragefairy2019.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public interface IFairyLogDropRecipe {

    @Nonnull
    public NonNullList<IFairyLogDropRequirement> getRequirements();

    @Nonnull
    public ItemStack getOutput();

    public double getRate();

}
