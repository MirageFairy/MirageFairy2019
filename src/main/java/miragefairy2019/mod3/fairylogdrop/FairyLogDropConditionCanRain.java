package miragefairy2019.mod3.fairylogdrop;

import miragefairy2019.mod3.fairylogdrop.api.IFairyLogDropCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyLogDropConditionCanRain implements IFairyLogDropCondition {

    @Override
    public boolean test(World world, BlockPos blockPos) {
        return world.getBiome(blockPos).canRain();
    }

    @Override
    public String getLocalizedDescription() {
        return "RAINY";
    }

}
