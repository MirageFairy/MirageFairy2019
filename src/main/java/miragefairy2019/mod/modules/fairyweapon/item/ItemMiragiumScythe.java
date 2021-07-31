package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus;
import miragefairy2019.mod.modules.fairyweapon.magic.EnumTargetExecutability;
import miragefairy2019.mod.modules.fairyweapon.magic.MagicExecutor;
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace;
import miragefairy2019.mod.modules.fairyweapon.magic.UtilsMagic;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static miragefairy2019.mod.api.fairyweapon.formula.ApiFormula.*;

public class ItemMiragiumScythe extends ItemFairyWeaponBase2 {

    public IMagicStatus<Double> wear = registerMagicStatus("wear", formatterPercent1(),
            val(1 / 25.0));

    public IMagicStatus<Double> coolTime = registerMagicStatus("coolTime", formatterTick(),
            val(20));

    //

    public ItemMiragiumScythe() {
        addInformationHandlerFunctions("Right click: use magic");
    }

    @Override
    protected MagicExecutor getExecutor(World world, ItemStack itemStack, EntityPlayer player) {

        // 妖精取得
        IFairyType fairyType = findFairy(itemStack, player).map(t -> t.y).orElseGet(ApiFairy::empty);

        // 視線判定
        SelectorRayTrace selectorRayTrace = new SelectorRayTrace(world, player, 0);

        // 対象判定
        List<BlockPos> blockPoses;
        if (selectorRayTrace.getSideHit().isPresent()) {
            blockPoses = getTargets(world, selectorRayTrace.getBlockPos().offset(selectorRayTrace.getSideHit().get()));
        } else {
            blockPoses = getTargets(world, selectorRayTrace.getBlockPos());
        }

        // 妖精なし判定
        if (fairyType.isEmpty()) {
            return new MagicExecutor() {
                @Override
                public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
                    selectorRayTrace.doEffect(0xFF00FF);
                }
            };
        }

        // 材料なし判定
        if (itemStack.getItemDamage() + (int) Math.ceil(wear.get(fairyType)) > itemStack.getMaxDamage()) {
            return new MagicExecutor() {
                @Override
                public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
                    selectorRayTrace.doEffect(0xFF0000);
                }
            };
        }

        // ターゲットなし判定
        if (blockPoses.size() == 0) {
            return new MagicExecutor() {
                @Override
                public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
                    selectorRayTrace.doEffect(0x00FFFF);
                }
            };
        }

        // クールダウン判定
        if (player.getCooldownTracker().hasCooldown(this)) {
            return new MagicExecutor() {
                @Override
                public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
                    selectorRayTrace.doEffect(0xFFFF00);
                }
            };
        }

        // 発動可能
        return new MagicExecutor() {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

                if (!world.isRemote) {

                    // 音取得
                    SoundEvent breakSound;
                    {
                        BlockPos blockPos = blockPoses.get(0);
                        IBlockState blockState = world.getBlockState(blockPos);
                        breakSound = blockState.getBlock().getSoundType(blockState, world, blockPos, player).getBreakSound();
                    }

                    int count = 0;
                    for (BlockPos blockPos : blockPoses) {

                        // 耐久コスト
                        int damage = UtilsMath.randomInt(world.rand, wear.get(fairyType));

                        // 耐久不足
                        if (itemStack.getItemDamage() + damage > itemStack.getMaxDamage()) break;

                        // 発動
                        itemStack.damageItem(damage, player);
                        breakBlock(world, player, EnumFacing.UP, itemStack, blockPos, 0, false);
                        count++;

                    }

                    if (count > 0) {

                        // エフェクト
                        world.playSound(null, player.posX, player.posY, player.posZ, breakSound, player.getSoundCategory(), 1.0F, 1.0F);

                        // クールタイム
                        player.getCooldownTracker().setCooldown(ItemMiragiumScythe.this, (int) (double) coolTime.get(fairyType));

                    }

                    // エフェクト
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                    player.spawnSweepParticles();

                } else {

                    // エフェクト
                    player.swingArm(hand);

                }

                return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
            }

            @Override
            public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
                selectorRayTrace.doEffect(0xFFFFFF);
                UtilsMagic.spawnParticleTargets(world, ISuppliterator.ofIterable(blockPoses)
                        .map(t -> Tuple.of(new Vec3d(t).addVector(0.5, 0.5, 0.5), EnumTargetExecutability.EFFECTIVE)));
            }
        };
    }

    protected List<BlockPos> getTargets(World world, BlockPos blockPos) {
        List<Tuple<BlockPos, Double>> tuples = new ArrayList<>();
        for (int xi = -2; xi <= 2; xi++) {
            for (int yi = -0; yi <= 0; yi++) {
                for (int zi = -2; zi <= 2; zi++) {
                    BlockPos blockPos2 = blockPos.add(xi, yi, zi);
                    IBlockState blockState = world.getBlockState(blockPos2);
                    if (blockState.getMaterial() == Material.PLANTS ||
                            blockState.getMaterial() == Material.LEAVES ||
                            blockState.getMaterial() == Material.VINE ||
                            blockState.getMaterial() == Material.GRASS ||
                            blockState.getMaterial() == Material.CACTUS) {
                        if (blockState.getBlockHardness(world, blockPos2) == 0) {
                            tuples.add(Tuple.of(blockPos2, blockPos2.distanceSq(blockPos)));
                        }
                    }
                }
            }
        }
        return ISuppliterator.ofIterable(tuples)
                .sortedDouble(Tuple::getY)
                .map(Tuple::getX)
                .toList();
    }

}
