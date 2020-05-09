package miragefairy2019.mod.modules.sphere;

import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.multi.ItemMulti;
import net.minecraft.item.ItemStack;

public class ItemSpheres extends ItemMulti<VariantSphere>
{

	@Override
	public String getItemStackDisplayName(ItemStack itemStack)
	{
		VariantSphere variant = getVariant(itemStack).orElse(null);
		if (variant == null) return UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".name");
		return UtilsMinecraft.translateToLocalFormatted(getUnlocalizedName() + ".format", variant.sphere.abilityType.getDisplayName().getFormattedText());
	}

}
