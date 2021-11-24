package miragefairy2019.mod.api.fairycrystal;

import miragefairy2019.modkt.modules.fairy.RankedFairyTypeBundle;
import net.minecraft.item.ItemStack;

public final class DropFixed implements IDrop {

    public final RankedFairyTypeBundle bundle;
    public final DropCategory dropCategory;
    public final double weight;

    public DropFixed(RankedFairyTypeBundle bundle, DropCategory dropCategory, double weight) {
        this.bundle = bundle;
        this.dropCategory = dropCategory;
        this.weight = weight;
    }

    @Override
    public ItemStack getItemStack(int rank) {
        return bundle.get(rank).createItemStack();
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
