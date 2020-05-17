package miragefairy2019.mod.api.fairy;

import java.util.function.Supplier;

import miragefairy2019.mod.modules.fairy.EnumAbilityType;

public class AbilityTypes
{

	public static final Supplier<IAbilityType> attack = () -> EnumAbilityType.attack;
	public static final Supplier<IAbilityType> craft = () -> EnumAbilityType.craft;
	public static final Supplier<IAbilityType> fell = () -> EnumAbilityType.fell;
	public static final Supplier<IAbilityType> light = () -> EnumAbilityType.light;
	public static final Supplier<IAbilityType> flame = () -> EnumAbilityType.flame;
	public static final Supplier<IAbilityType> water = () -> EnumAbilityType.water;
	public static final Supplier<IAbilityType> crystal = () -> EnumAbilityType.crystal;
	public static final Supplier<IAbilityType> art = () -> EnumAbilityType.art;
	public static final Supplier<IAbilityType> store = () -> EnumAbilityType.store;
	public static final Supplier<IAbilityType> warp = () -> EnumAbilityType.warp;
	public static final Supplier<IAbilityType> shoot = () -> EnumAbilityType.shoot;
	public static final Supplier<IAbilityType> breaking = () -> EnumAbilityType.breaking;
	public static final Supplier<IAbilityType> chemical = () -> EnumAbilityType.chemical;
	public static final Supplier<IAbilityType> slash = () -> EnumAbilityType.slash;
	public static final Supplier<IAbilityType> food = () -> EnumAbilityType.food;
	public static final Supplier<IAbilityType> knowledge = () -> EnumAbilityType.knowledge;
	public static final Supplier<IAbilityType> energy = () -> EnumAbilityType.energy;
	public static final Supplier<IAbilityType> submission = () -> EnumAbilityType.submission;
	public static final Supplier<IAbilityType> christmas = () -> EnumAbilityType.christmas;
	public static final Supplier<IAbilityType> freeze = () -> EnumAbilityType.freeze;
	public static final Supplier<IAbilityType> thunder = () -> EnumAbilityType.thunder;

}
