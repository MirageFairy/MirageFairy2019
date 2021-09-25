package miragefairy2019.mod.modules.ore;

import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Optional;

public class ItemFilledBucket extends ItemMultiMaterial<ItemVariantFilledBucket> {

    public ItemFilledBucket() {
        this.maxStackSize = 1;
    }

    //

    @Override
    public boolean hasContainerItem(ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(Items.BUCKET);
    }

    //

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        // 使用アイテム
        ItemStack itemStack = playerIn.getHeldItem(handIn);

        // 液体を無視する視線判定
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, false);

        // Forgeオーバーライド
        ActionResult<ItemStack> result = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStack, rayTraceResult);
        if (result != null) return result;

        // 視線判定でブロックに当たらなかった場合は失敗
        if (rayTraceResult == null) return new ActionResult<>(EnumActionResult.PASS, itemStack);
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return new ActionResult<>(EnumActionResult.PASS, itemStack);

        // 視線判定で当たった座標
        BlockPos blockPos = rayTraceResult.getBlockPos();

        // 視線判定で当たった座標が編集不可の場合は失敗
        if (!worldIn.isBlockModifiable(playerIn, blockPos)) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

        // 「雑草とかの上面」以外に対して使った場合は手前の座標を選択する
        // ただし、実際には雑草は視線判定をすり抜ける
        if (!(worldIn.getBlockState(blockPos).getBlock().isReplaceable(worldIn, blockPos) && rayTraceResult.sideHit == EnumFacing.UP)) {
            blockPos = blockPos.offset(rayTraceResult.sideHit);
        }

        // その座標をプレイヤーが編集できない場合は失敗
        if (!playerIn.canPlayerEdit(blockPos, rayTraceResult.sideHit, itemStack)) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

        // 設置試行
        boolean result2 = tryPlaceContainedLiquid(playerIn, itemStack, worldIn, blockPos);

        // 設置できなかった場合は失敗
        if (!result2) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);

        // 統計関連
        if (playerIn instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, blockPos, itemStack);
        }
        playerIn.addStat(StatList.getObjectUseStats(this));

        // 使用後アイテム
        ItemStack itemStackResult = !playerIn.capabilities.isCreativeMode
                ? new ItemStack(Items.BUCKET)
                : itemStack;

        // 成功
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackResult);
    }

    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, ItemStack itemStack, World worldIn, BlockPos posIn) {

        // 対象ブロックステート
        IBlockState blockStateTarget = worldIn.getBlockState(posIn);
        boolean solid = blockStateTarget.getMaterial().isSolid();
        boolean replaceable = blockStateTarget.getBlock().isReplaceable(worldIn, posIn);

        // 対象座標が埋まっていた場合は失敗
        if (!worldIn.isAirBlock(posIn) && solid && !replaceable) return false;

        // 設置ブロック
        IBlockState blockStateFluid = getVariant(itemStack).map(v -> v.soBlockStateFluid.get()).orElse(Optional.empty()).orElse(null);
        if (blockStateFluid == null) return false;

        if (getVariant(itemStack).map(v -> v.vaporizable).orElse(false) && worldIn.provider.doesWaterVaporize()) {
            // 気化する場合

            // エフェクト
            worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
            for (int i = 0; i < 8; ++i) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                        posIn.getX() + Math.random(),
                        posIn.getY() + Math.random(),
                        posIn.getZ() + Math.random(),
                        0, 0, 0);
            }

            // 成功
            return true;
        } else {
            // 設置可能な場合

            // エフェクト
            worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);

            // 設置
            if (!worldIn.isRemote) {
                if ((!solid || replaceable) && !blockStateTarget.getMaterial().isLiquid()) worldIn.destroyBlock(posIn, true);
            }
            worldIn.setBlockState(posIn, blockStateFluid, 11);

            // 成功
            return true;
        }
    }

    public static boolean tryDrainFluid(World world, EntityPlayer player, ItemStack itemStack, RayTraceResult rayTraceResult, IBlockState blockStateFluid) {

        // 視線判定でブロックに当たらなかった場合は失敗
        if (rayTraceResult == null) return false;
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return false;

        // 視線判定で当たった座標
        BlockPos blockPos = rayTraceResult.getBlockPos();

        // 視線判定で当たった座標が編集不可の場合は失敗
        if (!world.isBlockModifiable(player, blockPos)) return false;

        // その座標をプレイヤーが編集できない場合は失敗
        if (!player.canPlayerEdit(blockPos.offset(rayTraceResult.sideHit), rayTraceResult.sideHit, itemStack)) return false;

        // 対象が指定フルイドブロックでない場合は失敗
        IBlockState blockState = world.getBlockState(blockPos);
        if (!blockState.equals(blockStateFluid)) return false;

        // エフェクト
        player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1, 1);

        // 統計
        player.addStat(StatList.getObjectUseStats(itemStack.getItem()));

        // 破壊
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);

        // 成功
        return true;
    }

	/* TODO
	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.NBTTagCompound nbt)
	{
		if (this.getClass() == ItemBucket.class) {
			return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
		} else {
			return super.initCapabilities(stack, nbt);
		}
	}
	*/

}
