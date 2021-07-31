package miragefairy2019.mod.common.magic;

import miragefairy2019.mod.modules.fairyweapon.magic.UtilsMagic;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicSelectorPosition extends MagicSelector {

    public final Vec3d position;

    public MagicSelectorPosition(World world, Vec3d position) {
        super(world);
        this.position = position;
    }

    //

    public MagicSelectorCircle getMagicSelectorCircle(double radius) {
        return new MagicSelectorCircle(world, position, radius);
    }

    public MagicSelectorSphere getMagicSelectorSphere(double radius) {
        return new MagicSelectorSphere(world, position, radius);
    }

    //

    public void doEffect(int color) {
        UtilsMagic.spawnParticle(world, position, color);
    }

}
