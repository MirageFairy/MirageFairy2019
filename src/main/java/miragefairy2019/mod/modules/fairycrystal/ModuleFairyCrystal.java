package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod3.main.api.ApiMain;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairyCrystal {

    public static ItemFairyCrystal itemFairyCrystal;

    public static VariantFairyCrystal variantFairyCrystal;
    public static VariantFairyCrystalChristmas variantFairyCrystalChristmas;
    public static VariantFairyCrystal variantFairyCrystalPure;

    public static void init(EventRegistryMod erMod) {
        erMod.registerItem.register(b -> {

            // 妖晶のアイテム登録
            {
                ItemFairyCrystal item = new ItemFairyCrystal();
                item.setRegistryName(ModMirageFairy2019.MODID, "fairy_crystal");
                item.setUnlocalizedName("fairyCrystal");
                item.setCreativeTab(ApiMain.creativeTab());
                item.registerVariant(0, variantFairyCrystal = new VariantFairyCrystal("fairy_crystal", "fairyCrystal", "mirageFairyCrystal"));
                item.registerVariant(1, variantFairyCrystalChristmas = new VariantFairyCrystalChristmas("christmas_fairy_crystal", "fairyCrystalChristmas", "mirageFairyCrystalChristmas"));
                item.registerVariant(2, variantFairyCrystalPure = new VariantFairyCrystalPure("pure_fairy_crystal", "fairyCrystalPure", "mirageFairyCrystalPure"));
                ForgeRegistries.ITEMS.register(item);
                if (ApiMain.side().isClient()) {
                    for (Tuple<Integer, VariantFairyCrystal> tuple : item.getVariants()) {
                        ModelLoader.setCustomModelResourceLocation(
                                item,
                                tuple.x,
                                new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, tuple.y.registryName), null));
                    }
                }
                itemFairyCrystal = item;
            }

        });
        erMod.createItemStack.register(ic -> {

            // 鉱石辞書
            for (Tuple<Integer, VariantFairyCrystal> tuple : itemFairyCrystal.getVariants()) {
                OreDictionary.registerOre(tuple.y.oreName, tuple.y.createItemStack());
                OreDictionary.registerOre("mirageFairyCrystalAny", tuple.y.createItemStack());
            }

        });
    }

}
