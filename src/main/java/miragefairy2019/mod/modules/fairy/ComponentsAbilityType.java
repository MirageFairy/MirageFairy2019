package miragefairy2019.mod.modules.fairy;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairy.IComponentAbilityType;
import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.util.ResourceLocation;

public class ComponentsAbilityType
{

	private static Map<IAbilityType, IComponentAbilityType> fairyAbilityTypes = new HashMap<>();

	public static IComponentAbilityType getComponentAbilityType(IAbilityType abilityType)
	{
		return fairyAbilityTypes.computeIfAbsent(abilityType, k -> new ComponentAbilityType(abilityType));
	}

	private static class ComponentAbilityType implements IComponentAbilityType
	{

		private IAbilityType abilityType;

		private ComponentAbilityType(IAbilityType abilityType)
		{
			this.abilityType = abilityType;
		}

		@Override
		public ResourceLocation getName()
		{
			return new ResourceLocation(ModMirageFairy2019.MODID, "cuticle_" + abilityType.getName());
		}

		@Override
		public String getUnlocalizedName()
		{
			return "mirageFairy2019.component.cuticle.format";
		}

		@Override
		public String getLocalizedName()
		{
			return UtilsMinecraft.translateToLocalFormatted(getUnlocalizedName(), abilityType.getLocalizedName());
		}

		@Override
		public IAbilityType getAbilityType()
		{
			return abilityType;
		}

	}

}
