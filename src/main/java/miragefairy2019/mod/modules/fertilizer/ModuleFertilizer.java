package miragefairy2019.mod.modules.fertilizer;

import static miragefairy2019.mod.lib.Configurator.*;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ModuleFertilizer
{

	public static void init(EventRegistryMod erMod)
	{

		// 肥料
		item(erMod, ItemFertilizer::new, new ResourceLocation(ModMirageFairy2019.MODID, "fertilizer"), "fertilizer")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(c -> {
				if (ApiMain.side().isClient()) ModelLoader.setCustomModelResourceLocation(c.get(), 0, new ModelResourceLocation(c.get().getRegistryName(), null));
				return Monad.of(c);
			});

	}

}
