package miragefairy2019.mod.modules.sphere;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.OreIngredientComplex;
import miragefairy2019.modkt.api.fairystickcraft.ApiFairyStickCraft;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionConsumeBlock;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionConsumeItem;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionSpawnItem;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionUseItem;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftRecipe;
import mirrg.boron.util.UtilsString;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleSphere {

    public static ItemSpheres itemSpheres;

    public static Map<EnumSphere, VariantSphere> variantSpheres = new HashMap<>();

    public static void init(EventRegistryMod erMod) {
        erMod.registerItem.register(b -> {

            // スフィアのアイテム登録
            {
                ItemSpheres item = new ItemSpheres();
                item.setRegistryName(ModMirageFairy2019.MODID, "spheres");
                item.setUnlocalizedName("spheres");
                item.setCreativeTab(ApiMain.creativeTab());
                List<EnumSphere> spheres = EnumSphere.values().toList();
                for (int i = 0; i < spheres.size(); i++) {
                    EnumSphere sphere = spheres.get(i);

                    VariantSphere variantSphere = new VariantSphere(sphere);
                    item.registerVariant(i, variantSphere);
                    variantSpheres.put(sphere, variantSphere);

                }
                ForgeRegistries.ITEMS.register(item);
                if (ApiMain.side().isClient()) {
                    for (Tuple<Integer, VariantSphere> tuple : item.getVariants()) {
                        ModelLoader.setCustomModelResourceLocation(
                                item,
                                tuple.x,
                                new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "sphere"), null));
                    }
                }
                itemSpheres = item;
            }

        });
        erMod.registerItemColorHandler.register(new Runnable() {
            @SideOnly(Side.CLIENT)
            @Override
            public void run() {

                // スフィアのカスタム色
                @SideOnly(Side.CLIENT)
                class ItemColorImpl implements IItemColor {

                    @Override
                    public int colorMultiplier(ItemStack stack, int tintIndex) {
                        VariantSphere variant = itemSpheres.getVariant(stack).orElse(null);
                        if (variant == null) return 0xFFFFFF;
                        if (tintIndex == 0) return variant.sphere.colorBackground;
                        if (tintIndex == 1) return variant.sphere.colorPlasma;
                        if (tintIndex == 2) return variant.sphere.colorCore;
                        if (tintIndex == 3) return variant.sphere.colorHighlight;
                        return 0xFFFFFF;
                    }

                }
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(), itemSpheres);

            }
        });
        erMod.createItemStack.register(ic -> {

            // スフィアの鉱石辞書
            for (Tuple<Integer, VariantSphere> tuple : itemSpheres.getVariants()) {
                OreDictionary.registerOre(tuple.y.sphere.getOreName(), tuple.y.createItemStack());
                OreDictionary.registerOre("mirageFairy2019SphereAny", tuple.y.createItemStack());
            }

        });
        erMod.addRecipe.register(() -> {

            for (Tuple<Integer, VariantSphere> variant : itemSpheres.getVariants()) {

                {
                    Ingredient ingredient = variant.y.sphere.sIngredientWithFluorite.get();
                    if (ingredient != null) {

                        // クラフトレシピ
                        GameRegistry.addShapelessRecipe(
                                new ResourceLocation(ModMirageFairy2019.MODID + ":" + variant.y.sphere.abilityType.getName() + "_sphere_with_fluorite"),
                                new ResourceLocation(ModMirageFairy2019.MODID + ":" + variant.y.sphere.abilityType.getName() + "_sphere"),
                                variant.y.createItemStack(),
                                new OreIngredientComplex("container1000MiragiumWater"),
                                new OreIngredient("gemFluorite"),
                                new OreIngredientComplex("mirageFairy2019CraftingToolFairyWandCrafting"),
                                new OreIngredient("mirageFairy2019FairyAbility" + UtilsString.toUpperCaseHead(variant.y.sphere.abilityType.getName())),
                                ingredient);

                        // 妖精のステッキレシピ
                        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeBlock(() -> ApiOre.blockFluidMiragiumWater.getDefaultState())))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("gemFluorite"))))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairyAbility" + UtilsString.toUpperCaseHead(variant.y.sphere.abilityType.getName())))))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(ingredient)))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnItem(() -> variant.y.createItemStack())))
                                .get());

                    }
                }

                {
                    Ingredient ingredient = variant.y.sphere.sIngredientGem.get();
                    if (ingredient != null) {

                        // クラフトレシピ
                        GameRegistry.addShapelessRecipe(
                                new ResourceLocation(ModMirageFairy2019.MODID + ":" + variant.y.sphere.abilityType.getName() + "_sphere_from_gem"),
                                new ResourceLocation(ModMirageFairy2019.MODID + ":" + variant.y.sphere.abilityType.getName() + "_sphere"),
                                variant.y.createItemStack(),
                                new OreIngredientComplex("container1000MiragiumWater"),
                                new OreIngredientComplex("mirageFairy2019CraftingToolFairyWandCrafting"),
                                new OreIngredient("mirageFairy2019FairyAbility" + UtilsString.toUpperCaseHead(variant.y.sphere.abilityType.getName())),
                                ingredient);

                        // 妖精のステッキレシピ
                        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeBlock(() -> ApiOre.blockFluidMiragiumWater.getDefaultState())))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairyAbility" + UtilsString.toUpperCaseHead(variant.y.sphere.abilityType.getName())))))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(ingredient)))
                                .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnItem(() -> variant.y.createItemStack())))
                                .get());

                    }
                }

            }

        });
    }

}
