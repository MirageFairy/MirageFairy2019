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

public enum EnumVariantOre2 implements IStringSerializable, IBlockVariantOre {
    TOURMALINE_ORE(0, "tourmaline_ore", "oreTourmaline", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemTourmaline").copy(), 1, 0.5, 1, 5)),
    HELIOLITE_ORE(1, "heliolite_ore", "oreHeliolite", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemHeliolite").copy(), 1, 0.5, 10, 20)),
    END_STONE_LABRADORITE_ORE(2, "end_stone_labradorite_ore", "oreLabradorite", 3, 5, 2, new GemProvider(() -> UtilsMinecraft.getItemStack("gemLabradorite").copy(), 1, 0.5, 15, 30)),
    ;

    //

    public static final IListBlockVariant<EnumVariantOre2> variantList = new IListBlockVariant<EnumVariantOre2>() {
        private final EnumVariantOre2[] values = EnumVariantOre2.values();
        private final Map<Integer, EnumVariantOre2> metaLookup = new HashMap<>();

        {
            for (EnumVariantOre2 variant : EnumVariantOre2.values()) {
                if (metaLookup.containsKey(variant.metadata)) throw new IllegalArgumentException();
                metaLookup.put(variant.metadata, variant);
            }
        }

        @Override
        public EnumVariantOre2 byMetadata(int metadata) {
            return metaLookup.getOrDefault(metadata, values[0]);
        }

        @Override
        public Iterator<EnumVariantOre2> iterator() {
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

    private EnumVariantOre2(int metadata, String resourceName, String unlocalizedName, float hardness, float resistance, int harvestLevel, GemProvider nGemProvider) {
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
