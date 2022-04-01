package miragefairy2019.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface IPlaceAcceptorBlock {

    public boolean place(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull EntityPlayer player, @Nonnull IPlaceExchanger placeExchanger);

}
