package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod3.worldgen.MirageFlowerKt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class ItemFairyWandMelting extends ItemFairyWeaponCraftingTool {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        // 指定の妖精を持っていない場合、抜ける
        a:
        {
            ItemStack itemStackFairy = getCombinedFairy(player.getHeldItem(hand));
            if (getFairy(itemStackFairy).isPresent()) {
                if (Objects.equals(getFairy(itemStackFairy).get().getMotif(), new ResourceLocation(ModMirageFairy2019.MODID, "mina"))) {
                    break a;
                }
            }
            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }

        if (!world.isRemote) {
            if (player.isSneaking()) {
                player.sendStatusMessage(MirageFlowerKt.getGrowRateTableMessage(), false);
            } else {
                player.sendStatusMessage(MirageFlowerKt.getGrowRateMessage(world, pos.offset(facing)), false);
            }
        }

        return EnumActionResult.SUCCESS;
    }

}
