package miragefairy2019.mod.api.fairy;

import java.util.function.Supplier;

public class ManaTypes
{

	public static final Supplier<IManaType> shine = () -> miragefairy2019.mod.modules.fairy.EnumManaType.shine;
	public static final Supplier<IManaType> fire = () -> miragefairy2019.mod.modules.fairy.EnumManaType.fire;
	public static final Supplier<IManaType> wind = () -> miragefairy2019.mod.modules.fairy.EnumManaType.wind;
	public static final Supplier<IManaType> gaia = () -> miragefairy2019.mod.modules.fairy.EnumManaType.gaia;
	public static final Supplier<IManaType> aqua = () -> miragefairy2019.mod.modules.fairy.EnumManaType.aqua;
	public static final Supplier<IManaType> dark = () -> miragefairy2019.mod.modules.fairy.EnumManaType.dark;

}
