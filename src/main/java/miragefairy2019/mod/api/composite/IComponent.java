package miragefairy2019.mod.api.composite;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.ResourceLocation;

public interface IComponent extends Comparable<IComponent>
{

	public ResourceLocation getName();

	public String getUnlocalizedName();

	public default String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal(getUnlocalizedName());
	}

	@Override
	public default int compareTo(IComponent other)
	{
		return getName().compareTo(other.getName());
	}

}
