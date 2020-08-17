package miragefairy2019.mod.modules.materialsfairy;

import java.util.List;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFairyStick extends Item
{

	public ItemFairyStick()
	{
		setMaxStackSize(1);
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public final void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		if (UtilsMinecraft.canTranslate(getUnlocalizedName() + ".poem")) {
			String string = UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".poem");
			if (!string.isEmpty()) {
				tooltip.add(string);
			}
		}

		// 機能
		tooltip.add(TextFormatting.RED + "Right Click: World Craft");

	}

	//

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		// TODO
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	//

	@Override
	public boolean hasContainerItem(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		return itemStack.copy();
	}

}
