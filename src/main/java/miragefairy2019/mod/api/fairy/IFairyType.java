package miragefairy2019.mod.api.fairy;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IFairyType
{

	public ResourceLocation getName();

	public int getColor();

	public double getCost();

	public IManaSet getManas();

	public IAbilitySet getAbilities();

	public ITextComponent getDisplayName();

}
