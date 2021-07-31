package miragefairy2019.mod.modules.ore.material;

import miragefairy2019.mod.lib.multi.IBlockVariant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public interface IBlockVariantMaterials extends IBlockVariant {

    public float getBlockHardness();

    public String getHarvestTool();

    public int getHarvestLevel();

    public int getBurnTime();

    public SoundType getSoundType();

    public boolean isFallable();

    public Material getMaterial();

}
