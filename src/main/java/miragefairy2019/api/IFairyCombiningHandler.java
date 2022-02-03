package miragefairy2019.api;

import net.minecraft.item.ItemStack;

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

}
