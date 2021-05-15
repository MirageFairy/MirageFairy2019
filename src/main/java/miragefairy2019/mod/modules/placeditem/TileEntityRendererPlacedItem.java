package miragefairy2019.mod.modules.placeditem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityRendererPlacedItem extends TileEntitySpecialRenderer<TileEntityPlacedItem>
{

	@Override
	public void render(TileEntityPlacedItem tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(0.5, 1 / 64.0, 0.5);
		GlStateManager.rotate(90, 1, 0, 0);
		ItemStack itemStack = tileEntity.getItemStack();
		renderItem(itemStack.isEmpty() ? new ItemStack(Blocks.BARRIER) : itemStack);
		GlStateManager.popMatrix();
	}

	private void renderItem(ItemStack itemStack)
	{
		if (itemStack.isEmpty()) return;

		GlStateManager.disableLighting();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
		GlStateManager.enableLighting();
	}

}
