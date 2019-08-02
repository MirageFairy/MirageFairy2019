package miragefairy2019.mod.modules.sphere;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.fairy.IAbilityType;

public enum EnumSphere
{
	attack(EnumAbilityType.attack, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000),
	craft(EnumAbilityType.craft, 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC),
	fell(EnumAbilityType.fell, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F),
	light(EnumAbilityType.light, 0xFFFFFF, 0x848484, 0x000000, 0xFFFFFF),
	flame(EnumAbilityType.flame, 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000),
	water(EnumAbilityType.water, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF),
	;

	public final IAbilityType abilityType;
	public final int colorCore;
	public final int colorHighlight;
	public final int colorBackground;
	public final int colorPlasma;

	private EnumSphere(IAbilityType abilityType, int colorCore, int colorHighlight, int colorBackground, int colorPlasma)
	{
		this.abilityType = abilityType;
		this.colorCore = colorCore;
		this.colorHighlight = colorHighlight;
		this.colorBackground = colorBackground;
		this.colorPlasma = colorPlasma;
	}

}
