package miragefairy2019.mod.modules.materialsfairy;

import static miragefairy2019.mod.api.composite.ApiComposite.*;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.lib.Configurator.*;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ModuleMaterialsFairy
{

	public static void init(EventRegistryMod erMod)
	{

		// 妖精のステッキ

		item(erMod, ItemFairyStick::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_stick"), "fairyStick")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(onRegisterItem(i -> {
				if (ApiMain.side().isClient()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), null));
			}));

		// マテリアル
		item(erMod, ItemMultiMaterial<ItemVariantMaterial>::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_materials"), "materialsFairy")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(c -> {

				itemVariant(c.erMod, c, 0, () -> new ItemVariantMaterial("shine_mana_rod", "manaRodShine"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(moonstone.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodShine"));

				itemVariant(c.erMod, c, 1, () -> new ItemVariantMaterial("fire_mana_rod", "manaRodFire"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(cinnabar.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodFire"));

				itemVariant(c.erMod, c, 2, () -> new ItemVariantMaterial("wind_mana_rod", "manaRodWind"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(fluorite.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodWind"));

				itemVariant(c.erMod, c, 3, () -> new ItemVariantMaterial("gaia_mana_rod", "manaRodGaia"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(sulfur.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodGaia"));

				itemVariant(c.erMod, c, 4, () -> new ItemVariantMaterial("aqua_mana_rod", "manaRodAqua"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(apatite.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodAqua"));

				itemVariant(c.erMod, c, 5, () -> new ItemVariantMaterial("dark_mana_rod", "manaRodDark"))
					.bind(addComponent(instance(miragium.get(), 0.5)))
					.bind(addComponent(instance(magnetite.get(), 0.5)))
					.bind(addOreName("mirageFairy2019ManaRodDark"));

				erMod.registerItem.register(b -> {
					if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
				});

				return Monad.of(c);
			});

	}

}
