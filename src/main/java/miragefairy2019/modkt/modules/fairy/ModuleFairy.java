package miragefairy2019.modkt.modules.fairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.relation.IIngredientFairyRelation;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.common.fairy.relation.FairyRelationRegistry;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.OreIngredientComplex;
import miragefairy2019.modkt.api.erg.IErgEntry;
import mirrg.boron.util.UtilsString;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModuleFairy {

    public static CreativeTabs creativeTab;

    public static ItemFairy[] itemFairyList;

    public static ItemStack itemStackFairyMain;

    public static void init(EventRegistryMod erMod) {

        erMod.initRegistry.register(() -> {
            ApiFairy.fairyRelationRegistry = new FairyRelationRegistry();
        });

        erMod.initCreativeTab.register(() -> {
            creativeTab = new CreativeTabs("mirageFairy2019.fairy") {
                @Override
                @SideOnly(Side.CLIENT)
                public ItemStack getTabIconItem() {
                    return itemStackFairyMain;
                }

                @Override
                @SideOnly(value = Side.CLIENT)
                public void displayAllRelevantItems(NonNullList<ItemStack> itemStacks) {
                    for (Tuple<Integer, RankedFairyTypeBundle> variant : FairyTypes.instance.getVariants()) {
                        for (int i = 0; i < itemFairyList.length; i++) {
                            itemStacks.add(variant.y.get(i).createItemStack());
                        }
                    }
                }
            };
        });
        erMod.registerItem.register(b -> {

            // 妖精タイプ登録
            FairyTypes.instance = new FairyTypes(7);

            itemFairyList = new ItemFairy[7];

            for (int i = 0; i < itemFairyList.length; i++) {

                // 妖精
                itemFairyList[i] = new ItemFairy();
                itemFairyList[i].setRegistryName(ModMirageFairy2019.MODID, i == 0 ? "mirage_fairy" : "mirage_fairy_r" + (i + 1));
                itemFairyList[i].setUnlocalizedName("mirageFairyR" + (i + 1));
                itemFairyList[i].setCreativeTab(creativeTab);
                for (Tuple<Integer, RankedFairyTypeBundle> tuple : FairyTypes.instance.getVariants()) {
                    itemFairyList[i].registerVariant(tuple.x, tuple.y.get(i));
                }
                ForgeRegistries.ITEMS.register(itemFairyList[i]);
                if (ApiMain.side().isClient()) {
                    for (Tuple<Integer, VariantFairy> tuple : itemFairyList[i].getVariants()) {
                        ModelLoader.setCustomModelResourceLocation(itemFairyList[i], tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "fairy"), null));
                    }
                }

            }

        });
        erMod.registerItemColorHandler.register(new Runnable() {
            @SideOnly(Side.CLIENT)
            @Override
            public void run() {

                // 妖精のカスタム色
                {
                    @SideOnly(Side.CLIENT)
                    class ItemColorImpl implements IItemColor {

                        private final ItemFairy itemMirageFairy;
                        private final int colorCloth;

                        public ItemColorImpl(ItemFairy itemMirageFairy, int colorCloth) {
                            this.itemMirageFairy = itemMirageFairy;
                            this.colorCloth = colorCloth;
                        }

                        @Override
                        public int colorMultiplier(ItemStack stack, int tintIndex) {
                            VariantFairy variant = itemMirageFairy.getVariant(stack).orElse(null);
                            if (variant == null) return 0xFFFFFF;
                            if (tintIndex == 0) return variant.getColorSet().getSkin();
                            if (tintIndex == 1) return colorCloth;
                            if (tintIndex == 2) return variant.getColorSet().getDark();
                            if (tintIndex == 3) return variant.getColorSet().getBright();
                            if (tintIndex == 4) return variant.getColorSet().getHair();
                            return 0xFFFFFF;
                        }

                    }
                    for (int i = 0; i < itemFairyList.length; i++) {
                        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemFairyList[i], new int[]{
                                0xFF8888,
                                0x8888FF,
                                0x88FF88,
                                0xFFFF88,
                                0x111111,
                                0xFFFFFF,
                                0x88FFFF,
                        }[i]), itemFairyList[i]);
                    }
                }

            }
        });
        erMod.createItemStack.register(ic -> {

            itemStackFairyMain = FairyTypes.instance.getMagentaglazedterracotta().get(0).createItemStack();

            // 妖精の鉱石辞書
            for (Tuple<Integer, RankedFairyTypeBundle> variant : FairyTypes.instance.getVariants()) {
                for (int i = 0; i <= itemFairyList.length - 1; i++) {
                    OreDictionary.registerOre(
                            "mirageFairy2019Fairy" + UtilsString.toUpperCaseHead(variant.y.get(i).getType().getBreed().getResourcePath()) + "Rank" + (i + 1),
                            variant.y.get(i).createItemStack());
                    for (IErgEntry tuple : variant.y.get(i).getType().getErgSet().getEntries()) {
                        if (tuple.getPower() >= 10) {
                            OreDictionary.registerOre(
                                    "mirageFairy2019FairyAbility" + UtilsString.toUpperCaseHead(tuple.getType().getName()),
                                    variant.y.get(i).createItemStack());
                        }
                    }
                }
            }

        });
        erMod.addRecipe.register(() -> {

            // 凝縮・分散レシピ
            for (Tuple<Integer, RankedFairyTypeBundle> tuple : FairyTypes.instance.getVariants()) {

                for (int i = 0; i < itemFairyList.length - 1; i++) {

                    // 凝縮
                    GameRegistry.addShapelessRecipe(
                            new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i + "_fairy_" + tuple.y.get(i).getType().getBreed().getResourcePath()),
                            new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i + "_fairy_" + tuple.y.get(i).getType().getBreed().getResourcePath()),
                            tuple.y.get(i + 1).createItemStack(),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()),
                            Ingredient.fromStacks(tuple.y.get(i).createItemStack()));

                    // 分散
                    GameRegistry.addShapelessRecipe(
                            new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i + "_fairy_" + tuple.y.get(i).getType().getBreed().getResourcePath()),
                            new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i + "_fairy_" + tuple.y.get(i).getType().getBreed().getResourcePath()),
                            tuple.y.get(i).createItemStack(8),
                            Ingredient.fromStacks(tuple.y.get(i + 1).createItemStack()));

                }

            }

        });

        // 妖精の確定レシピ
        erMod.addRecipe.register(() -> {
            int counter = 0;
            for (IIngredientFairyRelation relation : ApiFairy.fairyRelationRegistry.getIngredientFairyRelations()) {
                if (relation.getRelevance() >= 1) {

                    GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(
                            new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_item_" + counter),
                            relation.getItemStackFairy(),
                            new OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
                            new OreIngredient("mirageFairyCrystal"),
                            relation.getIngredient()).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_item_" + counter));
                    counter++;

                    GameRegistry.findRegistry(IRecipe.class).register(new ShapelessOreRecipe(
                            new ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_summoning_fairy_wand_" + counter),
                            relation.getItemStackFairy(),
                            new OreIngredient("gemDiamond"),
                            new OreIngredient("mirageFairyCrystal"),
                            relation.getIngredient()).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_summoning_fairy_wand_" + counter));
                    counter++;

                }
            }
        });

    }

}
