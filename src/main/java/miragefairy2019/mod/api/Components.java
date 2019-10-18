package miragefairy2019.mod.api;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.lib.component.Component;

public class Components
{

	public static Component APATITE = new Component("apatite");
	public static Component FLUORITE = new Component("fluorite");
	public static Component SULFUR = new Component("sulfur");
	public static Component CINNABAR = new Component("cinnabar");
	public static Component MOONSTONE = new Component("moonstone");
	public static Component MAGNETITE = new Component("magnetite");
	public static Component MIRAGIUM = new Component("miragium");
	public static Component IRON = new Component("iron");
	public static Component GOLD = new Component("gold");
	public static Component WOOD = new Component("wood");
	public static Component STONE = new Component("stone");
	public static Component OBSIDIAN = new Component("obsidian");

	private static Map<EnumAbilityType, Component> fairyAbilityTypes = new HashMap<>();

	public static Component fairyAbilityType(EnumAbilityType abilityType)
	{
		return fairyAbilityTypes.computeIfAbsent(abilityType, k -> new ComponentFairyAbilityType(abilityType));
	}

}
