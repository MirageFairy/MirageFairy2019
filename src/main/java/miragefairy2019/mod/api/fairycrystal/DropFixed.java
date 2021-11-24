package miragefairy2019.mod.api.fairycrystal;

import net.minecraft.item.ItemStack;

public final class DropFixed implements IDrop {

    public final ItemStack itemStack;
    public final DropCategory dropCategory;
    public final double weight;

    public DropFixed(ItemStack itemStack, DropCategory dropCategory, double weight) {
        this.itemStack = itemStack;
        this.dropCategory = dropCategory;
        this.weight = weight;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public DropCategory getDropCategory() {
        return dropCategory;
    }

    @Override
    public double getWeight() {
        return weight;
    }

}
