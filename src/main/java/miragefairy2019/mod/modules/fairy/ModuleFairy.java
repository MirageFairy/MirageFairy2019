package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry;
import miragefairy2019.mod.api.fairy.relation.IIngredientFairyRelation;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.common.fairy.relation.FairyRelationRegistry;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.InitializationContext;
import miragefairy2019.mod.lib.OreIngredientComplex;
import miragefairy2019.modkt.api.erg.IErgEntry;
import mirrg.boron.util.UtilsString;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

import java.util.function.Consumer;

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
                    for (IErgEntry tuple : variant.y.get(i).getType().getAbilities().getEntries()) {
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

        // 妖精関係レジストリー
        erMod.createItemStack.register(ic -> {

            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockDiamond"), FairyTypes.instance.getDiamond().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockEmerald"), FairyTypes.instance.getEmerald().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockPyrope"), FairyTypes.instance.getPyrope().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockMoonstone"), FairyTypes.instance.getMoonstone().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockApatite"), FairyTypes.instance.getApatite().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("obsidian"), FairyTypes.instance.getObsidian().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockFluorite"), FairyTypes.instance.getFluorite().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockCinnabar"), FairyTypes.instance.getCinnabar().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockMagnetite"), FairyTypes.instance.getMagnetite().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("glowstone"), FairyTypes.instance.getGlowstone().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockSmithsonite"), FairyTypes.instance.getSmithsonite().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockLapis"), FairyTypes.instance.getLapislazuli().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockSulfur"), FairyTypes.instance.getSulfur().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockGold"), FairyTypes.instance.getGold().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockRedstone"), FairyTypes.instance.getRedstone().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(Ingredient.fromStacks(new ItemStack(Blocks.SAND)), FairyTypes.instance.getSand().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockNephrite"), FairyTypes.instance.getNephrite().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockTourmaline"), FairyTypes.instance.getTourmaline().get(0).getType().getBreed());
            ApiFairyRegistry.getFairyRelationRegistry().registerFairyRelationItemStack(new OreIngredient("blockTopaz"), FairyTypes.instance.getTopaz().get(0).getType().getBreed());
            // TODO ほとんどの妖精とアイテムの関連付けは妖精レジストリーを使う

        });

        // 妖精の関係を登録
        erMod.createItemStack.register(new Consumer<InitializationContext>() {
            @Override
            public void accept(InitializationContext ic) {
                r(1, FairyTypes.instance.getStone().get(0), i(Blocks.STONE, 0));
                r(1, FairyTypes.instance.getDirt().get(0), i(Blocks.DIRT, 0));
                r(1, FairyTypes.instance.getIron().get(0), i("ingotIron"));
                r(1, FairyTypes.instance.getDiamond().get(0), i("gemDiamond"));
                r(1, FairyTypes.instance.getMagnetite().get(0), i("gemMagnetite"));
                r(1, FairyTypes.instance.getApatite().get(0), i("gemApatite"));
                r(1, FairyTypes.instance.getFluorite().get(0), i("gemFluorite"));
                r(1, FairyTypes.instance.getSulfur().get(0), i("gemSulfur"));
                r(1, FairyTypes.instance.getCinnabar().get(0), i("gemCinnabar"));
                r(1, FairyTypes.instance.getMoonstone().get(0), i("gemMoonstone"));
                r(1, FairyTypes.instance.getPyrope().get(0), i("gemPyrope"));
                r(1, FairyTypes.instance.getSmithsonite().get(0), i("gemSmithsonite"));
                r(1, FairyTypes.instance.getRedstone().get(0), i("dustRedstone"));
                r(1, FairyTypes.instance.getSand().get(0), i(Blocks.SAND));
                r(1, FairyTypes.instance.getGold().get(0), i("ingotGold"));
                r(1, FairyTypes.instance.getWheat().get(0), i(Items.WHEAT));
                r(1, FairyTypes.instance.getLilac().get(0), i(Blocks.DOUBLE_PLANT, 1));
                r(1, FairyTypes.instance.getTorch().get(0), i(Blocks.TORCH));
                r(1, FairyTypes.instance.getGravel().get(0), i(Blocks.GRAVEL));
                r(1, FairyTypes.instance.getEmerald().get(0), i("gemEmerald"));
                r(1, FairyTypes.instance.getLapislazuli().get(0), i("gemLapislazuli"));
                r(1, FairyTypes.instance.getFurnace().get(0), i(Blocks.FURNACE));
                r(1, FairyTypes.instance.getMagentaglazedterracotta().get(0), i(Blocks.MAGENTA_GLAZED_TERRACOTTA));
                r(1, FairyTypes.instance.getBread().get(0), i(Items.BREAD));
                r(1, FairyTypes.instance.getApple().get(0), i(Items.APPLE));
                r(1, FairyTypes.instance.getCarrot().get(0), i(Items.CARROT));
                r(1, FairyTypes.instance.getCactus().get(0), i(Blocks.CACTUS));
                r(1, FairyTypes.instance.getAxe().get(0), i(Items.IRON_AXE));
                r(1, FairyTypes.instance.getChest().get(0), i(Blocks.CHEST));
                r(1, FairyTypes.instance.getCraftingtable().get(0), i(Blocks.CRAFTING_TABLE));
                r(1, FairyTypes.instance.getPotion().get(0), i(Items.POTIONITEM));
                r(1, FairyTypes.instance.getSword().get(0), i(Items.IRON_SWORD));
                r(1, FairyTypes.instance.getDispenser().get(0), i(Blocks.DISPENSER));
                r(1, FairyTypes.instance.getCod().get(0), i(Items.FISH, 0));
                r(1, FairyTypes.instance.getSalmon().get(0), i(Items.FISH, 1));
                r(1, FairyTypes.instance.getPufferfish().get(0), i(Items.FISH, 3));
                r(1, FairyTypes.instance.getClownfish().get(0), i(Items.FISH, 2));
                r(1, FairyTypes.instance.getSpruce().get(0), i(Blocks.LOG, 1), i(Blocks.SAPLING, 1));
                r(1, FairyTypes.instance.getAnvil().get(0), i(Blocks.ANVIL));
                r(1, FairyTypes.instance.getObsidian().get(0), i(Blocks.OBSIDIAN));
                r(1, FairyTypes.instance.getSeed().get(0), i(Items.WHEAT_SEEDS));
                r(1, FairyTypes.instance.getGlowstone().get(0), i(Items.GLOWSTONE_DUST), i(Blocks.GLOWSTONE));
                r(1, FairyTypes.instance.getCoal().get(0), i(Items.COAL, 0));
                r(1, FairyTypes.instance.getNetherstar().get(0), i(Items.NETHER_STAR));
                r(1, FairyTypes.instance.getBrewingstand().get(0), i(Items.BREWING_STAND));
                r(1, FairyTypes.instance.getHoe().get(0), i(Items.IRON_HOE));
                r(1, FairyTypes.instance.getShield().get(0), i(Items.SHIELD));
                r(1, FairyTypes.instance.getHopper().get(0), i(Blocks.HOPPER));
                r(1, FairyTypes.instance.getNephrite().get(0), i("gemNephrite"));
                r(1, FairyTypes.instance.getTourmaline().get(0), i("gemTourmaline"));
                r(1, FairyTypes.instance.getTopaz().get(0), i("gemTopaz"));
                r(1, FairyTypes.instance.getCookie().get(0), i(Items.COOKIE));
                r(1, FairyTypes.instance.getCake().get(0), i(Items.CAKE));
                r(1, FairyTypes.instance.getEnchantedgoldenapple().get(0), i(Items.GOLDEN_APPLE, 1));
                r(1, FairyTypes.instance.getSugar().get(0), i(Items.SUGAR));
                r(1, FairyTypes.instance.getRottenflesh().get(0), i(Items.ROTTEN_FLESH));
                r(1, FairyTypes.instance.getPoisonouspotato().get(0), i(Items.POISONOUS_POTATO));
                r(1, FairyTypes.instance.getMelon().get(0), i(Items.MELON));
                r(1, FairyTypes.instance.getBakedpotato().get(0), i(Items.BAKED_POTATO));
                r(1, FairyTypes.instance.getCookedchicken().get(0), i(Items.COOKED_CHICKEN));
                r(1, FairyTypes.instance.getCookedsalmon().get(0), i(Items.COOKED_FISH, 1));
                r(1, FairyTypes.instance.getSteak().get(0), i(Items.COOKED_BEEF));
                r(1, FairyTypes.instance.getGoldenapple().get(0), i(Items.GOLDEN_APPLE, 0));

            }

            private Ingredient i(Item item) {
                return i(item, 32767);
            }

            private Ingredient i(Item item, int meta) {
                return Ingredient.fromStacks(new ItemStack(item, 1, meta));
            }

            private Ingredient i(Block block) {
                return i(block, 32767);
            }

            private Ingredient i(Block block, int meta) {
                return Ingredient.fromStacks(new ItemStack(Item.getItemFromBlock(block), 1, meta));
            }

            @SuppressWarnings("unused")
            private Ingredient i(ItemStack itemStack) {
                return Ingredient.fromStacks(itemStack);
            }

            private Ingredient i(String ore) {
                return new OreIngredient(ore);
            }

            private void r(double relevance, VariantFairy variantFairy, Ingredient... ingredients) {
                for (Ingredient ingredient : ingredients) {
                    ApiFairy.fairyRelationRegistry.registerIngredientFairyRelation(relevance, variantFairy.createItemStack(), ingredient);
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
