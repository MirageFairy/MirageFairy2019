package miragefairy2019.mod.modules.fairyweapon.critical;

import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.lib.WeightedRandomKt;
import mirrg.boron.util.suppliterator.ISuppliterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CriticalRate {

    private List<WeightedRandom.WeightedItem<EnumCriticalFactor>> weightedItems = new ArrayList<>();
    private double totalWeight;

    public CriticalRate(
            double weightRed,
            double weightOrange,
            double weightYellow,
            double weightGreen,
            double weightBlue,
            double weightWhite,
            double weightPurple,
            double weightCyan) {
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.red, weightRed));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.orange, weightOrange));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.yellow, weightYellow));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.green, weightGreen));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.blue, weightBlue));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.white, weightWhite));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.purple, weightPurple));
        weightedItems.add(new WeightedRandom.WeightedItem<>(EnumCriticalFactor.cyan, weightCyan));
        totalWeight = WeightedRandomKt.getTotalWeight(weightedItems);
    }

    public ISuppliterator<EnumCriticalFactor> getBar() {
        return ISuppliterator.range(0, 50)
                .map(i -> WeightedRandomKt.getItem(weightedItems, i / 50.0));
    }

    public double getMean() {
        return weightedItems.stream()
                .mapToDouble(i -> i.getWeight() * i.getItem().coefficient)
                .sum() / totalWeight;
    }

    public EnumCriticalFactor get(Random random) {
        return WeightedRandomKt.getRandomItem(weightedItems, random);
    }

}
