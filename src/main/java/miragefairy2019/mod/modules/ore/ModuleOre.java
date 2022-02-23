package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.ore.ore.BlockOre;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre2;
import miragefairy2019.mod.modules.ore.ore.IBlockVariantOre;
import miragefairy2019.mod.modules.ore.ore.ItemBlockOre;
import miragefairy2019.mod3.main.api.ApiMain;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleOre {

    public static BlockOre<EnumVariantOre1> blockOre1;
    public static BlockOre<EnumVariantOre2> blockOre2;

    public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
    public static ItemBlockOre<EnumVariantOre2> itemBlockOre2;

    public static void init(EventRegistryMod erMod) {

        erMod.registerBlock.register(b -> {

            // 鉱石1
            ApiOre.blockOre1 = blockOre1 = new BlockOre<>(EnumVariantOre1.Companion.getVariantList());
            blockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
            blockOre1.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockOre1);

            // 鉱石2
            ApiOre.blockOre2 = blockOre2 = new BlockOre<>(EnumVariantOre2.Companion.getVariantList());
            blockOre2.setRegistryName(ModMirageFairy2019.MODID, "ore2");
            blockOre2.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockOre2);

        });
        erMod.registerItem.register(b -> {

            // 鉱石1
            ApiOre.itemBlockOre1 = itemBlockOre1 = new ItemBlockOre<>(blockOre1);
            itemBlockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
            itemBlockOre1.setUnlocalizedName("ore1");
            itemBlockOre1.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemBlockOre1);
            if (ApiMain.side.isClient()) {
                for (IBlockVariantOre variant : blockOre1.getVariantList().getBlockVariants()) {
                    ModelLoader.setCustomModelResourceLocation(
                        itemBlockOre1,
                        variant.getMetadata(),
                        new ModelResourceLocation(new ResourceLocation(itemBlockOre1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
                }
            }

            // 鉱石2
            ApiOre.itemBlockOre2 = itemBlockOre2 = new ItemBlockOre<>(blockOre2);
            itemBlockOre2.setRegistryName(ModMirageFairy2019.MODID, "ore2");
            itemBlockOre2.setUnlocalizedName("ore2");
            itemBlockOre2.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemBlockOre2);
            if (ApiMain.side.isClient()) {
                for (IBlockVariantOre variant : blockOre2.getVariantList().getBlockVariants()) {
                    ModelLoader.setCustomModelResourceLocation(
                        itemBlockOre2,
                        variant.getMetadata(),
                        new ModelResourceLocation(new ResourceLocation(itemBlockOre2.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
                }
            }

        });
        erMod.addRecipe.register(() -> {

            // 製錬
            //GameRegistry.addSmelting(ApiOre.itemStackDustMiragium, ApiOre.itemStackIngotMiragium, 0);

        });
    }

}
