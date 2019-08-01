package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.multi.ItemMulti;
import net.minecraft.item.ItemStack;

public class ItemMirageSpheres extends ItemMulti<VariantAbility>
{

	@Override
	public String getItemStackDisplayName(ItemStack itemStack)
	{
		VariantAbility variant = getVariant(itemStack).orElse(null);
		if (variant == null) return UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".name").trim();
		return UtilsMinecraft.translateToLocalFormatted(getUnlocalizedName() + ".format", variant.abilityType.getLocalizedName()).trim();
	}

}
