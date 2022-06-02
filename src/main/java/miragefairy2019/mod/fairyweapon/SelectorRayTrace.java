package miragefairy2019.mod.fairyweapon;

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
        this.oRayTraceResult = Optional.ofNullable(FairyWeaponUtils.rayTrace(world, player, false, additionalReach));
        if (oRayTraceResult.isPresent()) {
            this.position = oRayTraceResult.get().hitVec;
            this.blockPos = oRayTraceResult.get().getBlockPos() != null
                ? oRayTraceResult.get().getBlockPos()
                : new BlockPos(this.position);
        } else {
            this.position = UtilKt.getSight(player, additionalReach);
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
        UtilKt.spawnParticle(world, position, color);
    }

}
