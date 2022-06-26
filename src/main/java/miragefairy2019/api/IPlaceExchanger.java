package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPlaceExchanger {

    /**
     * ブロック側のアイテムを手元に移動させます。
     */
    public void harvest(@Nonnull ItemStack itemStack);

    /**
     * 手元のアイテムをブロック側に移動させます。
     *
     * @return 空でない、スプリットされた新しいItemStackのインスタンス。
     * 何も持っていない場合はnull。
     */
    @Nullable
    public ItemStack deploy();

}
