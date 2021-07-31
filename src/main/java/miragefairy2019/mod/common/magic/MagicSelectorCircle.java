package miragefairy2019.mod.common.magic;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.modules.fairyweapon.item.ItemMagicWandCollecting;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicSelectorCircle extends MagicSelector {

    public final Vec3d position;
    public final double radius;

    public MagicSelectorCircle(World world, Vec3d position, double radius) {
        super(world);
        this.position = position;
        this.radius = radius;
    }

    //

    public void doEffect() {
        ItemMagicWandCollecting.spawnParticleSphericalRange(world, position, radius);
    }

    public static class BlockEntry {

        public final BlockPos blockPos;
        public final double distanceSquared;

        public BlockEntry(BlockPos blockPos, double distanceSquared) {
            this.blockPos = blockPos;
            this.distanceSquared = distanceSquared;
        }

    }

    public ISuppliterator<BlockEntry> getBlockPosList() {
        List<BlockEntry> entries = new ArrayList<>();

        int xMin = (int) Math.floor(position.x - radius);
        int xMax = (int) Math.ceil(position.x + radius);
        int zMin = (int) Math.floor(position.z - radius);
        int zMax = (int) Math.ceil(position.z + radius);
        for (int x = xMin; x <= xMax; x++) {
            int y = (int) position.y;
            for (int z = zMin; z <= zMax; z++) {
                double dx = (x + 0.5) - position.x;
                double dz = (z + 0.5) - position.z;
                double distanceSquared = dx * dx + dz * dz;
                if (distanceSquared <= radius * radius) {
                    entries.add(new BlockEntry(new BlockPos(x, y, z), distanceSquared));
                }
            }
        }

        return ISuppliterator.ofIterable(entries);
    }

}
