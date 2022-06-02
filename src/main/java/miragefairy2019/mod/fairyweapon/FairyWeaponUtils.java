package miragefairy2019.mod.fairyweapon;

import com.google.common.base.Predicate;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FairyWeaponUtils {

    @Nonnull
    public static Vec3d getSight(EntityPlayer player, double distance) {
        float rotationPitch = player.rotationPitch;
        float rotationYaw = player.rotationYaw;
        double x = player.posX;
        double y = player.posY + (double) player.getEyeHeight();
        double z = player.posZ;
        Vec3d vec1 = new Vec3d(x, y, z);
        float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
        float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3d vec2 = vec1.addVector((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
        return vec2;
    }

    @Nullable
    public static RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids, double additionalReach) {
        return rayTrace(world, player, useLiquids, additionalReach, Entity.class, e -> true);
    }

    @Nullable
    public static <E extends Entity> RayTraceResult rayTrace(
        World world,
        EntityPlayer player,
        boolean useLiquids,
        double additionalReach,
        Class<? extends E> classEntity,
        Predicate<? super E> filterEntity) {
        float rotationPitch = player.rotationPitch;
        float rotationYaw = player.rotationYaw;
        double x = player.posX;
        double y = player.posY + (double) player.getEyeHeight();
        double z = player.posZ;
        Vec3d vec3d = new Vec3d(x, y, z);
        float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
        float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + additionalReach;
        Vec3d vec3d1 = vec3d.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);

        // ブロックのレイトレース
        RayTraceResult rayTraceResultBlock = world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
        double squareDistanceBlock = rayTraceResultBlock != null
            ? vec3d.squareDistanceTo(rayTraceResultBlock.hitVec)
            : 0;

        // エンティティのレイトレース
        RayTraceResult rayTraceResultEntity = null;
        double squareDistanceEntity = 0;
        {
            List<E> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(
                vec3d.x,
                vec3d.y,
                vec3d.z,
                vec3d1.x,
                vec3d1.y,
                vec3d1.z), filterEntity);

            Tuple<Double, RayTraceResult> nTuple = ISuppliterator.ofIterable(entities)
                .mapIfPresent(entity -> {
                    if (entity == player) return Optional.empty();
                    AxisAlignedBB aabb = entity.getEntityBoundingBox();
                    RayTraceResult rayTraceResult = aabb.calculateIntercept(vec3d, vec3d1);
                    if (rayTraceResult == null) return Optional.empty();
                    return Optional.of(Tuple.of(vec3d.squareDistanceTo(rayTraceResult.hitVec), new RayTraceResult(entity, rayTraceResult.hitVec)));
                })
                .min((a, b) -> a.x.compareTo(b.x)).orElse(null);
            if (nTuple != null) {
                rayTraceResultEntity = nTuple.y;
                squareDistanceEntity = nTuple.x;
            }
        }

        if (rayTraceResultBlock != null && rayTraceResultEntity != null) {
            if (squareDistanceBlock < squareDistanceEntity) {
                return rayTraceResultBlock;
            } else {
                return rayTraceResultEntity;
            }
        } else if (rayTraceResultBlock != null) {
            return rayTraceResultBlock;
        } else if (rayTraceResultEntity != null) {
            return rayTraceResultEntity;
        } else {
            return null;
        }
    }

    public static <E extends Entity> List<E> getEntities(Class<? extends E> classEntity, World world, Vec3d positionCenter, double radius) {
        return world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(
                positionCenter.x - radius,
                positionCenter.y - radius,
                positionCenter.z - radius,
                positionCenter.x + radius,
                positionCenter.y + radius,
                positionCenter.z + radius),
            e -> {
                if (e.getDistanceSq(positionCenter.x, positionCenter.y, positionCenter.z) > radius * radius) return false;
                return true;
            });
    }

    private static double rotateY = 0;

    public static void spawnParticleSphericalRange(World world, Vec3d positionCenter, double radius) {

        // 角度アニメーション更新
        rotateY += 7.4 / 180.0 * Math.PI;
        if (rotateY > 2 * Math.PI) rotateY -= 2 * Math.PI;

        for (int i = 0; i < 8; i++) {

            // 横角度
            double yaw = rotateY + i * 0.25 * Math.PI;

            // 円形パーティクル生成
            a:
            for (int j = 0; j < 100; j++) {

                // パーティクル仮出現点
                double pitch = (-0.5 + Math.random()) * Math.PI;
                Vec3d offset = new Vec3d(
                    Math.cos(pitch) * Math.cos(yaw),
                    Math.sin(pitch),
                    Math.cos(pitch) * Math.sin(yaw)).scale(radius);
                Vec3d positionParticle = positionCenter.add(offset);

                // 仮出現点が、真下がブロックな空洞だった場合のみ受理
                if (!world.getBlockState(new BlockPos(positionParticle)).isFullBlock()) {
                    if (world.getBlockState(new BlockPos(positionParticle).down()).isFullBlock()) {

                        // パーティクル出現点2
                        // 高さを地面にくっつけるために、高さを地面の高さに固定した状態で横位置を調整する
                        double y = Math.floor(positionParticle.y) + 0.15;
                        double offsetY = y - positionCenter.y;
                        double r1 = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
                        if (Double.isNaN(r1)) break a;
                        double r2 = Math.sqrt(radius * radius - offsetY * offsetY);
                        if (Double.isNaN(r2)) break a;
                        double offsetX = offset.x / r1 * r2;
                        double offsetZ = offset.z / r1 * r2;

                        // パーティクル生成
                        world.spawnParticle(
                            EnumParticleTypes.END_ROD,
                            positionCenter.x + offsetX,
                            Math.floor(positionParticle.y) + 0.15,
                            positionCenter.z + offsetZ,
                            0,
                            -0.08,
                            0);

                        break a;
                    }
                }

            }

        }

    }

    public static <T> void spawnParticleTargets(World world, Iterable<? extends T> targets, java.util.function.Predicate<? super T> filter, Function<? super T, ? extends Vec3d> fPosition, int maxCount) {
        List<? extends T> listTargets = ISuppliterator.ofIterable(targets)
            .toList();
        double rate = 5 / (double) Math.max(listTargets.size(), 5);
        for (T target : listTargets) {

            int color;
            if (filter.test(target)) {
                if (maxCount > 0) {
                    maxCount--;
                    color = 0xFFFFFF;
                } else {
                    color = 0x00FFFF;
                }
            } else {
                color = 0xFF0000;
            }

            if (Math.random() < 0.2 * rate) {
                Vec3d position = fPosition.apply(target);
                world.spawnParticle(
                    EnumParticleTypes.SPELL_MOB,
                    position.x,
                    position.y,
                    position.z,
                    ((color >> 16) & 0xFF) / 255.0,
                    ((color >> 8) & 0xFF) / 255.0,
                    ((color >> 0) & 0xFF) / 255.0);
            }

        }
    }

}
