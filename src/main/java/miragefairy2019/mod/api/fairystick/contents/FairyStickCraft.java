package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftResult;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraft implements IFairyStickCraft, IFairyStickCraftResult
{

	private final Optional<EntityPlayer> oPlayer;
	private final BlockPos pos;
	private final IBlockState blockState;
	private final ItemStack itemStackFairyStick;
	private final World world;

	private List<EntityItem> entitiesItemRemaining = new ArrayList<>();

	private List<Runnable> listenersOnCraft = new ArrayList<>();
	private List<Runnable> listenersOnUpdate = new ArrayList<>();

	public FairyStickCraft(Optional<EntityPlayer> oPlayer, BlockPos pos, IBlockState blockState, ItemStack itemStackFairyStick, World world, List<EntityItem> entitiesItem)
	{
		this.oPlayer = oPlayer;
		this.pos = pos;
		this.blockState = blockState;
		this.itemStackFairyStick = itemStackFairyStick;
		this.world = world;

		this.entitiesItemRemaining.addAll(entitiesItem);
	}

	@Override
	public Optional<EntityPlayer> getPlayer()
	{
		return oPlayer;
	}

	@Override
	public World getWorld()
	{
		return world;
	}

	@Override
	public BlockPos getPos()
	{
		return pos;
	}

	@Override
	public IBlockState getBlockState()
	{
		return blockState;
	}

	@Override
	public ItemStack getItemStackFairyStick()
	{
		return itemStackFairyStick;
	}

	@Override
	public Optional<EntityItem> pullItem(Predicate<ItemStack> ingredient)
	{
		Iterator<EntityItem> iterator = entitiesItemRemaining.iterator();
		while (iterator.hasNext()) {
			EntityItem entity = iterator.next();

			if (ingredient.test(entity.getItem())) {
				iterator.remove();
				return Optional.of(entity);
			}

		}
		return Optional.empty();
	}

	@Override
	public void hookOnCraft(Runnable listener)
	{
		listenersOnCraft.add(listener);
	}

	@Override
	public void hookOnUpdate(Runnable listener)
	{
		listenersOnUpdate.add(listener);
	}

	@Override
	public void onCraft()
	{
		listenersOnCraft.forEach(Runnable::run);
	}

	@Override
	public void onUpdate()
	{
		listenersOnUpdate.forEach(Runnable::run);
	}

}
