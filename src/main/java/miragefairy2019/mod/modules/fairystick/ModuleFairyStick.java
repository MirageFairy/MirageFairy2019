package miragefairy2019.mod.modules.fairystick;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeItem;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionNotNether;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionReplaceBlock;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionSpawnBlock;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe;
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRegistry;
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import static miragefairy2019.mod.lib.Configurator.*;

public class ModuleFairyStick {

    public static ItemStack itemStackFairyStick;

    public static void init(EventRegistryMod erMod) {

        erMod.initRegistry.register(() -> {
            ApiFairyStickCraft.fairyStickCraftRegistry = new FairyStickCraftRegistry();
        });

        // 妖精のステッキ
        item(erMod, ItemFairyStick::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_stick"), "fairyStick")
                .bind(setCreativeTab(() -> ApiMain.creativeTab()))
                .bind(onRegisterItem(i -> {
                    if (ApiMain.side().isClient()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), null));
                }))
                .bind(onCreateItemStack(i -> OreDictionary.registerOre("mirageFairy2019FairyStick", new ItemStack(i))))
                .bind(onCreateItemStack(i -> itemStackFairyStick = new ItemStack(i)));

        // レシピ登録
        erMod.addRecipe.register(() -> {

            // 水精→水源
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionNotNether()))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnBlock(() -> Blocks.WATER.getDefaultState())))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairyCrystal"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairyWaterRank1"))))
                    .get());

            // 溶岩精→溶岩流
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnBlock(() -> Blocks.FLOWING_LAVA.getDefaultState().withProperty(BlockDynamicLiquid.LEVEL, 15))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairyCrystal"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairyLavaRank1"))))
                    .get());

            // 蜘蛛精→糸ブロック
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionSpawnBlock(() -> Blocks.WEB.getDefaultState())))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairyCrystal"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairySpiderRank1"))))
                    .get());

            // 水＋ミラジウムの粉→妖水
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019FairyStick"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionReplaceBlock(
                            () -> Blocks.WATER.getDefaultState(),
                            () -> ApiOre.blockFluidMiragiumWater.getDefaultState())))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionConsumeItem(new OreIngredient("dustMiragium"))))
                    .get());

        });

    }

}
