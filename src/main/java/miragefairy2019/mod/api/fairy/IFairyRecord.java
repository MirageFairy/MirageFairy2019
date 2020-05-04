package miragefairy2019.mod.api.fairy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IFairyRecord
{

	public ResourceLocation getRegistryName();

	public IFairyType getFairyType();

	public ItemStack getItemStack();

}
