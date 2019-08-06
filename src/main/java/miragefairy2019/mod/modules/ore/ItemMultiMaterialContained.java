package miragefairy2019.mod.modules.ore;

import java.util.List;

import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMultiMaterialContained<V extends ItemVariantMaterialContained> extends ItemMultiMaterial<V>
{

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		V variant = getVariant(itemStack).orElse(null);
		if (variant != null) {
			tooltip.add(TextFormatting.YELLOW + "Contains: " + variant.contains);
		}
	}

}
