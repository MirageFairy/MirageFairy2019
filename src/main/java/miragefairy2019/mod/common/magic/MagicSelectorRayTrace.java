package miragefairy2019.mod.common.magic;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Predicate;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * レイトレースを行い、何もヒットしなかった場合は空中の座標を得ます。
 */
public class MagicSelectorRayTrace extends MagicSelector {

    private final Optional<RayTraceResult> oRayTraceResult;
    private final Vec3d position;

    /**
     * エンティティを無視します。
     */
    public MagicSelectorRayTrace(
            World world,
            EntityLivingBase entityLivingBase,
            double additionalReach) {
        super(world);
        oRayTraceResult = Optional.ofNullable(rayTrace(world, entityLivingBase, false, additionalReach));
        position = oRayTraceResult.map(r -> r.hitVec).orElseGet(() -> getSight(entityLivingBase, additionalReach));
    }

    public <E extends Entity> MagicSelectorRayTrace(
            World world,
            EntityLivingBase entityLivingBase,
            double additionalReach,
            Class<? extends E> classEntity,
            Predicate<? super E> filterEntity) {
        super(world);
        oRayTraceResult = Optional.ofNullable(rayTrace(world, entityLivingBase, false, additionalReach, classEntity, filterEntity));
        position = oRayTraceResult.map(r -> r.hitVec).orElseGet(() -> getSight(entityLivingBase, additionalReach));
    }

    //

    public MagicSelectorPosition getMagicSelectorPosition() {
        return new MagicSelectorPosition(world, position);
    }

    /**
     * ブロックとエンティティのうち近い方にヒットしたリザルト。
     * 空中の場合、Empty。
     */
    public Optional<RayTraceResult> getRayTraceResult() {
        return oRayTraceResult;
    }

    public boolean isHit() {
        return oRayTraceResult.isPresent();
    }

    public Vec3d getPosition() {
        return position;
    }

    public Optional<BlockPos> getHitBlockPos() {
        return oRayTraceResult.flatMap(r -> Optional.ofNullable(r.getBlockPos()));
    }

    public BlockPos getBlockPos() {
        return getHitBlockPos().orElseGet(() -> new BlockPos(position));
    }

    public Optional<Entity> getHitEntity() {
        return oRayTraceResult.flatMap(r -> Optional.ofNullable(r.entityHit));
    }

    //

    private static <E extends Entity> RayTraceResult rayTrace(
            World world,
            EntityLivingBase entityLivingBase,
            boolean useLiquids,
            double additionalReach) {
        float rotationPitch = entityLivingBase.rotationPitch;
        float rotationYaw = entityLivingBase.rotationYaw;
        double x = entityLivingBase.posX;
        double y = entityLivingBase.posY + (double) entityLivingBase.getEyeHeight();
        double z = entityLivingBase.posZ;
        Vec3d vec3d = new Vec3d(x, y, z);
        float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
        float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = entityLivingBase.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + additionalReach;
        Vec3d vec3d1 = vec3d.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);

        // ブロックのレイトレース
        RayTraceResult rayTraceResultBlock = world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);

        return rayTraceResultBlock;
    }

    private static <E extends Entity> RayTraceResult rayTrace(
            World world,
            EntityLivingBase entityLivingBase,
            boolean useLiquids,
            double additionalReach,
            Class<? extends E> classEntity,
            Predicate<? super E> filterEntity) {
        float rotationPitch = entityLivingBase.rotationPitch;
        float rotationYaw = entityLivingBase.rotationYaw;
        double x = entityLivingBase.posX;
        double y = entityLivingBase.posY + (double) entityLivingBase.getEyeHeight();
        double z = entityLivingBase.posZ;
        Vec3d vec3d = new Vec3d(x, y, z);
        float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
        float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = entityLivingBase.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + additionalReach;
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
                        if (entity == entityLivingBase) return Optional.empty();
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

    private static Vec3d getSight(EntityLivingBase entityLivingBase, double distance) {
        double d = entityLivingBase.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + distance;

        float rotationPitch = entityLivingBase.rotationPitch;
        float rotationYaw = entityLivingBase.rotationYaw;
        double x = entityLivingBase.posX;
        double y = entityLivingBase.posY + (double) entityLivingBase.getEyeHeight();
        double z = entityLivingBase.posZ;
        Vec3d vec1 = new Vec3d(x, y, z);
        float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
        float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3d vec2 = vec1.addVector((double) f6 * d, (double) f5 * d, (double) f7 * d);
        return vec2;
    }

}
