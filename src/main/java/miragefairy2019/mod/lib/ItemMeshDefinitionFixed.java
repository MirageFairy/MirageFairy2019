package miragefairy2019.mod.lib;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemMeshDefinitionFixed implements ItemMeshDefinition
{

	private ModelResourceLocation modelResourceLocation;

	public ItemMeshDefinitionFixed(ModelResourceLocation modelResourceLocation)
	{
		this.modelResourceLocation = modelResourceLocation;
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack var1)
	{
		return modelResourceLocation;
	}

}
