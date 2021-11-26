package miragefairy2019.mod.modules.fairyweapon.magic;

import kotlin.Pair;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class UtilsMagic {

    private static double rotateY = 0;

    public static void spawnParticleSphericalRange(World world, Vec3d position, double radius) {

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
                Vec3d positionParticle = position.add(offset);

                // 仮出現点が、真下がブロックな空洞だった場合のみ受理
                if (!world.getBlockState(new BlockPos(positionParticle)).isFullBlock()) {
                    if (world.getBlockState(new BlockPos(positionParticle).down()).isFullBlock()) {

                        // パーティクル出現点2
                        // 高さを地面にくっつけるために、高さを地面の高さに固定した状態で横位置を調整する
                        double y = Math.floor(positionParticle.y) + 0.15;
                        double offsetY = y - position.y;
                        double r1 = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
                        if (Double.isNaN(r1)) break a;
                        double r2 = Math.sqrt(radius * radius - offsetY * offsetY);
                        if (Double.isNaN(r2)) break a;
                        double offsetX = offset.x / r1 * r2;
                        double offsetZ = offset.z / r1 * r2;

                        // パーティクル生成
                        world.spawnParticle(
                                EnumParticleTypes.END_ROD,
                                position.x + offsetX,
                                Math.floor(positionParticle.y) + 0.15,
                                position.z + offsetZ,
                                0,
                                -0.08,
                                0);

                        break a;
                    }
                }

            }

        }

    }

    //

    private static final double MAX_PARTICLE_COUNT = 1;

    public static void spawnParticleTargets(World world, List<Pair<Vec3d, EnumTargetExecutability>> tuples) {

        // 1tickに平均MAX_PARTICLE_COUNT個までしかパーティクルを表示しない
        double rate = MAX_PARTICLE_COUNT / (double) Math.max(tuples.size(), MAX_PARTICLE_COUNT);

        // パーティクル生成
        for (Pair<Vec3d, EnumTargetExecutability> tuple : tuples) {
            if (Math.random() < rate) {
                spawnParticleTarget(world, tuple.getFirst(), tuple.getSecond());
            }
        }

    }

    public static void spawnParticleTarget(World world, Vec3d position, EnumTargetExecutability targetExecutability) {
        spawnParticle(world, position, targetExecutability.color);
    }

    public static void spawnParticle(World world, Vec3d position, int color) {
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
