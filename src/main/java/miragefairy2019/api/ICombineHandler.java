package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICombineHandler {

    /**
     * @param otherItemStack 分解の場合、EMPTY。
     * @return このアイテムとotherItemStackを合成できない場合、null。
     */
    @Nullable
    public ICombineResult combineWith(@Nonnull ItemStack otherItemStack);

}
