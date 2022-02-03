package miragefairy2019.api;

import net.minecraft.item.ItemStack;

// TODO 構造改善（レシピ側のKotlin化待ち）
public interface IFairyCombiningHandler {

    /**
     * @return このアイテムに何かを合成できるか否か
     */
    @Deprecated
    public boolean canCombine(ItemStack itemStack);

    /**
     * @return このアイテムにitemStackPartを合成できるか否か
     */
    @Deprecated
    public boolean canCombineWith(ItemStack itemStack, ItemStack itemStackPart);

    /**
     * @return アイテムから部品を取り外せるか否か
     */
    @Deprecated
    public boolean canUncombine(ItemStack itemStack);

    /**
     * @return canUncombineである場合、合成されているアイテムがあるなら、そのアイテム。無いなら、Empty。
     * canUncombineでない場合、常にEmpty。
     */
    @Deprecated
    public ItemStack getCombinedPart(ItemStack itemStack);

    /**
     * アイテムを合成します。
     * このメソッドはitemStack引数を改変します。
     * itemStackPartにEmptyが与えられた場合、canUncombineならば、合成されているアイテムを取り外します。
     * そうでない場合、何も行いません。
     */
    @Deprecated
    public void setCombinedPart(ItemStack itemStack, ItemStack itemStackPart);


    // そのアイテムが合成分解レシピの主体になれるかのAPIは処理上必要

    /**
     * 本体にパーツを合成します。
     *
     * @param itemStack     本体となるアイテム。
     * @param itemStackPart パーツとなるアイテム。
     * @return 合成されたアイテム。この組み合わせで合成が不能な場合、{@link ItemStack#EMPTY}。
     */
    //@Nonnull
    //public ItemStack combine(@Nonnull ItemStack itemStack, @Nonnull ItemStack itemStackPart);

    /**
     * 合成されたアイテムを本体とパーツに分割します。
     *
     * @param itemStack 合成されたアイテム。
     * @return 本体およびバーツのアイテム。このアイテムが分解不能な場合、null。
     */
    //@Nullable
    //public Tuple<ItemStack, ItemStack> split(ItemStack itemStack);

}
