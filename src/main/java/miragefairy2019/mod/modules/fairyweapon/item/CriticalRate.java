package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.suppliterator.ISuppliterator;

public final class CriticalRate
{

	private List<WeightedRandom.Item<EnumCriticalFactor>> items = new ArrayList<>();
	private double totalWeight;

	public CriticalRate(
		double weightRed,
		double weightOrange,
		double weightYellow,
		double weightGreen,
		double weightBlue,
		double weightWhite,
		double weightPurple,
		double weightCyan)
	{
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.red, weightRed));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.orange, weightOrange));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.yellow, weightYellow));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.green, weightGreen));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.blue, weightBlue));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.white, weightWhite));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.purple, weightPurple));
		items.add(new WeightedRandom.Item<>(EnumCriticalFactor.cyan, weightCyan));
		totalWeight = WeightedRandom.getTotalWeight(items);
	}

	public ISuppliterator<EnumCriticalFactor> getBar()
	{
		return ISuppliterator.range(0, 50)
			.map(i -> WeightedRandom.getItem(i / 50.0, items).get());
	}

	public double getMean()
	{
		return items.stream()
			.mapToDouble(i -> i.weight * i.item.coefficient)
			.sum() / totalWeight;
	}

}
