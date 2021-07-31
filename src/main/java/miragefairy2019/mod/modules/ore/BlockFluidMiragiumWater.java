package miragefairy2019.mod.modules.ore;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMiragiumWater extends BlockFluidClassic {

    protected BlockFluidMiragiumWater(Fluid fluid) {
        super(fluid, Material.WATER);
        setHardness(100.0f);
        setLightOpacity(3);
    }

}
