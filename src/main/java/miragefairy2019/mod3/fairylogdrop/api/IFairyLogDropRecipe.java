package miragefairy2019.mod3.fairylogdrop.api;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.item.ItemStack;

public interface IFairyLogDropRecipe {

    public double getRate();

    public ItemStack getItemStackOutput();

    public ISuppliterator<IFairyLogDropCondition> getConditions();

}