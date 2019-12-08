package miragefairy2019.mod.modules.ore;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMaterials<V extends IBlockVariant> extends ItemBlock
{

	private BlockMaterials<V> block2;

	public ItemBlockMaterials(BlockMaterials<V> block)
	{
		super(block);
		this.block2 = block;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "tile." + block2.variantList.byMetadata(itemStack.getMetadata()).getUnlocalizedName();
	}

}
