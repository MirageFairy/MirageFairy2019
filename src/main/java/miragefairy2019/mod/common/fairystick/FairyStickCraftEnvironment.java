package miragefairy2019.mod.common.fairystick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraftEnvironment implements IFairyStickCraftEnvironment
{

	private final Optional<EntityPlayer> oPlayer;
	private final World world;
	private final BlockPos blockPos;
	private final Supplier<ItemStack> getterItemStackFairyStick;

	private List<EntityItem> entitiesItemRemaining = new ArrayList<>();

	public FairyStickCraftEnvironment(Optional<EntityPlayer> oPlayer, World world, BlockPos blockPos, Supplier<ItemStack> getterItemStackFairyStick)
	{
		this.oPlayer = oPlayer;
		this.world = world;
		this.blockPos = blockPos;
		this.getterItemStackFairyStick = getterItemStackFairyStick;

		entitiesItemRemaining.addAll(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(blockPos).grow(1)));
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
	public BlockPos getBlockPos()
	{
		return blockPos;
	}

	@Override
	public ItemStack getItemStackFairyStick()
	{
		return getterItemStackFairyStick.get();
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

}
