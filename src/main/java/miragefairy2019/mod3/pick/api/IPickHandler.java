package miragefairy2019.mod3.pick.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IPickHandler {

    public Block getBlock();

    /**
     * このブロックステートにおいて{@link #tryPick(World, BlockPos, EntityPlayer, int)}が確実に失敗する場合にfalseを返します。
     */
    public boolean canPick(IBlockState blockState);

    /**
     * ブロック状態にかかわらず右クリック時に呼び出されます。
     * メソッド内で{@link #canPick(IBlockState)}を呼び出し、収穫可能ではない場合はfalseを返してください。
     */
    public boolean tryPick(World world, BlockPos blockPos, @Nullable EntityPlayer player, int fortune);

}
