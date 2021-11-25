package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.fairy.api.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleMinFormulaDouble implements IFormulaDouble {

    private IFormulaDouble a;
    private double b;

    public FormulaDoubleMinFormulaDouble(IFormulaDouble a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Double get(IFairyType fairyType) {
        return Math.min(a.get(fairyType), b);
    }

    @Override
    public Double getMax() {
        return Math.min(a.getMax(), b);
    }

    @Override
    public Double getMin() {
        return Math.min(a.getMin(), b);
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return a.getSources();
    }

}
