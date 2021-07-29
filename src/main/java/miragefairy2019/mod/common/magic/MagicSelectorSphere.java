package miragefairy2019.mod.common.magic;

import miragefairy2019.mod.modules.fairyweapon.item.ItemMagicWandCollecting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicSelectorSphere extends MagicSelector
{

	public final Vec3d position;
	public final double radius;

	public MagicSelectorSphere(World world, Vec3d position, double radius)
	{
		super(world);
		this.position = position;
		this.radius = radius;
	}

	//

	public void doEffect()
	{
		ItemMagicWandCollecting.spawnParticleSphericalRange(world, position, radius);
	}

}
