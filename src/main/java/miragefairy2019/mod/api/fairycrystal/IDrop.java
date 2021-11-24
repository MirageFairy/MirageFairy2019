package miragefairy2019.mod.api.fairycrystal;

import net.minecraft.item.ItemStack;

public interface IDrop {

    public ItemStack getItemStack();

    public DropCategory getDropCategory();

    public double getWeight();

}
