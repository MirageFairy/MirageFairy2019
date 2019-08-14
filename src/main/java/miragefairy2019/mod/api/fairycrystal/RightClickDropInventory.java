package miragefairy2019.mod.api.fairycrystal;

import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RightClickDropInventory implements IRightClickDrop
{

	public final DropFixed drop;
	private Predicate<ItemStack> predicate;

	public RightClickDropInventory(ItemStack itemStack, double weight, Predicate<ItemStack> predicate)
	{
		this.drop = new DropFixed(itemStack, weight);
		this.predicate = predicate;
	}

	@Override
	public Optional<IDrop> getDrop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		for (ItemStack itemStack : player.inventory.mainInventory) {
			if (predicate.test(itemStack)) return Optional.of(drop);
		}
		for (ItemStack itemStack : player.inventory.armorInventory) {
			if (predicate.test(itemStack)) return Optional.of(drop);
		}
		for (ItemStack itemStack : player.inventory.offHandInventory) {
			if (predicate.test(itemStack)) return Optional.of(drop);
		}
		return Optional.empty();
	}

}
