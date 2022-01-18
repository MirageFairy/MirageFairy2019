package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.libkt.WeightedItem;
import miragefairy2019.libkt.WeightedItemKt;
import miragefairy2019.mod3.skill.api.ApiSkill;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class VariantFairyCrystal extends VariantFairyCrystalBase {

    public VariantFairyCrystal(@NotNull String registryName, @NotNull String unlocalizedName, @NotNull String oreName) {
        super(registryName, unlocalizedName, oreName);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStackCrystal = player.getHeldItem(hand);
        if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

        if (!player.isSneaking()) {
            if (world.isRemote) return EnumActionResult.SUCCESS;

            // ガチャを引く
            Optional<ItemStack> oItemStack = getDropper().drop(player, world, pos, hand, facing, hitX, hitY, hitZ, getDropRank(), getRareBoost(ApiSkill.skillManager.getServerSkillContainer(player)));

            // ガチャが成功した場合、
            if (oItemStack.isPresent()) {
                if (!oItemStack.get().isEmpty()) {

                    // ガチャアイテムを消費
                    itemStackCrystal.shrink(1);
                    player.addStat(StatList.getObjectUseStats(itemStackCrystal.getItem()));

                    // 妖精をドロップ
                    BlockPos pos2 = pos.offset(facing);
                    EntityItem entityitem = new EntityItem(world, pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5, oItemStack.get().copy());
                    entityitem.setNoPickupDelay();
                    world.spawnEntity(entityitem);

                }
            }

            return EnumActionResult.SUCCESS;
        } else {
            if (world.isRemote) return EnumActionResult.SUCCESS;

            // ガチャリスト取得
            List<WeightedItem<ItemStack>> dropTable = getDropper().getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ, getDropRank(), getRareBoost(ApiSkill.skillManager.getServerSkillContainer(player)));

            // 表示
            ITextComponent string = new TextComponentString("");
            string.appendText("===== " + itemStackCrystal.getDisplayName() + " (" + (world.isRemote ? "Client" : "Server") + ") =====");
            string.appendText("\n");
            double totalWeight = WeightedItemKt.getTotalWeight(dropTable);
            for (WeightedItem<ItemStack> weightedItem : ISuppliterator.ofIterable(dropTable)
                .sortedObj(i -> i.getItem().getDisplayName())
                .sortedDouble(i -> i.getWeight())) {
                string.appendText(String.format("%f%%", 100 * weightedItem.getWeight() / totalWeight) + ": ");
                string.appendText(weightedItem.getItem().getDisplayName());
                string.appendText("\n");
            }
            string.appendText("====================");
            player.sendStatusMessage(string, false);

            return EnumActionResult.SUCCESS;
        }

    }

}
