package miragefairy2019.mod.modules.materialsfairy;

import static miragefairy2019.mod.lib.Configurator.*;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.ItemMultiMaterialContained;
import miragefairy2019.mod.lib.multi.ItemVariantMaterialContained;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleMaterialsFairy
{

	public static void init(EventRegistryMod erMod)
	{

		// マテリアル
		item(erMod, ItemMultiMaterialContained<ItemVariantMaterialContained>::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_materials"), "materialsFairy")
			.bind(setCreativeTab(ApiMain.creativeTab()))
			.bind(c -> {

				erMod.registerItem.register(b -> {

					// TODO 素材リストにComponentを使用
					c.get().registerVariant(0, new ItemVariantMaterialContained("shine_mana_rod", "manaRodShine", "mirageFairy2019ManaRodShine", "Miragium(0.500), Diamond(0.500)"));
					c.get().registerVariant(1, new ItemVariantMaterialContained("fire_mana_rod", "manaRodFire", "mirageFairy2019ManaRodFire", "Miragium(0.500), Cinnabar(0.500)"));
					c.get().registerVariant(2, new ItemVariantMaterialContained("wind_mana_rod", "manaRodWind", "mirageFairy2019ManaRodWind", "Miragium(0.500), Fluorite(0.500)"));
					c.get().registerVariant(3, new ItemVariantMaterialContained("gaia_mana_rod", "manaRodGaia", "mirageFairy2019ManaRodGaia", "Miragium(0.500), Sulfur(0.500)"));
					c.get().registerVariant(4, new ItemVariantMaterialContained("aqua_mana_rod", "manaRodAqua", "mirageFairy2019ManaRodAqua", "Miragium(0.500), Apatite(0.500)"));
					c.get().registerVariant(5, new ItemVariantMaterialContained("dark_mana_rod", "manaRodDark", "mirageFairy2019ManaRodDark", "Miragium(0.500), Coal(0.500)"));
					ForgeRegistries.ITEMS.register(c.get());
					if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();

				});
				erMod.createItemStack.register(ic -> {

					// 鉱石辞書登録
					for (Tuple<Integer, ItemVariantMaterialContained> tuple : c.get().getVariants()) {
						OreDictionary.registerOre(tuple.y.oreName, tuple.y.createItemStack());
					}

				});

				return Monad.of(c);
			})
			.get();

	}

}
