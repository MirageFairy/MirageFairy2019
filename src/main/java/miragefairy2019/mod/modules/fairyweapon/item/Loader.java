package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper;
import miragefairy2019.mod.lib.Configurator;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.modkt.api.erg.ErgTypes;
import miragefairy2019.modkt.api.fairystickcraft.ApiFairyStickCraft;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionReplaceBlock;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftConditionUseItem;
import miragefairy2019.modkt.impl.fairystickcraft.FairyStickCraftRecipe;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.function.Function;
import java.util.function.Supplier;

import static miragefairy2019.mod.api.composite.ApiComposite.instance;
import static miragefairy2019.mod.api.fairy.ApiFairy.getComponentAbilityType;
import static miragefairy2019.mod.lib.Configurator.*;

public class Loader {

    @SuppressWarnings("unused")
    public static void init(EventRegistryMod erMod) {

        // ミラジウムの斧
        Configurator<ItemMiragiumAxe> miragiumAxe = fairyWeapon(erMod, ItemMiragiumAxe::new, "miragium_axe", "miragiumAxe")
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.slash))))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.harvest))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // ロッドベース
        Configurator<ItemFairyWeaponBase> magicWandBase = fairyWeapon(erMod, ItemFairyWeaponBase::new, "magic_wand_base", "magicWandBase")
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.knowledge))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 光のロッド
        Configurator<ItemMagicWandLight> magicWandLight = fairyWeapon(erMod, ItemMagicWandLight::new, "light_magic_wand", "magicWandLight")
                .bind(addComponent(magicWandBase))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.light))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 収集のロッド
        Configurator<ItemMagicWandCollecting> magicWandCollecting = fairyWeapon(erMod, ItemMagicWandCollecting::new, "collecting_magic_wand", "magicWandCollecting")
                .bind(addComponent(magicWandBase))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.warp))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // ライトニングロッド
        Configurator<ItemMagicWandLightning> magicWandLightning = fairyWeapon(erMod, ItemMagicWandLightning::new, "lightning_magic_wand", "magicWandLightning")
                .bind(addComponent(magicWandBase))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.thunder))))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.energy))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // オカリナベース
        Configurator<ItemFairyWeaponBase> ocarinaBase = fairyWeapon(erMod, ItemFairyWeaponBase::new, "ocarina_base", "ocarinaBase")
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.sound))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 魅惑のオカリナ
        Configurator<ItemOcarinaTemptation> ocarinaTemptation = fairyWeapon(erMod, ItemOcarinaTemptation::new, "temptation_ocarina", "ocarinaTemptation")
                .bind(addComponent(ocarinaBase))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.life))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // 鐘ベース
        Configurator<ItemBellBase> bellBase = fairyWeapon(erMod, ItemBellBase::new, "bell_base", "bellBase")
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.sound))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // 花摘みの鐘
        Configurator<ItemBellFlowerPicking> bellFlowerPicking = fairyWeapon(erMod, () -> new ItemBellFlowerPicking(0.0, 0.0, 0.0, 0.0, 0.2), "flower_picking_bell", "bellFlowerPicking")
                .bind(addComponent(bellBase))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.harvest))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // 花摘みの鐘 II
        Configurator<ItemBellFlowerPicking> bellFlowerPicking2 = fairyWeapon(erMod, () -> new ItemBellFlowerPicking(10.0, 10.0, 10.0, 10.0, 1.0), "flower_picking_bell_2", "bellFlowerPicking2")
                .bind(addComponent(bellFlowerPicking))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.harvest))))
                .bind(setWeaponStatusOfTier(4))
                .get();

        // クリスマスの鐘
        Configurator<ItemBellChristmas> bellChristmas = fairyWeapon(erMod, ItemBellChristmas::new, "christmas_bell", "bellChristmas")
                .bind(addComponent(bellBase))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.christmas))))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.attack))))
                .bind(setWeaponStatusOfTier(3))
                .get();

        // ミラジウムの大鎌
        Configurator<ItemMiragiumScythe> miragiumScythe = fairyWeapon(erMod, ItemMiragiumScythe::new, "miragium_scythe", "miragiumScythe")
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.slash))))
                .bind(addComponent(instance(getComponentAbilityType(ErgTypes.harvest))))
                .bind(setWeaponStatusOfTier(2))
                .get();

        // レシピ登録
        erMod.addRecipe.register(() -> {

            // 丸石＞紅蓮→焼き石
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(Monad.of(new FairyStickCraftRecipe())
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionUseItem(new OreIngredient("mirageFairy2019CraftingToolFairyWandMelting"))))
                    .peek(FairyStickCraftRecipe.adderCondition(new FairyStickCraftConditionReplaceBlock(
                            () -> Blocks.COBBLESTONE.getDefaultState(),
                            () -> Blocks.STONE.getDefaultState())))
                    .get());

        });

    }

    //

    private static <I extends Item> Monad<Configurator<I>> fairyWeapon(EventRegistryMod erMod, Supplier<I> sItem, String registryName, String unlocalizedName) {
        return item(erMod, sItem, new ResourceLocation(ModMirageFairy2019.MODID, registryName), unlocalizedName)
                .bind(setCreativeTab(() -> ApiMain.creativeTab()))
                .bind(c -> {

                    c.erMod.registerItem.register(b -> {
                        if (ApiMain.side().isClient()) {

                            // 搭乗妖精の描画
                            MinecraftForge.EVENT_BUS.register(new Object() {
                                @SubscribeEvent
                                public void accept(ModelBakeEvent event) {
                                    IBakedModel bakedModel = event.getModelRegistry().getObject(new ModelResourceLocation(c.get().getRegistryName(), null));
                                    event.getModelRegistry().putObject(new ModelResourceLocation(c.get().getRegistryName(), null), new BakedModelBuiltinWrapper(bakedModel));
                                }
                            });

                            ModelLoader.setCustomModelResourceLocation(c.get(), 0, new ModelResourceLocation(c.get().getRegistryName(), null));

                        }
                    });

                    return Monad.of(c);
                });
    }

    private static <I extends Item> Function<Configurator<I>, Monad<Configurator<I>>> addOreName(String oreName) {
        return c -> Monad.of(c)
                .bind(onCreateItemStack(i -> OreDictionary.registerOre(oreName, new ItemStack(i, 1, OreDictionary.WILDCARD_VALUE))));

    }

    private static <I extends ItemFairyWeaponBase, I2 extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> addComponent(Configurator<I2> mci) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.addComponent(mci.get().getComposite())));
    }

    private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> addComponent(IComponentInstance componentInstance) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.addComponent(componentInstance)));
    }

    private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> setWeaponStatusOfTier(int tier) {
        return c -> Monad.of(c)
                .bind(onRegisterItem(i -> i.setMaxDamage(getDurability(tier) - 1)));
    }

    public static int getDurability(int tier) {
        if (tier == 1) return 32;
        if (tier == 2) return 64;
        if (tier == 3) return 128;
        if (tier == 4) return 256;
        throw new IllegalArgumentException("Illegal tier: " + tier);
    }

}
