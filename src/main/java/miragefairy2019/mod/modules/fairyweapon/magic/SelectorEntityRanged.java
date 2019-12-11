package miragefairy2019.mod.modules.fairyweapon.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * ある点を中心とした球形の範囲のエンティティに効果を与えるエンティティセレクタです。
 */
public class SelectorEntityRanged<E extends Entity>
{

	private final World world;
	private final Vec3d position;
	private final double radius;

	private final List<E> effective;
	private final List<Tuple<E, EnumTargetExecutability>> tuples;

	public SelectorEntityRanged(World world, Vec3d position, Class<? extends E> classEntity, Predicate<? super E> predicate, double radius, int maxTargetCount)
	{
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
			e -> {
				if (e.getDistanceSq(position.x, position.y, position.z) > radius * radius) return false;
				return true;
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

	public ISuppliterator<E> getEffectiveEntities()
	{
		return ISuppliterator.ofIterable(effective);
	}

	public void effect()
	{
		UtilsMagic.spawnParticleSphericalRange(world, position, radius);
		UtilsMagic.spawnParticleTargets(world, ISuppliterator.ofIterable(tuples)
			.map(t -> t.deriveX(t.x.getPositionVector())));
	}

}
