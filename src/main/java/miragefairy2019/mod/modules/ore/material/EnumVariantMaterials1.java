package miragefairy2019.mod.modules.ore.material;

import miragefairy2019.mod.lib.multi.IListBlockVariant;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public enum EnumVariantMaterials1 implements IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", 3, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", 5, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON),
    SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", 3, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", 5, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON),
    MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", 7, "pickaxe", 3, 0, SoundType.STONE, false, Material.IRON),
    MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", 3, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", 5, "pickaxe", 2, 0, SoundType.STONE, false, Material.IRON),
    SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", 3, "pickaxe", 1, 0, SoundType.STONE, false, Material.IRON),
    CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", 5, "pickaxe", 0, 20 * 10 * 8 * 9, SoundType.STONE, false, Material.ROCK),
    MIRAGE_FLOWER_LEAF_BLOCK(9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", 2, "axe", 0, 0, SoundType.GLASS, false, Material.LEAVES),
    MIRAGIUM_INGOT_BLOCK(10, "miragium_ingot_block", "blockMiragium", "blockMiragium", 5, "pickaxe", 1, 0, SoundType.METAL, false, Material.IRON),
    MIRAGIUM_DUST_BLOCK(11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", 0.5f, "shovel", 0, 0, SoundType.SNOW, true, Material.SAND),
    ;

    //

    public static final IListBlockVariant<EnumVariantMaterials1> variantList = new IListBlockVariant<EnumVariantMaterials1>() {
        private final EnumVariantMaterials1[] values = EnumVariantMaterials1.values();
        private final Map<Integer, EnumVariantMaterials1> metaLookup = new HashMap<>();

        {
            for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
                if (metaLookup.containsKey(variant.metadata)) throw new IllegalArgumentException();
                metaLookup.put(variant.metadata, variant);
            }
        }

        @Override
        public EnumVariantMaterials1 byMetadata(int metadata) {
            return metaLookup.getOrDefault(metadata, values[0]);
        }

        @Override
        public Iterator<EnumVariantMaterials1> iterator() {
            return ISuppliterator.ofObjArray(values).iterator();
        }
    };

    //

    public final int metadata;
    public final String resourceName;
    public final String unlocalizedName;
    public final String oreName;
    public final float blockHardness;
    public final String harvestTool;
    public final int harvestLevel;
    public final int burnTime;
    public final SoundType soundType;
    public final boolean fallable;
    public final Material material;

    private EnumVariantMaterials1(int metadata, String resourceName, String unlocalizedName, String oreName, float blockHardness, String harvestTool, int harvestLevel, int burnTime, SoundType soundType, boolean fallable, Material material) {
        this.metadata = metadata;
        this.resourceName = resourceName;
        this.unlocalizedName = unlocalizedName;
        this.oreName = oreName;
        this.blockHardness = blockHardness;
        this.harvestTool = harvestTool;
        this.harvestLevel = harvestLevel;
        this.burnTime = burnTime;
        this.soundType = soundType;
        this.fallable = fallable;
        this.material = material;
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
    public float getBlockHardness() {
        return blockHardness;
    }

    @Override
    public String getHarvestTool() {
        return harvestTool;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public SoundType getSoundType() {
        return soundType;
    }

    @Override
    public boolean isFallable() {
        return fallable;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

}
