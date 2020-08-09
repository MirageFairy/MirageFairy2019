package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.oreseed.EnumOreSeedShape.*;
import static miragefairy2019.mod.api.oreseed.GenerationConditions.*;

import miragefairy2019.mod.api.oreseed.RegisterOreSeedDrop;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;

public class LoaderOreSeedDrop
{

	public static void loadOreSeedDrop()
	{
		RegisterOreSeedDrop.register(LARGE, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.APATITE_ORE));
		RegisterOreSeedDrop.register(LARGE, 0.08, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SMITHSONITE_ORE), minY(30));
		RegisterOreSeedDrop.register(PYRAMID, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.FLUORITE_ORE));
		RegisterOreSeedDrop.register(STAR, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SULFUR_ORE), maxY(15));
		RegisterOreSeedDrop.register(POINT, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.CINNABAR_ORE), maxY(15));
		RegisterOreSeedDrop.register(POINT, 0.05, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.PYROPE_ORE), maxY(50));
		RegisterOreSeedDrop.register(COAL, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MAGNETITE_ORE));
		RegisterOreSeedDrop.register(TINY, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MOONSTONE_ORE), minY(40), maxY(50));
	}

}
