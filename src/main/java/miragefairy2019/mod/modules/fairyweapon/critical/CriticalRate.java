package miragefairy2019.mod.modules.fairyweapon.critical;

import miragefairy2019.libkt.WeightedItem;
import miragefairy2019.libkt.WeightedItemKt;
import mirrg.boron.util.suppliterator.ISuppliterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CriticalRate {

    private List<WeightedItem<EnumCriticalFactor>> weightedItems = new ArrayList<>();
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
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.RED, weightRed));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.ORANGE, weightOrange));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.YELLOW, weightYellow));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.GREEN, weightGreen));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.BLUE, weightBlue));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.WHITE, weightWhite));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.PURPLE, weightPurple));
        weightedItems.add(new WeightedItem<>(EnumCriticalFactor.CYAN, weightCyan));
        totalWeight = WeightedItemKt.getTotalWeight(weightedItems);
    }

    public ISuppliterator<EnumCriticalFactor> getBar() {
        return ISuppliterator.range(0, 50)
                .map(i -> WeightedItemKt.getItem(weightedItems, i / 50.0));
    }

    public double getMean() {
        return weightedItems.stream()
                .mapToDouble(i -> i.getWeight() * i.getItem().coefficient)
                .sum() / totalWeight;
    }

    public EnumCriticalFactor get(Random random) {
        return WeightedItemKt.getRandomItem(weightedItems, random);
    }

}
