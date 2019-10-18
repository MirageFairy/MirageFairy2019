package miragefairy2019.mod.lib.component;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.ResourceLocation;

public class Component implements Comparable<Component>
{

	public final ResourceLocation name;

	public Component(ResourceLocation name)
	{
		this.name = name;
	}

	public Component(String name)
	{
		this(new ResourceLocation(name));
	}

	public String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal("mirageFairy2019.component." + name.getResourcePath() + ".name");
	}

	@Override
	public int compareTo(Component other)
	{
		return name.compareTo(other.name);
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + name + "]";
	}

}
