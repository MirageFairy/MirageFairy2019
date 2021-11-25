package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.materialsfairy.ApiMaterialsFairy;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.Configurator;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.ItemBlockMulti;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeBlock;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeItem;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionSpawnItem;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe;
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft;
import miragefairy2019.mod3.main.api.ApiMain;
import miragefairy2019.mod3.mirageflower.api.ApiMirageFlower;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Optional;
import java.util.function.Function;

import static miragefairy2019.mod.api.composite.ApiComposite.instance;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.lib.Configurator.*;

public class ModuleMaterialsFairy {

    public static BlockTwinkleStone blockTwinkleStone;
    public static ItemBlockMulti<BlockTwinkleStone, EnumVariantTwinkleStone> itemBlockTwinkleStone;
    public static ItemStack itemStackManaRodShine;
    public static ItemStack itemStackManaRodFire;
    public static ItemStack itemStackManaRodWind;
    public static ItemStack itemStackManaRodGaia;
    public static ItemStack itemStackManaRodAqua;
    public static ItemStack itemStackManaRodDark;
    public static ItemStack itemStackManaRodQuartz;
    public static ItemStack itemStackStickMirageFlower;
    public static ItemStack itemStackLeafMirageFlower;
    public static ItemStack itemStackStickMirageFairyWood;
    public static ItemStack itemStackBottleMiragiumWater;
    public static ItemStack itemStackBottleMirageFlowerExtract;
    public static ItemStack itemStackBottleMirageFlowerOil;
    public static ItemStack itemStackManaRodGlass;
    public static ItemStack itemStackMirageFairyLeather;

    public static void init(EventRegistryMod erMod) {

        // マテリアル
        item(erMod, ItemMultiFairyMaterial<ItemVariantFairyMaterial>::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_materials"), "materialsFairy")
                .bind(setCreativeTab(() -> ApiMain.creativeTab))
                .bind(c -> {

                    itemVariant(c.erMod, c, 0, () -> new ItemVariantFairyMaterial("shine_mana_rod", "manaRodShine", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(moonstone.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodShine"))
                            .bind(onCreateItemStack(v -> itemStackManaRodShine = v.createItemStack()));

                    itemVariant(c.erMod, c, 1, () -> new ItemVariantFairyMaterial("fire_mana_rod", "manaRodFire", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(cinnabar.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodFire"))
                            .bind(onCreateItemStack(v -> itemStackManaRodFire = v.createItemStack()));

                    itemVariant(c.erMod, c, 2, () -> new ItemVariantFairyMaterial("wind_mana_rod", "manaRodWind", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(fluorite.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodWind"))
                            .bind(onCreateItemStack(v -> itemStackManaRodWind = v.createItemStack()));

                    itemVariant(c.erMod, c, 3, () -> new ItemVariantFairyMaterial("gaia_mana_rod", "manaRodGaia", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(sulfur.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodGaia"))
                            .bind(onCreateItemStack(v -> itemStackManaRodGaia = v.createItemStack()));

                    itemVariant(c.erMod, c, 4, () -> new ItemVariantFairyMaterial("aqua_mana_rod", "manaRodAqua", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(apatite.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodAqua"))
                            .bind(onCreateItemStack(v -> itemStackManaRodAqua = v.createItemStack()));

                    itemVariant(c.erMod, c, 5, () -> new ItemVariantFairyMaterial("dark_mana_rod", "manaRodDark", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(magnetite.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodDark"))
                            .bind(onCreateItemStack(v -> itemStackManaRodDark = v.createItemStack()));

                    itemVariant(c.erMod, c, 6, () -> new ItemVariantFairyMaterial("quartz_mana_rod", "manaRodQuartz", 3))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(quartz.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodQuartz"))
                            .bind(onCreateItemStack(v -> itemStackManaRodQuartz = v.createItemStack()))
                            .bind(onAddRecipe(v -> {

                                // フェアリーステッキクラフト
                                ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeBlock(() -> ApiOre.blockFluidMirageFlowerExtract.getDefaultState())))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019ManaRodGlass"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("gemQuartz"), 16)))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnItem(() -> v.createItemStack())))
                                        .get());

                            }));

                    itemVariant(c.erMod, c, 7, () -> new ItemVariantFairyMaterial("mirage_flower_stick", "stickMirageFlower", 1))
                            .bind(addOreName("stickMirageFlower"))
                            .bind(onCreateItemStack(v -> itemStackStickMirageFlower = v.createItemStack()))
                            .bind(onAddRecipe(v -> {

                                // フェアリーステッキクラフト
                                ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeBlock(() -> ApiOre.blockFluidMiragiumWater.getDefaultState())))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("leafMirageFlower"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("bone"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("gemApatite"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnItem(() -> v.createItemStack())))
                                        .get());

                            }));

                    itemVariant(c.erMod, c, 8, () -> new ItemVariantFairyMaterial("mirage_flower_leaf", "leafMirageFlower", 0))
                            .bind(addOreName("leafMirageFlower"))
                            .bind(onCreateItemStack(v -> itemStackLeafMirageFlower = v.createItemStack()));

                    itemVariant(c.erMod, c, 9, () -> new ItemVariantFairyMaterial("mirage_fairy_wood_stick", "stickMirageFairyWood", 4))
                            .bind(addOreName("stickMirageFairyWood"))
                            .bind(onCreateItemStack(v -> itemStackStickMirageFairyWood = v.createItemStack()));

                    itemVariant(c.erMod, c, 10, () -> new ItemVariantFairyMaterial("miragium_water_bottle", "bottleMiragiumWater", 0))
                            .bind(addOreName("bottleMiragiumWater"))
                            .bind(addOreName("container250MiragiumWater"))
                            .bind(ItemVariantFairyMaterial.setterContainerItem(Optional.of(() -> new ItemStack(Items.GLASS_BOTTLE))))
                            .bind(onCreateItemStack(v -> itemStackBottleMiragiumWater = v.createItemStack()));

                    itemVariant(c.erMod, c, 11, () -> new ItemVariantFairyMaterial("mirage_flower_extract_bottle", "bottleMirageFlowerExtract", 2))
                            .bind(addOreName("bottleMirageFlowerExtract"))
                            .bind(addOreName("container250MirageFlowerExtract"))
                            .bind(ItemVariantFairyMaterial.setterContainerItem(Optional.of(() -> new ItemStack(Items.GLASS_BOTTLE))))
                            .bind(onCreateItemStack(v -> itemStackBottleMirageFlowerExtract = v.createItemStack()));

                    itemVariant(c.erMod, c, 12, () -> new ItemVariantFairyMaterial("mirage_flower_oil_bottle", "bottleMirageFlowerOil", 4))
                            .bind(addOreName("bottleMirageFlowerOil"))
                            .bind(addOreName("container250MirageFlowerOil"))
                            .bind(ItemVariantFairyMaterial.setterContainerItem(Optional.of(() -> new ItemStack(Items.GLASS_BOTTLE))))
                            .bind(onCreateItemStack(v -> itemStackBottleMirageFlowerOil = v.createItemStack()))
                            .bind(onAddRecipe(v -> {

                                // 空き瓶＋ミラ種50個＋辰砂16個＞珠玉→ミラオイル瓶
                                ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019CraftingToolFairyWandPolishing"))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(Ingredient.fromItem(ApiMirageFlower.itemMirageFlowerSeeds), 50)))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("gemCinnabar"), 4)))
                                        .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnItem(() -> v.createItemStack())))
                                        .get());

                            }));

                    itemVariant(c.erMod, c, 13, () -> new ItemVariantFairyMaterial("glass_mana_rod", "manaRodGlass", 2))
                            .bind(addComponent(instance(miragium.get(), 0.5)))
                            .bind(addComponent(instance(glass.get(), 0.5)))
                            .bind(addOreName("mirageFairy2019ManaRodGlass"))
                            .bind(onCreateItemStack(v -> itemStackManaRodGlass = v.createItemStack()));

                    itemVariant(c.erMod, c, 14, () -> new ItemVariantFairyMaterial("mirage_fairy_leather", "mirageFairyLeather", 4))
                            .bind(addOreName("mirageFairyLeather"))
                            .bind(onCreateItemStack(v -> itemStackMirageFairyLeather = v.createItemStack()));

                    erMod.registerItem.register(b -> {
                        if (ApiMain.side.isClient()) c.get().setCustomModelResourceLocations();
                    });

                    return Monad.of(c);
                });

        erMod.registerBlock.register(b -> {

            // トゥインクルストーン
            ApiMaterialsFairy.blockTwinkleStone = blockTwinkleStone = new BlockTwinkleStone();
            blockTwinkleStone.setRegistryName(ModMirageFairy2019.MODID, "twinkle_stone");
            blockTwinkleStone.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockTwinkleStone);

        });
        erMod.registerItem.register(b -> {

            // トゥインクルストーン
            ApiMaterialsFairy.itemBlockTwinkleStone = itemBlockTwinkleStone = new ItemBlockMulti<>(blockTwinkleStone);
            itemBlockTwinkleStone.setRegistryName(ModMirageFairy2019.MODID, "twinkle_stone");
            itemBlockTwinkleStone.setUnlocalizedName("twinkle_stone");
            itemBlockTwinkleStone.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemBlockTwinkleStone);
            if (ApiMain.side.isClient()) {
                for (EnumVariantTwinkleStone variant : blockTwinkleStone.variantList) {
                    ModelLoader.setCustomModelResourceLocation(
                            itemBlockTwinkleStone,
                            variant.getMetadata(),
                            new ModelResourceLocation(new ResourceLocation(itemBlockTwinkleStone.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
                }
            }

        });
        erMod.createItemStack.register(ic -> {
            for (EnumVariantTwinkleStone variant : EnumVariantTwinkleStone.values()) {
                for (String oreName : variant.oreNames) {
                    OreDictionary.registerOre(oreName, new ItemStack(itemBlockTwinkleStone, 1, variant.metadata));
                }
            }
        });

    }

    public static <V extends ItemVariantFairyMaterial> Function<Configurator<V>, Monad<Configurator<V>>> addComponent(IComponentInstance componentInstance) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(v -> v.addComponent(componentInstance)));
    }

}
