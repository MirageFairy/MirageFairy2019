package miragefairy2019.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPickHandler {

    /**
     * @return 対象ブロックがこのPickHandlerでは取り扱うことができない場合、null。
     */
    @Nullable
    public IPickExecutor getExecutor(@Nonnull World world, @Nonnull BlockPos blockPos, @Nullable EntityPlayer player);

}
