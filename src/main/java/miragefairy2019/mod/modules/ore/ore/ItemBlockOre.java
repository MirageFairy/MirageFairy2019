package miragefairy2019.mod.modules.ore.ore;

import miragefairy2019.mod.lib.multi.ItemBlockMulti;

public class ItemBlockOre<V extends IBlockVariantOre> extends ItemBlockMulti<BlockOre<V>, V>
{

	public ItemBlockOre(BlockOre<V> block)
	{
		super(block);
	}

}
