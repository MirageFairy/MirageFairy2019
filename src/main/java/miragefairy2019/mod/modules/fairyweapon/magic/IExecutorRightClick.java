package miragefairy2019.mod.modules.fairyweapon.magic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public interface IExecutorRightClick
{

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand);

	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected);

}
