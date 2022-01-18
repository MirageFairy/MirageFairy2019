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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemMultiMaterial<V extends ItemVariantMaterial> extends ItemMulti<V> {

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return Optional.ofNullable(getVariant(stack)).map(v -> "item" + v.getUnlocalizedName()).orElse("item.null");
    }

    @SideOnly(Side.CLIENT)
    public void setCustomModelResourceLocations() {
        for (Tuple<Integer, V> tuple : getVariants()) {
            ModelLoader.setCustomModelResourceLocation(this,
                    tuple.x,
                    new ModelResourceLocation(new ResourceLocation(getRegistryName().getResourceDomain(), tuple.y.getRegistryName()), null));
        }
    }

    // TODO 子クラスに移動
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        V variant = getVariant(itemStack);
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
