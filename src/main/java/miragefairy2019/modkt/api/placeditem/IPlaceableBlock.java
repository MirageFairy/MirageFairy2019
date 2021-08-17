package miragefairy2019.modkt.api.placeditem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IPlaceableBlock {

    /**
     * @param consumer
     * @param supplier 手に持っているアイテムをスプリットして新しいItemStackインスタンスで返す。
     *                 ただし何も持っていない場合はItemStack.EMPTYをcopyして返す。
     * @return
     */
    public boolean doPlaceAction(EntityPlayer player, World world, BlockPos blockPos, Consumer<ItemStack> consumer, Supplier<ItemStack> supplier);

}
