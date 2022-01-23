package miragefairy2019.mod.modules.fairyweapon.magic;

import com.google.common.base.Predicate;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

/**
 * レイトレースを行い、何もヒットしなかった場合は空中の座標を得ます。
 */
public class SelectorRayTrace {

    private final World world;
    private final Optional<RayTraceResult> oRayTraceResult;
    private final Vec3d position;
    private final BlockPos blockPos;

    public SelectorRayTrace(World world, EntityPlayer player, double additionalReach) {
        this.world = world;
        this.oRayTraceResult = Optional.ofNullable(rayTrace(world, player, false, additionalReach));
        if (oRayTraceResult.isPresent()) {
            this.position = oRayTraceResult.get().hitVec;
            this.blockPos = oRayTraceResult.get().getBlockPos() != null
                ? oRayTraceResult.get().getBlockPos()
                : new BlockPos(this.position);
        } else {
            this.position = getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + additionalReach);
            this.blockPos = new BlockPos(this.position);
        }
    }

    //

    public Optional<RayTraceResult> getRayTraceResult() {
        return oRayTraceResult;
    }

    public boolean isHit() {
        return oRayTraceResult.isPresent();
    }

    public Vec3d getPosition() {
        return position;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Optional<Entity> getEntityHit() {
        if (oRayTraceResult.isPresent()) {
            if (oRayTraceResult.get().typeOfHit == Type.ENTITY) {
                return Optional.of(oRayTraceResult.get().entityHit);
            }
        }
        return Optional.empty();
    }

    public Optional<Vec3d> getPositionHit() {
        if (oRayTraceResult.isPresent()) {
            if (oRayTraceResult.get().typeOfHit == Type.ENTITY || oRayTraceResult.get().typeOfHit == Type.BLOCK) {
                return Optional.of(oRayTraceResult.get().hitVec);
            }
        }
        return Optional.empty();
    }

    public Optional<BlockPos> getBlockPosHit() {
        if (oRayTraceResult.isPresent()) {
            if (oRayTraceResult.get().typeOfHit == Type.BLOCK) {
                return Optional.of(oRayTraceResult.get().getBlockPos());
            }
        }
        return Optional.empty();
    }

    public Optional<EnumFacing> getSideHit() {
        if (oRayTraceResult.isPresent()) {
            if (oRayTraceResult.get().typeOfHit == Type.BLOCK) {
                return Optional.of(oRayTraceResult.get().sideHit);
            }
        }
        return Optional.empty();
    }

    public void doEffect(int color) {
        UtilsMagic.spawnParticle(world, position, color);
    }

    //

    private static RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids, double additionalReach) {
        return rayTrace(world, player, useLiquids, additionalReach, Entity.class, e -> true);
    }

    private static <E extends Entity> RayTraceResult rayTrace(
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

    private static Vec3d getSight(EntityPlayer player, double distance) {
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

}
