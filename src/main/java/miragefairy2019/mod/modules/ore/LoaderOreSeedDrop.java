package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.oreseed.EnumOreSeedShape.*;
import static miragefairy2019.mod.api.oreseed.EnumOreSeedType.*;
import static miragefairy2019.mod.api.oreseed.GenerationConditions.*;

import miragefairy2019.mod.api.oreseed.RegistryOreSeedDrop;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;

public class LoaderOreSeedDrop
{

	public static void loadOreSeedDrop()
	{

		RegistryOreSeedDrop.register(STONE, LARGE, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.APATITE_ORE));
		RegistryOreSeedDrop.register(STONE, PYRAMID, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.FLUORITE_ORE));
		RegistryOreSeedDrop.register(STONE, STAR, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SULFUR_ORE), maxY(15));
		RegistryOreSeedDrop.register(STONE, POINT, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.CINNABAR_ORE), maxY(15));
		RegistryOreSeedDrop.register(STONE, COAL, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MAGNETITE_ORE));
		RegistryOreSeedDrop.register(STONE, TINY, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MOONSTONE_ORE), minY(40), maxY(50));

		RegistryOreSeedDrop.register(STONE, POINT, 0.05, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.PYROPE_ORE), maxY(50));
		RegistryOreSeedDrop.register(STONE, LARGE, 0.08, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SMITHSONITE_ORE), minY(30));
		RegistryOreSeedDrop.register(STONE, LAPIS, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NEPHRITE_ORE));

		RegistryOreSeedDrop.register(NETHERRACK, LARGE, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NETHERRACK_APATITE_ORE), minY(90));
		RegistryOreSeedDrop.register(NETHERRACK, PYRAMID, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NETHERRACK_FLUORITE_ORE), minY(90));
		RegistryOreSeedDrop.register(NETHERRACK, TINY, 0.30, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NETHERRACK_SULFUR_ORE), minY(20), maxY(40));
		RegistryOreSeedDrop.register(NETHERRACK, IRON, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NETHERRACK_CINNABAR_ORE));
		RegistryOreSeedDrop.register(NETHERRACK, COAL, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NETHERRACK_MAGNETITE_ORE));
		RegistryOreSeedDrop.register(NETHERRACK, TINY, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.NETHERRACK_MOONSTONE_ORE), maxY(32));

	}

}
