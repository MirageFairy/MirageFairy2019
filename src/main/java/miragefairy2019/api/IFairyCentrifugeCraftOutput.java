package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IFairyCentrifugeCraftOutput {

    /**
     * @return 空でなく、個数が1のアイテムスタック。
     */
    @Nonnull
    public ItemStack getItemStack();

    public double getCount();

    public double getFortuneFactor();

}
