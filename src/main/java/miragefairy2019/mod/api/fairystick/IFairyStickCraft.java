package miragefairy2019.mod.api.fairystick;

import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFairyStickCraft
{

	public Optional<EntityPlayer> getPlayer();

	public World getWorld();

	public IBlockState getBlockState();

	public BlockPos getPos();

	public ItemStack getItemStackFairyStick();

	public Optional<EntityItem> pullItem(Predicate<ItemStack> ingredient);

	public void hookOnCraft(Runnable listener);

	public void hookOnUpdate(Runnable listener);

	public void onCraft();

	public void onUpdate();

}
