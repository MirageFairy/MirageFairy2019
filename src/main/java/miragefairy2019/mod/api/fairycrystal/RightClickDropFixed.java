package miragefairy2019.mod.api.fairycrystal;

import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class RightClickDropFixed implements IRightClickDrop
{

	public final DropFixed drop;

	public RightClickDropFixed(ItemStack itemStack, double weight)
	{
		this.drop = new DropFixed(itemStack, weight);
	}

	@Override
	public Optional<IDrop> getDrop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return Optional.of(drop);
	}

}
