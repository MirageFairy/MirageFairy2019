package miragefairy2019.mod.modules.fertilizer;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import static miragefairy2019.mod.lib.Configurator.*;

public class ModuleFertilizer {

    public static void init(EventRegistryMod erMod) {

        // 肥料
        item(erMod, ItemFertilizer::new, new ResourceLocation(ModMirageFairy2019.MODID, "fertilizer"), "fertilizer")
                .bind(setCreativeTab(() -> ApiMain.creativeTab()))
                .bind(onRegisterItem(i -> {
                    if (ApiMain.side().isClient()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), null));
                }));

    }

}
