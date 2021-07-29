package miragefairy2019.mod.modules.fertilizer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class ItemFertilizer extends Item
{

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(blockPos.offset(facing), facing, itemStack)) {
			return EnumActionResult.FAIL;
		}

		if (ItemDye.applyBonemeal(itemStack, world, blockPos, player, hand)) {
			if (!world.isRemote) {
				world.playEvent(2005, blockPos, 0);
			}
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

}
