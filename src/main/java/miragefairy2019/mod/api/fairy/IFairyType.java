package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.ResourceLocation;

public interface IFairyType
{

	public ResourceLocation getName();

	public int getColor();

	public double getCost();

	public IManaSet getManas();

	public IAbilitySet getAbilities();

	public String getUnlocalizedName();

	public default String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal(getUnlocalizedName());
	}

}
