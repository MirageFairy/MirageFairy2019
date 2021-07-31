package miragefairy2019.mod.modules.ore.ore;

import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.multi.IListBlockVariant;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public enum EnumVariantOre1 implements IStringSerializable, IBlockVariantOre {
    APATITE_ORE(0, "apatite_ore", "oreApatite", 3, 5, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemApatite").copy(), 1, 1.5, 1, 3)),
    FLUORITE_ORE(1, "fluorite_ore", "oreFluorite", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemFluorite").copy(), 1, 1, 15, 30)),
    SULFUR_ORE(2, "sulfur_ore", "oreSulfur", 3, 5, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemSulfur").copy(), 1, 1.5, 1, 3)),
    CINNABAR_ORE(3, "cinnabar_ore", "oreCinnabar", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemCinnabar").copy(), 1, 1, 1, 3)),
    MOONSTONE_ORE(4, "moonstone_ore", "oreMoonstone", 3, 5, 3, new GemProvider(() -> UtilsMinecraft.getItemStack("gemMoonstone").copy(), 1, 0.5, 20, 40)),
    MAGNETITE_ORE(5, "magnetite_ore", "oreMagnetite", 3, 5, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemMagnetite").copy(), 1, 2, 1, 2)),

    PYROPE_ORE(6, "pyrope_ore", "orePyrope", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemPyrope").copy(), 1, 0.5, 1, 5)),
    SMITHSONITE_ORE(7, "smithsonite_ore", "oreSmithsonite", 3, 5, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemSmithsonite").copy(), 1, 1, 1, 3)),

    NETHERRACK_APATITE_ORE(8, "netherrack_apatite_ore", "oreApatite", 0.4F, 0.4F, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemApatite").copy(), 1, 1.5, 1, 3)),
    NETHERRACK_FLUORITE_ORE(9, "netherrack_fluorite_ore", "oreFluorite", 0.4F, 0.4F, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemFluorite").copy(), 1, 1, 15, 30)),
    NETHERRACK_SULFUR_ORE(10, "netherrack_sulfur_ore", "oreSulfur", 0.4F, 0.4F, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemSulfur").copy(), 1, 1.5, 1, 3)),
    NETHERRACK_CINNABAR_ORE(11, "netherrack_cinnabar_ore", "oreCinnabar", 0.4F, 0.4F, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemCinnabar").copy(), 1, 1, 1, 3)),
    NETHERRACK_MOONSTONE_ORE(12, "netherrack_moonstone_ore", "oreMoonstone", 0.4F, 0.4F, 3, new GemProvider(() -> UtilsMinecraft.getItemStack("gemMoonstone").copy(), 1, 0.5, 20, 40)),
    NETHERRACK_MAGNETITE_ORE(13, "netherrack_magnetite_ore", "oreMagnetite", 0.4F, 0.4F, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemMagnetite").copy(), 1, 2, 1, 2)),

    NEPHRITE_ORE(14, "nephrite_ore", "oreNephrite", 3, 5, 1, new GemProvider(() -> UtilsMinecraft.getItemStack("gemNephrite").copy(), 1, 2, 1, 3)),
    TOPAZ_ORE(15, "topaz_ore", "oreTopaz", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemTopaz").copy(), 1, 0.5, 1, 5)),

    ;

    //

    public static final IListBlockVariant<EnumVariantOre1> variantList = new IListBlockVariant<EnumVariantOre1>() {
        private final EnumVariantOre1[] values = EnumVariantOre1.values();
        private final Map<Integer, EnumVariantOre1> metaLookup = new HashMap<>();

        {
            for (EnumVariantOre1 variant : EnumVariantOre1.values()) {
                if (metaLookup.containsKey(variant.metadata)) throw new IllegalArgumentException();
                metaLookup.put(variant.metadata, variant);
            }
        }

        @Override
        public EnumVariantOre1 byMetadata(int metadata) {
            return metaLookup.getOrDefault(metadata, values[0]);
        }

        @Override
        public Iterator<EnumVariantOre1> iterator() {
            return ISuppliterator.ofObjArray(values).iterator();
        }
    };

    //

    public final int metadata;
    public final String resourceName;
    public final String unlocalizedName;
    public final float hardness;
    public final float resistance;
    public final int harvestLevel;
    public final GemProvider nGemProvider;

    private static class GemProvider {

        private final Supplier<ItemStack> sItemStack;
        private final double amount;
        private final double amountPerFortune;
        private final int expMin;
        private final int expMax;

        public GemProvider(Supplier<ItemStack> sItemStack, double amount, double amountPerFortune, int expMin, int expMax) {
            this.sItemStack = sItemStack;
            this.amount = amount;
            this.amountPerFortune = amountPerFortune;
            this.expMin = expMin;
            this.expMax = expMax;
        }

    }

    private EnumVariantOre1(int metadata, String resourceName, String unlocalizedName, float hardness, float resistance, int harvestLevel, GemProvider nGemProvider) {
        this.metadata = metadata;
        this.resourceName = resourceName;
        this.unlocalizedName = unlocalizedName;
        this.hardness = hardness;
        this.resistance = resistance;
        this.harvestLevel = harvestLevel;
        this.nGemProvider = nGemProvider;
    }

    @Override
    public String toString() {
        return this.resourceName;
    }

    @Override
    public String getName() {
        return this.resourceName;
    }

    @Override
    public int getMetadata() {
        return metadata;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    @Override
    public float getHardness() {
        return hardness;
    }

    @Override
    public float getResistance() {
        return resistance;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, Random random, Block block, int metadata, int fortune) {
        if (nGemProvider != null) {
            int count = UtilsMath.randomInt(random, nGemProvider.amount + random.nextDouble() * nGemProvider.amountPerFortune * fortune);
            for (int i = 0; i < count; i++) {
                drops.add(nGemProvider.sItemStack.get());
            }
        } else {
            IBlockVariantOre.super.getDrops(drops, random, block, metadata, fortune);
        }
    }

    @Override
    public int getExpDrop(Random random, int fortune) {
        if (nGemProvider != null) {
            return MathHelper.getInt(random, nGemProvider.expMin, nGemProvider.expMax);
        } else {
            return IBlockVariantOre.super.getExpDrop(random, fortune);
        }
    }

}
