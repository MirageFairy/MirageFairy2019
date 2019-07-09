package miragefairy2019.mod.lib.multi;

import net.minecraft.item.ItemStack;

public abstract class ItemVariant
{

	private int metadata = 0;

	public int getMetadata()
	{
		return metadata;
	}

	public void setMetadata(int metadata)
	{
		this.metadata = metadata;
	}

	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item." + getOreName(itemStack);
	}

	public abstract String getRegistryName(ItemStack itemStack);

	public abstract String getOreName(ItemStack itemStack);

}
