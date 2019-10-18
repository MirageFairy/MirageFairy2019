package miragefairy2019.mod.api;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.component.Component;
import net.minecraft.util.ResourceLocation;

public class ComponentFairyAbilityType extends Component
{

	public final EnumAbilityType abilityType;

	public ComponentFairyAbilityType(EnumAbilityType abilityType)
	{
		super(new ResourceLocation(ModMirageFairy2019.MODID, "fairyCuticle" + Utils.toUpperCaseHead(abilityType.getName())));
		this.abilityType = abilityType;
	}

	@Override
	public String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocalFormatted("mirageFairy2019.component.fairyCuticle", abilityType.getLocalizedName());
	}

}
