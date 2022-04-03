package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 別のアイテムと合成、もしくは単体でクラフトして分離を行うアイテムを表します。
 */
public interface ICombineAcceptorItem {

    /**
     * @return このアイテムが合成・分解をサポートしない場合、null。
     */
    @Nullable
    public ICombineHandler getCombineHandler(@Nonnull ItemStack itemStack);

}
