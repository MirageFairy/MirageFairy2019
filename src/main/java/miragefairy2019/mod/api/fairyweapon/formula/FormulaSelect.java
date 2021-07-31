package miragefairy2019.mod.api.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaSelect implements IFormulaDouble {

    private IFormulaDouble formula;
    private double defaultValue;
    private IFormulaSelectEntry[] entries;

    public FormulaSelect(IFormulaDouble formula, double defaultValue, IFormulaSelectEntry... entries) {
        this.formula = formula;
        this.defaultValue = defaultValue;
        this.entries = ISuppliterator.ofObjArray(entries)
                .sortedDoubleDescending(e -> e.getThreshold())
                .toArray(IFormulaSelectEntry[]::new);
    }

    @Override
    public Double get(IFairyType fairyType) {
        double value = formula.get(fairyType);
        for (IFormulaSelectEntry entry : entries) {
            if (value > entry.getThreshold()) return entry.getValue();
        }
        return defaultValue;
    }

    @Override
    public Double getMax() {
        double value = formula.getMax();
        for (IFormulaSelectEntry entry : entries) {
            if (value > entry.getThreshold()) return entry.getValue();
        }
        return defaultValue;
    }

    @Override
    public Double getMin() {
        double value = formula.getMin();
        for (IFormulaSelectEntry entry : entries) {
            if (value > entry.getThreshold()) return entry.getValue();
        }
        return defaultValue;
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return formula.getSources();
    }

}
