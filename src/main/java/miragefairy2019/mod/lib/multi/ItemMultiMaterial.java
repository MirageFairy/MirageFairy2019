package miragefairy2019.mod.lib.multi;

import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMultiMaterial<V extends ItemVariantMaterial> extends ItemMulti<V>
{

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getVariant(stack).map(v -> v.getUnlocalizedName()).orElse("item.null");
	}

	@SideOnly(Side.CLIENT)
	public void setCustomModelResourceLocations()
	{
		for (Tuple<Integer, V> tuple : getVariants()) {
			ModelLoader.setCustomModelResourceLocation(this,
				tuple.x,
				new ModelResourceLocation(new ResourceLocation(getRegistryName().getResourceDomain(), tuple.y.registryName), null));
		}
	}

}
