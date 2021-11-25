package miragefairy2019.mod3.fairylogdrop;

import miragefairy2019.mod3.fairylogdrop.api.IFairyLogDropCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

public class FairyLogDropConditionOverworld implements IFairyLogDropCondition {

    @Override
    public boolean test(World world, BlockPos blockPos) {
        if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.NETHER)) return false;
        if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.END)) return false;
        return true;
    }

    @Override
    public String getLocalizedDescription() {
        return "OVERWORLD";
    }

}
