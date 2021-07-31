package miragefairy2019.mod.api.pickable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public interface IPickable {

    public Block getBlock();

    /**
     * {@link #tryPick(World, BlockPos, Optional, int)}が確実に失敗する場合にfalseを返します。
     */
    public boolean isPickableAge(IBlockState blockState);

    /**
     * ブロック状態にかかわらず右クリック時に呼び出されます。
     * メソッド内で{@link #isPickableAge(IBlockState)}を呼び出し、収穫可能ではない場合はfalseを返してください。
     */
    public boolean tryPick(World world, BlockPos blockPos, Optional<EntityPlayer> oPlayer, int fortune);

}
