package miragefairy2019.mod.common.fairystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEventBus;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraft
{

	private final Optional<EntityPlayer> oPlayer;
	private final World world;
	private final BlockPos blockPos;
	private final ItemStack itemStackFairyStick;

	private List<EntityItem> entitiesItemRemaining = new ArrayList<>();

	public FairyStickCraft(Optional<EntityPlayer> oPlayer, World world, BlockPos blockPos, ItemStack itemStackFairyStick, List<EntityItem> entitiesItem)
	{
		this.oPlayer = oPlayer;
		this.world = world;
		this.blockPos = blockPos;
		this.itemStackFairyStick = itemStackFairyStick;

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
				return blockPos;
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

	//

	private List<Runnable> listenersOnCraft = new ArrayList<>();
	private List<Runnable> listenersOnUpdate = new ArrayList<>();

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
