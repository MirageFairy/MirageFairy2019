package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IPlaceExchanger {

    /**
     * ブロック側のアイテムを手元に移動させます。
     */
    public void harvest(@Nonnull ItemStack itemStack);

    /**
     * 手元のアイテムをブロック側に移動させます。
     *
     * @return スプリットされた新しいItemStackのインスタンス。
     * ただし何も持っていない場合はItemStack.EMPTYをcopyして返す。
     */
    @Nonnull
    public ItemStack deploy();

}
