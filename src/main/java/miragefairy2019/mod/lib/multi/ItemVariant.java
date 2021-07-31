package miragefairy2019.mod.lib.multi;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemVariant {

    private int metadata = 0;
    private Item item = null;

    public int getMetadata() {
        return metadata;
    }

    public void setMetadata(int metadata) {
        this.metadata = metadata;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ItemStack createItemStack(int amount) {
        return new ItemStack(item, amount, metadata);
    }

    public ItemStack createItemStack() {
        return createItemStack(1);
    }

}
