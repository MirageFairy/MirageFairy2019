package miragefairy2019.mod.modules.ore.material;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMaterials<V extends IBlockVariantMaterials> extends ItemBlock
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

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		return block2.variantList.byMetadata(itemStack.getMetadata()).getBurnTime();
	}

}
