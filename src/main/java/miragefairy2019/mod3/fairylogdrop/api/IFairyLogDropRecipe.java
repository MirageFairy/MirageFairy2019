package miragefairy2019.mod3.fairylogdrop.api;

import net.minecraft.item.ItemStack;

public interface IFairyLogDropRecipe {

    public double getRate();

    public ItemStack getItemStackOutput();

    public Iterable<IFairyLogDropCondition> getConditions();

}
