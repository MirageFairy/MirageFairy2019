package miragefairy2019.mod.modules.fairyweapon;

import kotlin.Pair;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * ある点を中心とした球形の範囲のエンティティに効果を与えるエンティティセレクタです。
 */
public class SelectorEntityRanged<E extends Entity> {

    private final World world;
    private final Vec3d position;
    private final double radius;

    private final List<E> effective;
    private final List<Tuple<E, EnumTargetExecutability>> tuples;

    public SelectorEntityRanged(World world, Vec3d position, Class<? extends E> classEntity, Predicate<? super E> predicate, double radius, int maxTargetCount) {
        this.world = world;
        this.position = position;
        this.radius = radius;

        // 範囲内の全エンティティ取得
        List<E> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(
                position.x - radius,
                position.y - radius,
                position.z - radius,
                position.x + radius,
                position.y + radius,
                position.z + radius),
            new com.google.common.base.Predicate<E>() {
                private double d(double value, double min, double max) {
                    if (value < min) {
                        return min - value;
                    } else if (value < max) {
                        return 0;
                    } else {
                        return value - max;
                    }
                }

                @Override
                public boolean apply(E e) {
                    double dx = d(position.x, e.posX - e.width / 2.0, e.posX + e.width / 2.0);
                    double dy = d(position.y, e.posY, e.posY + e.width);
                    double dz = d(position.z, e.posZ - e.width / 2.0, e.posZ + e.width / 2.0);
                    if (dx * dx + dy * dy + dz * dz > radius * radius) return false;
                    return true;
                }
            });

        // 振り分け
        List<E> valid = new ArrayList<>();
        List<E> invalid = new ArrayList<>();
        ISuppliterator.ofIterable(entities)
            .forEach(e -> {
                (predicate.test(e) ? valid : invalid).add(e);
            });

        // ターゲット数制限計算
        this.effective = new ArrayList<>();
        List<E> overflowed = new ArrayList<>();
        ISuppliterator.ofIterable(valid)
            .sortedDouble(e -> e.getDistanceSq(position.x, position.y, position.z))
            .forEach((e, i) -> {
                (i < maxTargetCount ? effective : overflowed).add(e);
            });

        // 代入
        this.tuples = ISuppliterator.concat(
                ISuppliterator.ofIterable(effective).map(e -> Tuple.of(e, EnumTargetExecutability.EFFECTIVE)),
                ISuppliterator.ofIterable(overflowed).map(e -> Tuple.of(e, EnumTargetExecutability.OVERFLOWED)),
                ISuppliterator.ofIterable(invalid).map(e -> Tuple.of(e, EnumTargetExecutability.INVALID)))
            .toList();

    }

    //

    public ISuppliterator<E> getEffectiveEntities() {
        return ISuppliterator.ofIterable(effective);
    }

    public void effect() {
        UtilsMagic.spawnParticleSphericalRange(world, position, radius);
        UtilsMagic.spawnParticleTargets(world, ISuppliterator.ofIterable(tuples)
            .filter(t -> t.y == EnumTargetExecutability.EFFECTIVE)
            .map(t -> new Pair<>(t.x.getPositionVector(), t.y))
            .toList());
    }

}
