package miragefairy2019.mod.modules.fairyweapon;

import net.minecraft.item.ItemStack;

public interface IFairyCombiningItem
{

	/**
	 * {@code itemStackFairyWeapon}が、いかなる妖精に対しても搭乗に対応しない場合に偽を返します。
	 * 必ず{@link #canCombine(ItemStack, ItemStack)}より前に呼び出されます。
	 */
	public boolean canCombine(ItemStack itemStackFairyWeapon);

	/**
	 * {@code itemStackFairyWeapon}が、{@code itemStackFairy}に対して搭乗に対応していない場合に偽を返します。
	 * このメソッドの前に{@link #canCombine(ItemStack)}が必ず呼び出されます。
	 */
	public boolean canCombine(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy);

	/**
	 * 搭乗後のアイテムを返します。
	 * {@link #canCombine(ItemStack, ItemStack)}が真を返したときのみこのメソッドが呼び出されます。
	 */
	public ItemStack getCombinedItemStack(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy);

}
