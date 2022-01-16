package miragefairy2019.mod.modules.fairyweapon.item;

import kotlin.Unit;
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft;
import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftExecutor;
import miragefairy2019.mod3.main.api.ApiMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class ItemFairyWeaponCraftingTool extends ItemFairyWeaponBase {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        // レシピ判定
        IFairyStickCraftExecutor executor;
        a:
        {
            executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos, () -> player.getHeldItem(hand));
            if (executor != null) break a;
            executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos.offset(facing), () -> player.getHeldItem(hand));
            if (executor != null) break a;
            return EnumActionResult.PASS;
        }

        //

        executor.onCraft(itemStackFairyStick -> {
            player.setHeldItem(hand, itemStackFairyStick);
            return Unit.INSTANCE;
        });

        return EnumActionResult.SUCCESS;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

        // クライアントのみ
        if (!ApiMain.side.isClient()) return;

        // 使用tick判定
        if (!(world.rand.nextDouble() < 0.1)) return;

        // プレイヤー取得
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        // アイテム取得
        if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

        // プレイヤー視線判定
        RayTraceResult rayTraceResult = rayTrace(world, player, false);
        if (rayTraceResult == null) return; // ブロックに当たらなかった場合は無視
        if (rayTraceResult.typeOfHit != Type.BLOCK) return; // ブロックに当たらなかった場合は無視

        // レシピ判定
        IFairyStickCraftExecutor executor;
        a:
        {
            executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, world, rayTraceResult.getBlockPos(), () -> itemStack);
            if (executor != null) break a;
            executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, world, rayTraceResult.getBlockPos().offset(rayTraceResult.sideHit), () -> itemStack);
            if (executor != null) break a;
            return;
        }

        //

        executor.onUpdate();

    }

    //

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        if (itemStack.getItemDamage() < itemStack.getMaxDamage()) {
            itemStack = itemStack.copy();
            itemStack.setItemDamage(itemStack.getItemDamage() + 1);
            return itemStack;
        }
        return super.getContainerItem(itemStack);
    }

    //

    @Override
    public boolean canManualRepair(ItemStack itemStack) {
        return false;
    }

    //

    @Override
    public boolean canCombine(ItemStack itemStack) {
        return false;
    }

}
