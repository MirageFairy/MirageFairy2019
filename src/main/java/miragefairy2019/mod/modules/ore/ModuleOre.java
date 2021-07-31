package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.Configurator;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.IBlockVariant;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import miragefairy2019.mod.modules.ore.material.BlockMaterials;
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1;
import miragefairy2019.mod.modules.ore.material.ItemBlockMaterials;
import miragefairy2019.mod.modules.ore.ore.*;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static miragefairy2019.mod.lib.Configurator.*;

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

    public static ItemMultiMaterial<ItemVariantMaterial> itemMaterials;
    public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
    public static ItemBlockOre<EnumVariantOre2> itemBlockOre2;
    public static ItemBlockMaterials<EnumVariantMaterials1> itemBlockMaterials1;

    public static void init(EventRegistryMod erMod) {

        LoaderOreSeedDrop.loadOreSeedDrop();

        // マテリアル
        item(erMod, ItemMultiMaterial<ItemVariantMaterial>::new, new ResourceLocation(ModMirageFairy2019.MODID, "materials"), "materials")
                .bind(onRegisterItem(item -> ApiOre.itemMaterials1 = itemMaterials = item))
                .bind(setCreativeTab(() -> ApiMain.creativeTab()))
                .bind(c -> {

                    itemVariant(c.erMod, c, 0, () -> new ItemVariantMaterial("apatite_gem", "gemApatite")).bind(addOreName("gemApatite"));
                    itemVariant(c.erMod, c, 1, () -> new ItemVariantMaterial("fluorite_gem", "gemFluorite")).bind(addOreName("gemFluorite"));
                    itemVariant(c.erMod, c, 2, () -> new ItemVariantMaterial("sulfur_gem", "gemSulfur")).bind(addOreName("gemSulfur"));
                    itemVariant(c.erMod, c, 3, () -> new ItemVariantMaterial("miragium_dust", "dustMiragium")).bind(addOreName("dustMiragium"));
                    itemVariant(c.erMod, c, 4, () -> new ItemVariantMaterial("miragium_tiny_dust", "dustTinyMiragium")).bind(addOreName("dustTinyMiragium"));
                    itemVariant(c.erMod, c, 5, () -> new ItemVariantMaterial("miragium_ingot", "ingotMiragium")).bind(addOreName("ingotMiragium"));
                    itemVariant(c.erMod, c, 6, () -> new ItemVariantMaterial("cinnabar_gem", "gemCinnabar")).bind(addOreName("gemCinnabar"));
                    itemVariant(c.erMod, c, 7, () -> new ItemVariantMaterial("moonstone_gem", "gemMoonstone")).bind(addOreName("gemMoonstone"));
                    itemVariant(c.erMod, c, 8, () -> new ItemVariantMaterial("magnetite_gem", "gemMagnetite")).bind(addOreName("gemMagnetite"));
                    itemVariant(c.erMod, c, 9, () -> new ItemVariantMaterial("saltpeter_gem", "gemSaltpeter")).bind(addOreName("gemSaltpeter"));
                    itemVariant(c.erMod, c, 10, () -> new ItemVariantMaterial("pyrope_gem", "gemPyrope")).bind(addOreName("gemPyrope"));
                    itemVariant(c.erMod, c, 11, () -> new ItemVariantMaterial("smithsonite_gem", "gemSmithsonite")).bind(addOreName("gemSmithsonite"));
                    itemVariant(c.erMod, c, 12, () -> new ItemVariantMaterial("miragium_rod", "rodMiragium")).bind(addOreName("rodMiragium"));
                    itemVariant(c.erMod, c, 13, () -> new ItemVariantMaterial("miragium_nugget", "nuggetMiragium")).bind(addOreName("nuggetMiragium"));
                    itemVariant(c.erMod, c, 14, () -> new ItemVariantMaterial("nephrite_gem", "gemNephrite")).bind(addOreName("gemNephrite"));
                    itemVariant(c.erMod, c, 15, () -> new ItemVariantMaterial("topaz_gem", "gemTopaz")).bind(addOreName("gemTopaz"));
                    itemVariant(c.erMod, c, 16, () -> new ItemVariantMaterial("tourmaline_gem", "gemTourmaline")).bind(addOreName("gemTourmaline"));

                    erMod.registerItem.register(b -> {
                        if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
                    });

                    return Monad.of(c);
                });

        // 中身入りバケツ
        item(erMod, () -> new ItemFilledBucket(), new ResourceLocation(ModMirageFairy2019.MODID, "filled_bucket"), "filledBucket")
                .bind(setCreativeTab(() -> ApiMain.creativeTab()))
                .peek(c -> {

                    itemVariant(c.erMod, c, 0, () -> new ItemVariantFilledBucket("miragium_water_bucket", "bucketMiragiumWater", true, () -> Optional.of(blockFluidMiragiumWater.getDefaultState())))
                            .bind(addOreName("bucketMiragiumWater"))
                            .bind(addOreName("container1000MiragiumWater"))
                            .bind(registererEmptyBucketFiller(() -> blockFluidMiragiumWater.getDefaultState()));
                    itemVariant(c.erMod, c, 1, () -> new ItemVariantFilledBucket("mirage_flower_extract_bucket", "bucketMirageFlowerExtract", true, () -> Optional.of(blockFluidMirageFlowerExtract.getDefaultState())))
                            .bind(addOreName("bucketMirageFlowerExtract"))
                            .bind(addOreName("container1000MirageFlowerExtract"))
                            .bind(registererEmptyBucketFiller(() -> blockFluidMirageFlowerExtract.getDefaultState()));
                    itemVariant(c.erMod, c, 2, () -> new ItemVariantFilledBucket("mirage_flower_oil_bucket", "bucketMirageFlowerOil", true, () -> Optional.of(blockFluidMirageFlowerOil.getDefaultState())))
                            .bind(addOreName("bucketMirageFlowerOil"))
                            .bind(addOreName("container1000MirageFlowerOil"))
                            .bind(registererEmptyBucketFiller(() -> blockFluidMirageFlowerOil.getDefaultState()));

                    erMod.registerItem.register(b -> {
                        if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
                    });

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
            ApiOre.blockOre1 = blockOre1 = new BlockOre<>(EnumVariantOre1.variantList);
            blockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
            blockOre1.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockOre1);

            // 鉱石2
            ApiOre.blockOre2 = blockOre2 = new BlockOre<>(EnumVariantOre2.variantList);
            blockOre2.setRegistryName(ModMirageFairy2019.MODID, "ore2");
            blockOre2.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockOre2);

            // ブロック
            ApiOre.blockMaterials1 = blockMaterials1 = new BlockMaterials<>(EnumVariantMaterials1.variantList);
            blockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
            blockMaterials1.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockMaterials1);

            // 妖水ブロック
            ApiOre.blockFluidMiragiumWater = blockFluidMiragiumWater = new BlockFluidMiragiumWater(fluidMiragiumWater);
            blockFluidMiragiumWater.setRegistryName(ModMirageFairy2019.MODID, "miragium_water");
            blockFluidMiragiumWater.setUnlocalizedName("miragiumWater");
            blockFluidMiragiumWater.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockFluidMiragiumWater);
            if (ApiMain.side().isClient()) {
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
            blockFluidMirageFlowerExtract.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockFluidMirageFlowerExtract);
            if (ApiMain.side().isClient()) {
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
            blockFluidMirageFlowerOil.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockFluidMirageFlowerOil);
            if (ApiMain.side().isClient()) {
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
            itemBlockOre1.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.ITEMS.register(itemBlockOre1);
            if (ApiMain.side().isClient()) {
                for (IBlockVariantOre variant : blockOre1.variantList) {
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
            itemBlockOre2.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.ITEMS.register(itemBlockOre2);
            if (ApiMain.side().isClient()) {
                for (IBlockVariantOre variant : blockOre2.variantList) {
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
            itemBlockMaterials1.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.ITEMS.register(itemBlockMaterials1);
            if (ApiMain.side().isClient()) {
                for (IBlockVariant variant : blockMaterials1.variantList) {
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
            itemFluidMiragiumWater.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.ITEMS.register(itemFluidMiragiumWater);
            if (ApiMain.side().isClient()) {
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
            itemFluidMirageFlowerExtract.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.ITEMS.register(itemFluidMirageFlowerExtract);
            if (ApiMain.side().isClient()) {
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
            itemFluidMirageFlowerOil.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.ITEMS.register(itemFluidMirageFlowerOil);
            if (ApiMain.side().isClient()) {
                ModelLoader.setCustomModelResourceLocation(itemFluidMirageFlowerOil,
                        0,
                        new ModelResourceLocation(itemFluidMirageFlowerOil.getRegistryName(), null));
            }

        });
        erMod.createItemStack.register(ic -> {
            for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
                OreDictionary.registerOre(variant.oreName, new ItemStack(itemBlockMaterials1, 1, variant.metadata));
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

    public static <V extends ItemVariantMaterial> Function<Configurator<V>, Monad<Configurator<V>>> registererEmptyBucketFiller(Supplier<IBlockState> sBlockState) {
        return cv -> {
            MinecraftForge.EVENT_BUS.register(new Object() {
                @SubscribeEvent
                public void accept(FillBucketEvent event) {
                    if (event.getResult() != Result.DEFAULT) return;
                    if (event.getEmptyBucket().getItem() == Items.BUCKET) {
                        boolean result = ItemFilledBucket.tryDrainFluid(
                                event.getWorld(),
                                event.getEntityPlayer(),
                                event.getEmptyBucket(),
                                event.getTarget(),
                                sBlockState.get());
                        if (result) {
                            event.setFilledBucket(cv.get().createItemStack());
                            event.setResult(Result.ALLOW);
                        }
                    }
                }
            });
            return Monad.of(cv);
        };
    }

}
