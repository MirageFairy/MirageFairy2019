package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.AQUA;
import static net.minecraft.util.text.TextFormatting.YELLOW;

public class ItemMultiFairyMaterial<V extends ItemVariantFairyMaterial> extends ItemMultiMaterial<V> {

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

            tooltip.add(new TextComponentString("Tier " + variant.tier)
                    .setStyle(new Style().setColor(AQUA))
                    .getFormattedText());

            // 素材
            if (!variant.getComposite().isEmpty()) {
                tooltip.add(new TextComponentString("Contains: ")
                        .setStyle(new Style().setColor(YELLOW))
                        .appendSibling(variant.getComposite().getDisplayString())
                        .getFormattedText());
            }

        }
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return !getContainerItem(itemStack).isEmpty();
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return getVariant(itemStack).map(V::getContainerItem).orElse(ItemStack.EMPTY);
    }

}
