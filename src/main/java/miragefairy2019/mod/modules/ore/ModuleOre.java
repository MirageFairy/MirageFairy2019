package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.multi.IBlockVariant;
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOre {

    public static Fluid fluidMiragiumWater;
    public static Fluid fluidMirageFlowerExtract;
    public static Fluid fluidMirageFlowerOil;

    public static BlockFluidMiragiumWater blockFluidMiragiumWater;
    public static BlockFluidMiragiumWater blockFluidMirageFlowerExtract;
    public static BlockFluidMiragiumWater blockFluidMirageFlowerOil;

    public static ItemBlock itemFluidMiragiumWater;
    public static ItemBlock itemFluidMirageFlowerExtract;
    public static ItemBlock itemFluidMirageFlowerOil;

    //

    public static BlockOre<EnumVariantOre1> blockOre1;
    public static BlockOre<EnumVariantOre2> blockOre2;
    public static BlockMaterials<EnumVariantMaterials1> blockMaterials1;

    public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
    public static ItemBlockOre<EnumVariantOre2> itemBlockOre2;
    public static ItemBlockMaterials<EnumVariantMaterials1> itemBlockMaterials1;

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public static void init(EventRegistryMod erMod) {

        erMod.initRegistry.register(() -> {
            ApiOreSeedDrop.oreSeedDropRegistry = new OreSeedDropRegistry();
            LoaderOreSeedDrop.INSTANCE.loadOreSeedDrop();
        });

        erMod.registerBlock.register(b -> {

            // 妖水フルイド
            ApiOre.fluidMiragiumWater = fluidMiragiumWater = new Fluid(
                    "miragium_water",
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/miragium_water_still"),
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/miragium_water_flow"),
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/miragium_water_overlay"));
            fluidMiragiumWater.setViscosity(600);
            FluidRegistry.registerFluid(fluidMiragiumWater);
            FluidRegistry.addBucketForFluid(fluidMiragiumWater);

            // 妖水フルイド
            ApiOre.fluidMirageFlowerExtract = fluidMirageFlowerExtract = new Fluid(
                    "mirage_flower_extract",
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/mirage_flower_extract_still"),
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/mirage_flower_extract_flow"),
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/mirage_flower_extract_overlay"));
            fluidMirageFlowerExtract.setViscosity(1000);
            FluidRegistry.registerFluid(fluidMirageFlowerExtract);
            FluidRegistry.addBucketForFluid(fluidMirageFlowerExtract);

            // 妖水フルイド
            ApiOre.fluidMirageFlowerOil = fluidMirageFlowerOil = new Fluid(
                    "mirage_flower_oil",
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/mirage_flower_oil_still"),
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/mirage_flower_oil_flow"),
                    new ResourceLocation(ModMirageFairy2019.MODID, "blocks/mirage_flower_oil_overlay"));
            fluidMirageFlowerOil.setViscosity(1500);
            FluidRegistry.registerFluid(fluidMirageFlowerOil);
            FluidRegistry.addBucketForFluid(fluidMirageFlowerOil);

            //

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

            // 妖水ブロック
            ApiOre.blockFluidMiragiumWater = blockFluidMiragiumWater = new BlockFluidMiragiumWater(fluidMiragiumWater);
            blockFluidMiragiumWater.setRegistryName(ModMirageFairy2019.MODID, "miragium_water");
            blockFluidMiragiumWater.setUnlocalizedName("miragiumWater");
            blockFluidMiragiumWater.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockFluidMiragiumWater);
            if (ApiMain.side.isClient()) {
                new Object() {
                    @SideOnly(Side.CLIENT)
                    public void run() {
                        ModelLoader.setCustomStateMapper(blockFluidMiragiumWater, new StateMapperBase() {
                            @Override
                            protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
                                return new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "miragium_water"), "fluid");
                            }
                        });
                    }
                }.run();
            }

            // 妖水ブロック
            ApiOre.blockFluidMirageFlowerExtract = blockFluidMirageFlowerExtract = new BlockFluidMiragiumWater(fluidMirageFlowerExtract);
            blockFluidMirageFlowerExtract.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_extract");
            blockFluidMirageFlowerExtract.setUnlocalizedName("mirageFlowerExtract");
            blockFluidMirageFlowerExtract.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockFluidMirageFlowerExtract);
            if (ApiMain.side.isClient()) {
                new Object() {
                    @SideOnly(Side.CLIENT)
                    public void run() {
                        ModelLoader.setCustomStateMapper(blockFluidMirageFlowerExtract, new StateMapperBase() {
                            @Override
                            protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
                                return new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_flower_extract"), "fluid");
                            }
                        });
                    }
                }.run();
            }

            // 妖水ブロック
            ApiOre.blockFluidMirageFlowerOil = blockFluidMirageFlowerOil = new BlockFluidMiragiumWater(fluidMirageFlowerOil);
            blockFluidMirageFlowerOil.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_oil");
            blockFluidMirageFlowerOil.setUnlocalizedName("mirageFlowerOil");
            blockFluidMirageFlowerOil.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockFluidMirageFlowerOil);
            if (ApiMain.side.isClient()) {
                new Object() {
                    @SideOnly(Side.CLIENT)
                    public void run() {
                        ModelLoader.setCustomStateMapper(blockFluidMirageFlowerOil, new StateMapperBase() {
                            @Override
                            protected ModelResourceLocation getModelResourceLocation(IBlockState var1) {
                                return new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "mirage_flower_oil"), "fluid");
                            }
                        });
                    }
                }.run();
            }

        });
        erMod.registerItem.register(b -> {

            // 鉱石1
            ApiOre.itemBlockOre1 = itemBlockOre1 = new ItemBlockOre<>(blockOre1);
            itemBlockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
            itemBlockOre1.setUnlocalizedName("ore1");
            itemBlockOre1.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemBlockOre1);
            if (ApiMain.side.isClient()) {
                for (IBlockVariantOre variant : blockOre1.variantList.getBlockVariants()) {
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
                for (IBlockVariantOre variant : blockOre2.variantList.getBlockVariants()) {
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
                for (IBlockVariant variant : blockMaterials1.variantList.getBlockVariants()) {
                    ModelLoader.setCustomModelResourceLocation(
                            itemBlockMaterials1,
                            variant.getMetadata(),
                            new ModelResourceLocation(new ResourceLocation(itemBlockMaterials1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
                }
            }

            // 妖水アイテム
            ApiOre.itemFluidMiragiumWater = itemFluidMiragiumWater = new ItemBlock(blockFluidMiragiumWater) {
                public void getSubItems(CreativeTabs p_getSubItems_1_, NonNullList<ItemStack> p_getSubItems_2_) {
                    if (this.isInCreativeTab(p_getSubItems_1_)) {
                        this.block.getSubBlocks(p_getSubItems_1_, p_getSubItems_2_);
                    }
                }
            };
            itemFluidMiragiumWater.setRegistryName(ModMirageFairy2019.MODID, "miragium_water");
            itemFluidMiragiumWater.setUnlocalizedName("miragiumWater");
            itemFluidMiragiumWater.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemFluidMiragiumWater);
            if (ApiMain.side.isClient()) {
                ModelLoader.setCustomModelResourceLocation(itemFluidMiragiumWater,
                        0,
                        new ModelResourceLocation(itemFluidMiragiumWater.getRegistryName(), null));
            }

            // 妖水アイテム
            ApiOre.itemFluidMirageFlowerExtract = itemFluidMirageFlowerExtract = new ItemBlock(blockFluidMirageFlowerExtract) {
                public void getSubItems(CreativeTabs p_getSubItems_1_, NonNullList<ItemStack> p_getSubItems_2_) {
                    if (this.isInCreativeTab(p_getSubItems_1_)) {
                        this.block.getSubBlocks(p_getSubItems_1_, p_getSubItems_2_);
                    }
                }
            };
            itemFluidMirageFlowerExtract.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_extract");
            itemFluidMirageFlowerExtract.setUnlocalizedName("mirageFlowerExtract");
            itemFluidMirageFlowerExtract.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemFluidMirageFlowerExtract);
            if (ApiMain.side.isClient()) {
                ModelLoader.setCustomModelResourceLocation(itemFluidMirageFlowerExtract,
                        0,
                        new ModelResourceLocation(itemFluidMirageFlowerExtract.getRegistryName(), null));
            }

            // 妖水アイテム
            ApiOre.itemFluidMirageFlowerOil = itemFluidMirageFlowerOil = new ItemBlock(blockFluidMirageFlowerOil) {
                public void getSubItems(CreativeTabs p_getSubItems_1_, NonNullList<ItemStack> p_getSubItems_2_) {
                    if (this.isInCreativeTab(p_getSubItems_1_)) {
                        this.block.getSubBlocks(p_getSubItems_1_, p_getSubItems_2_);
                    }
                }
            };
            itemFluidMirageFlowerOil.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_oil");
            itemFluidMirageFlowerOil.setUnlocalizedName("mirageFlowerOil");
            itemFluidMirageFlowerOil.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemFluidMirageFlowerOil);
            if (ApiMain.side.isClient()) {
                ModelLoader.setCustomModelResourceLocation(itemFluidMirageFlowerOil,
                        0,
                        new ModelResourceLocation(itemFluidMirageFlowerOil.getRegistryName(), null));
            }

        });
        erMod.createItemStack.register(ic -> {
            for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
                OreDictionary.registerOre(variant.getOreName(), new ItemStack(itemBlockMaterials1, 1, variant.metadata));
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
