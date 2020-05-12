package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaSelectEntry;

public class FormulaSelectEntry implements IFormulaSelectEntry
{

	private double threshold;

	private double value;

	public FormulaSelectEntry(double threshold, double value)
	{
		this.threshold = threshold;
		this.value = value;
	}

	public double getThreshold()
	{
		return threshold;
	}

	public double getValue()
	{
		return value;
	}

}
