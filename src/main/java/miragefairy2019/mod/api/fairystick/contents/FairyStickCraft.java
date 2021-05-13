package miragefairy2019.mod.api.fairystick.contents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEventBus;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraft
{

	private final Optional<EntityPlayer> oPlayer;
	private final BlockPos pos;
	private final ItemStack itemStackFairyStick;
	private final World world;

	private List<EntityItem> entitiesItemRemaining = new ArrayList<>();

	private List<Runnable> listenersOnCraft = new ArrayList<>();
	private List<Runnable> listenersOnUpdate = new ArrayList<>();

	public FairyStickCraft(Optional<EntityPlayer> oPlayer, BlockPos pos, ItemStack itemStackFairyStick, World world, List<EntityItem> entitiesItem)
	{
		this.oPlayer = oPlayer;
		this.pos = pos;
		this.itemStackFairyStick = itemStackFairyStick;
		this.world = world;

		this.entitiesItemRemaining.addAll(entitiesItem);
	}

	public IFairyStickCraftEnvironment getEnvironment()
	{
		return new IFairyStickCraftEnvironment() {
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
			public BlockPos getBlockPos()
			{
				return pos;
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
		};
	}

	public IFairyStickCraftEventBus getEventBus()
	{
		return new IFairyStickCraftEventBus() {
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
		};
	}

	public IFairyStickCraftExecutor getExecutor()
	{
		return new IFairyStickCraftExecutor() {
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
		};
	}

}
