package miragefairy2019.mod.modules.ore;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOre<V extends IOreVariant> extends ItemBlock
{

	private BlockOre<V> block2;

	public ItemBlockOre(BlockOre<V> block)
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
