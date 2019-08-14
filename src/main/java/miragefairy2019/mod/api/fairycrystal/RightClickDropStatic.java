package miragefairy2019.mod.api.fairycrystal;

import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RightClickDropStatic implements IRightClickDrop
{

	public final Drop drop;

	public RightClickDropStatic(ItemStack itemStack, double weight)
	{
		this.drop = new Drop(itemStack, weight);
	}

	@Override
	public Optional<IDrop> getDrop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return Optional.of(drop);
	}

	public static class Drop implements IDrop
	{

		public final ItemStack itemStack;
		public final double weight;

		public Drop(ItemStack itemStack, double weight)
		{
			this.itemStack = itemStack;
			this.weight = weight;
		}

		@Override
		public ItemStack getItemStack()
		{
			return itemStack;
		}

		@Override
		public double getWeight()
		{
			return weight;
		}

	}

}
