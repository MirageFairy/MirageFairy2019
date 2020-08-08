package miragefairy2019.mod.lib.multi;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMulti<B extends BlockMulti<V>, V extends IBlockVariant> extends ItemBlock
{

	protected B block2;

	public ItemBlockMulti(B block)
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
	public String getUnlocalizedName(ItemStack stack)
	{
		return "tile." + block2.variantList.byMetadata(stack.getMetadata()).getUnlocalizedName();
	}

}
