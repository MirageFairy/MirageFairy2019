package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.multi.ItemMulti;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFairyCrystal extends ItemMulti<VariantFairyCrystal> {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStackCrystal = player.getHeldItem(hand);
        if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

        VariantFairyCrystal variant = getVariant(itemStackCrystal);
        if (variant == null) return EnumActionResult.PASS;

        return variant.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        VariantFairyCrystal variant = getVariant(itemStack);
        if (variant == null) return UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".name");
        return UtilsMinecraft.translateToLocalFormatted("item." + variant.unlocalizedName + ".name");
    }

}
