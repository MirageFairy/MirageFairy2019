package miragefairy2019.mod.modules.fairyweapon;

import com.google.common.base.Predicate;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MagicSelectorUtils {

    @Nullable
    public static RayTraceResult rayTrace(
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

    @Nullable
    public static <E extends Entity> RayTraceResult rayTrace(
        World world,
        EntityLivingBase entityLivingBase,
        boolean useLiquids,
        double additionalReach,
        Class<E> classEntity,
        Predicate<E> filterEntity) {
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

    /**
     * ブロックやエンティティがあるかに関わらず、視線の先の座標を返します。
     */
    @Nonnull
    public static Vec3d getSight(EntityLivingBase entityLivingBase, double distance) {

        // 最大距離
        double d = entityLivingBase.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + distance;

        // 起点
        float rotationPitch = entityLivingBase.rotationPitch;
        float rotationYaw = entityLivingBase.rotationYaw;
        double x = entityLivingBase.posX;
        double y = entityLivingBase.posY + (double) entityLivingBase.getEyeHeight();
        double z = entityLivingBase.posZ;

        float k = (float) Math.PI / 180.0F;
        float f2 = MathHelper.cos(-rotationYaw * k - (float) Math.PI);
        float f3 = MathHelper.sin(-rotationYaw * k - (float) Math.PI);
        float f4 = -MathHelper.cos(-rotationPitch * k);

        // 視線ベクトル
        float x2 = f3 * f4;
        float y2 = MathHelper.sin(-rotationPitch * k);
        float z2 = f2 * f4;

        // 終点
        Vec3d vec2 = new Vec3d(x, y, z).addVector((double) x2 * d, (double) y2 * d, (double) z2 * d);

        return vec2;
    }

}
