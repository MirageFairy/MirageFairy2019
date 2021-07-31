package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import net.minecraft.block.state.IBlockState;

import java.util.Optional;
import java.util.function.Supplier;

public class ItemVariantFilledBucket extends ItemVariantMaterial {

    public final boolean vaporizable;
    public final Supplier<Optional<IBlockState>> soBlockStateFluid;

    public ItemVariantFilledBucket(String registryName, String unlocalizedName, boolean vaporizable, Supplier<Optional<IBlockState>> soBlockStateFluid) {
        super(registryName, unlocalizedName);
        this.vaporizable = vaporizable;
        this.soBlockStateFluid = soBlockStateFluid;
    }

}
