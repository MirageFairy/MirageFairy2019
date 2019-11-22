package miragefairy2019.mod.modules.fairyweapon.recipe;

import net.minecraft.item.ItemStack;

public interface ICombiningItem
{

	/**
	 * @return このアイテムに何かを合成できるか否か
	 */
	public boolean canCombine(ItemStack itemStack);

	/**
	 * @return このアイテムにitemStackPartを合成できるか否か
	 */
	public boolean canCombineWith(ItemStack itemStack, ItemStack itemStackPart);

	/**
	 * @return アイテムから部品を取り外せるか否か
	 */
	public boolean canUncombine(ItemStack itemStack);

	/**
	 * @return canUncombineである場合、合成されているアイテムがあるなら、そのアイテム。無いなら、Empty。
	 *         canUncombineでない場合、常にEmpty。
	 */
	public ItemStack getCombinedPart(ItemStack itemStack);

	/**
	 * アイテムを合成します。
	 * このメソッドはitemStack引数を改変します。
	 * itemStackPartにEmptyが与えられた場合、canUncombineならば、合成されているアイテムを取り外します。
	 * そうでない場合、何も行いません。
	 */
	public void setCombinedPart(ItemStack itemStack, ItemStack itemStackPart);

}
