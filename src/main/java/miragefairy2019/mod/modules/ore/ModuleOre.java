package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.IBlockVariant;
import miragefairy2019.mod.modules.ore.material.BlockMaterials;
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1;
import miragefairy2019.mod.modules.ore.material.ItemBlockMaterials;
import miragefairy2019.mod.modules.ore.ore.BlockOre;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre2;
import miragefairy2019.mod.modules.ore.ore.IBlockVariantOre;
import miragefairy2019.mod.modules.ore.ore.ItemBlockOre;
import miragefairy2019.mod3.main.api.ApiMain;
import miragefairy2019.mod3.oreseeddrop.OreSeedDropRegistry;
import miragefairy2019.mod3.oreseeddrop.api.ApiOreSeedDrop;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOre {

    public static BlockOre<EnumVariantOre1> blockOre1;
    public static BlockOre<EnumVariantOre2> blockOre2;
    public static BlockMaterials<EnumVariantMaterials1> blockMaterials1;

    public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
    public static ItemBlockOre<EnumVariantOre2> itemBlockOre2;
    public static ItemBlockMaterials<EnumVariantMaterials1> itemBlockMaterials1;

    public static void init(EventRegistryMod erMod) {

        erMod.initRegistry.register(() -> {
            ApiOreSeedDrop.oreSeedDropRegistry = new OreSeedDropRegistry();
            LoaderOreSeedDrop.INSTANCE.loadOreSeedDrop();
        });

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

            // ブロック
            ApiOre.blockMaterials1 = blockMaterials1 = new BlockMaterials<>(EnumVariantMaterials1.Companion.getVariantList());
            blockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
            blockMaterials1.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockMaterials1);

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

            // ブロック
            ApiOre.itemBlockMaterials1 = itemBlockMaterials1 = new ItemBlockMaterials<>(blockMaterials1);
            itemBlockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
            itemBlockMaterials1.setUnlocalizedName("materials1");
            itemBlockMaterials1.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemBlockMaterials1);
            if (ApiMain.side.isClient()) {
                for (IBlockVariant variant : blockMaterials1.getVariantList().getBlockVariants()) {
                    ModelLoader.setCustomModelResourceLocation(
                        itemBlockMaterials1,
                        variant.getMetadata(),
                        new ModelResourceLocation(new ResourceLocation(itemBlockMaterials1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
                }
            }

        });
        erMod.createItemStack.register(ic -> {
            for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
                OreDictionary.registerOre(variant.getOreName(), new ItemStack(itemBlockMaterials1, 1, variant.getMetadata()));
            }
            OreDictionary.registerOre("container1000Water", Items.WATER_BUCKET);
            OreDictionary.registerOre("container1000Lava", Items.LAVA_BUCKET);
            OreDictionary.registerOre("wool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
            OreDictionary.registerOre("ice", new ItemStack(Blocks.ICE));
            OreDictionary.registerOre("gemCoal", new ItemStack(Items.COAL, 1, 0));
        });
        erMod.addRecipe.register(() -> {

            // 製錬
            //GameRegistry.addSmelting(ApiOre.itemStackDustMiragium, ApiOre.itemStackIngotMiragium, 0);

        });
    }

}
