package miragefairy2019.mod.api.fairycrystal;

import net.minecraft.item.ItemStack;

public final class DropFixed implements IDrop {

    public final ItemStack itemStack;
    public final double weight;

    public DropFixed(ItemStack itemStack, double weight) {
        this.itemStack = itemStack;
        this.weight = weight;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public double getWeight() {
        return weight;
    }

}
