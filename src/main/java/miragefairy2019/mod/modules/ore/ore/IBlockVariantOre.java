package miragefairy2019.mod.modules.ore.ore;

import java.util.Random;

import miragefairy2019.mod.api.ore.IBlockVariantHarvestable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IBlockVariantOre extends IBlockVariantHarvestable
{

	public default void getDrops(NonNullList<ItemStack> drops, Random random, Block block, int metadata, int fortune)
	{
		drops.add(new ItemStack(Item.getItemFromBlock(block), 1, metadata));
	}

	/**
	 * 非シルクタッチでの破壊時に得られる経験値の量です。
	 * 破壊時に鉱石ブロックがそのまま得られる場合、原則として0を返します。
	 * <br>
	 * バニラでの設定は以下の通りです。
	 * <table border="1">
	 * <tr>
	 * <th>種類</th>
	 * <th>量</th>
	 * </tr>
	 * <tr>
	 * <td>coal</td>
	 * <td>[0, 2)</td>
	 * </tr>
	 * <tr>
	 * <td>diamond</td>
	 * <td>[3, 7)</td>
	 * </tr>
	 * <tr>
	 * <td>emerald</td>
	 * <td>[3, 7)</td>
	 * </tr>
	 * <tr>
	 * <td>lapis</td>
	 * <td>[2, 5)</td>
	 * </tr>
	 * <tr>
	 * <td>quartz</td>
	 * <td>[2, 5)</td>
	 * </tr>
	 * </table>
	 */
	public default int getExpDrop(Random random, int fortune)
	{
		return 0;
	}

	public float getHardness();

	public float getResistance();

}
