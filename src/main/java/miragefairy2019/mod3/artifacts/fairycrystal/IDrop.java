package miragefairy2019.mod3.artifacts.fairycrystal;

import net.minecraft.item.ItemStack;

public interface IDrop {

    public ItemStack getItemStack(int rank);

    public DropCategory getDropCategory();

    public double getWeight();

}
