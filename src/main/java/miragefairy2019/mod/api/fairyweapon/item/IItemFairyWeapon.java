package miragefairy2019.mod.api.fairyweapon.item;

import java.util.List;

import miragefairy2019.mod.api.fairy.IFairyType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemFairyWeapon
{

	@SideOnly(Side.CLIENT)
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag);

	public ITextComponent getFairyMagicDisplayName(ItemStack itemStack);

}
