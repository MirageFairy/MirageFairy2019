package miragefairy2019.mod.lib.multi;

import miragefairy2019.mod.lib.UtilsMinecraft;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMultiMaterial<V extends ItemVariantMaterial> extends ItemMulti<V> {

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getVariant(stack).map(v -> v.getUnlocalizedName()).orElse("item.null");
    }

    @SideOnly(Side.CLIENT)
    public void setCustomModelResourceLocations() {
        for (Tuple<Integer, V> tuple : getVariants()) {
            ModelLoader.setCustomModelResourceLocation(this,
                    tuple.x,
                    new ModelResourceLocation(new ResourceLocation(getRegistryName().getResourceDomain(), tuple.y.registryName), null));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        V variant = getVariant(itemStack).orElse(null);
        if (variant != null) {

            // ポエム
            if (UtilsMinecraft.canTranslate(getUnlocalizedName(itemStack) + ".poem")) {
                String string = UtilsMinecraft.translateToLocal(getUnlocalizedName(itemStack) + ".poem");
                if (!string.isEmpty()) {
                    tooltip.add(string);
                }
            }

        }
    }

}
