package miragefairy2019.mod.oreseed;

import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.UtilsString;
import org.jetbrains.annotations.NotNull;
import scala.util.Random;


public class VeinHelper {

    public static boolean test(long seed, int horizontalSize, int verticalSize, double chance, Element[] elements, @NotNull OreSeedDropEnvironment environment) {

        // タイル位置の特定
        int tileX = getTileCoordinate(environment.getBlockPos().getX(), horizontalSize);
        int tileY = getTileCoordinate(environment.getBlockPos().getY(), verticalSize);
        int tileZ = getTileCoordinate(environment.getBlockPos().getZ(), horizontalSize);

        // 成分倍率
        double[] as = new double[elements.length];
        for (int i = 0; i < elements.length; i++) {
            as[i] = randomElement(environment.getWorld().getSeed() * 17566883L + elements[i].getSeed() * 16227457L, elements[i].getSize(), tileX * horizontalSize, tileZ * horizontalSize);
        }

        // 成分倍率の合成
        double a = multiplyElement(as);

        // 鉱石ごとの固有乱数を合成
        double b = rand(13788169L + environment.getWorld().getSeed() * 68640023L + seed * 86802673L + tileX * 84663211L + tileY * 34193609L + tileZ * 79500227L);

        // 出現判定
        return multiplyElement(a, b) < chance;
    }

    /**
     * ブロック座標が所属するタイル位置を求める
     */
    private static int getTileCoordinate(int a, int b) {
        if (a < 0) a -= b - 1;
        return a / b;
    }

    // 元素密度計算

    /**
     * 元素の密度を得る
     *
     * @return 引数の変動に対して戻り値は一様分布に従います。
     */
    private static double randomElement(long seed, int size, int x, int z) {
        int tileX = getTileCoordinate(x, size);
        int tileZ = getTileCoordinate(z, size);
        double b00 = randomElementCrossPoint(seed, tileX + 0, tileZ + 0);
        double b01 = randomElementCrossPoint(seed, tileX + 0, tileZ + 1);
        double b10 = randomElementCrossPoint(seed, tileX + 1, tileZ + 0);
        double b11 = randomElementCrossPoint(seed, tileX + 1, tileZ + 1);
        double rateX = (x - tileX * size) / (double) size;
        double rateZ = (z - tileZ * size) / (double) size;
        return k(rateZ, k(rateX, b00 * (1 - rateX) + b10 * rateX) * (1 - rateZ) + k(rateX, b01 * (1 - rateX) + b11 * rateX) * rateZ);
    }

    /**
     * 元素のタイル交点における密度を得る
     */
    private static double randomElementCrossPoint(long seed, int tileX, int tileZ) {
        return rand(49984939L + seed * 15158987L + tileX * 33835717L + tileZ * 46560797L);
    }

    /**
     * 2個の一様乱数の間のrateに対応する点における分布を一様分布に補正する関数
     */
    private static double k(double rate, double x) {
        if (rate >= 0.5) rate = 1 - rate;
        return x < 0.5 ? k2(rate, x) : 1 - k2(rate, 1 - x);
    }

    private static double k2(double rate, double x) {
        return x < rate
            ? (1 / (2 - 2 * rate)) * (x * x / rate)
            : (1 / (2 - 2 * rate)) * (2 * x - rate);
    }

    // 乱数合成

    /**
     * 一様分布に従う値を乗算し、更に一様分布になるように補正をかける
     *
     * @param as 要素は1から4まで。
     */
    private static double multiplyElement(double... as) {
        if (as.length == 1) {
            double a = as[0];
            return a;
        } else if (as.length == 2) {
            double a = as[0] * as[1];
            return a - a * log(a);
        } else if (as.length == 3) {
            double a = as[0] * as[1] * as[2];
            return 1 / 2.0 * a * (2 - 2 * log(a) + pow2(log(a)));
        } else if (as.length == 4) {
            double a = as[0] * as[1] * as[2] * as[3];
            return -1 / 6.0 * a * (-6 + 6 * log(a) - 3 * pow2(log(a)) + pow3(log(a)));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static double log(double a) {
        return Math.log(a);
    }

    private static double pow2(double a) {
        return a * a;
    }

    private static double pow3(double a) {
        return a * a * a;
    }

    // 乱数生成

    /**
     * シード付き乱数
     */
    private static double rand(long seed) {
        Random random = new Random(seed);
        random.nextDouble();
        random.nextDouble();
        random.nextDouble();
        return random.nextDouble();
    }

    // テスト

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        int countAll = 0;
        int[] count = new int[101];

        for (int i = 0; i < 1000000; i++) {
            int tileX = UtilsMath.randomBetween(-10000, 10000);
            int tileY = UtilsMath.randomBetween(-10000, 10000);
            int tileZ = UtilsMath.randomBetween(-10000, 10000);
            double r1 = randomElement(2456 * 17566883L + Elements.INSTANCE.getALUMINIUM().getSeed() * 16227457L, Elements.INSTANCE.getALUMINIUM().getSeed(), tileX * 16, tileZ * 16);
            double r2 = randomElement(2456 * 17566883L + Elements.INSTANCE.getMAGNESIUM().getSeed() * 16227457L, Elements.INSTANCE.getMAGNESIUM().getSeed(), tileX * 16, tileZ * 16);
            double r3 = randomElement(2456 * 17566883L + Elements.INSTANCE.getFLUORINE().getSeed() * 16227457L, Elements.INSTANCE.getFLUORINE().getSeed(), tileX * 16, tileZ * 16);
            double a = multiplyElement(r1, r2, r3);
            double b = rand(13788169L + 2456 * 68640023L + 2486 * 86802673L + tileX * 84663211L + tileY * 34193609L + tileZ * 79500227L);
            double c = multiplyElement(a, b);
            double d1 = multiplyElement(Math.random());
            double d2 = multiplyElement(Math.random(), Math.random());
            double d3 = multiplyElement(Math.random(), Math.random(), Math.random());
            double d4 = multiplyElement(Math.random(), Math.random(), Math.random(), Math.random());

            countAll++;
            count[(int) (c * 100)]++;
        }

        for (int i = 0; i < count.length; i++) {
            double a = count[i] / (double) countAll;
            System.out.println(String.format("%3d %7.5f %s",
                i,
                a * 100,
                UtilsString.repeat('|', (int) (100 * a * 100))));
        }

    }

}
