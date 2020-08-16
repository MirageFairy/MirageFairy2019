package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.api.oreseed.EnumOreSeedShape.*;
import static miragefairy2019.mod.api.oreseed.EnumOreSeedType.*;
import static miragefairy2019.mod.api.oreseed.GenerationConditions.*;

import java.util.Optional;
import java.util.function.Supplier;

import miragefairy2019.mod.api.oreseed.RegistryOreSeedDrop;
import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class LoaderOreSeedDrop
{

	public static void loadOreSeedDrop()
	{
		RegistryOreSeedDrop.register(STONE, LARGE, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.APATITE_ORE));
		RegistryOreSeedDrop.register(STONE, LARGE, 0.08, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SMITHSONITE_ORE), minY(30));
		RegistryOreSeedDrop.register(STONE, PYRAMID, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.FLUORITE_ORE));
		RegistryOreSeedDrop.register(STONE, STAR, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.SULFUR_ORE), maxY(15));
		RegistryOreSeedDrop.register(STONE, POINT, 0.15, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.CINNABAR_ORE), maxY(15));
		RegistryOreSeedDrop.register(STONE, POINT, 0.05, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.PYROPE_ORE), maxY(50));
		RegistryOreSeedDrop.register(STONE, COAL, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MAGNETITE_ORE));
		RegistryOreSeedDrop.register(STONE, TINY, 0.10, () -> ModuleOre.blockOre1.getState(EnumVariantOre1.MOONSTONE_ORE), minY(40), maxY(50));

		if (false) {
			RegistryOreSeedDrop.register((type, shape, world, pos) -> {
				if (type != NETHERRACK) return Optional.empty();
				return Optional.of(new WeightedRandom.Item<Supplier<IBlockState>>(() -> Blocks.OBSIDIAN.getDefaultState(), 1));
			});
			RegistryOreSeedDrop.register((type, shape, world, pos) -> {
				if (type != END_STONE) return Optional.empty();
				return Optional.of(new WeightedRandom.Item<Supplier<IBlockState>>(() -> Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState(), 1));
			});
		}
	}

}
