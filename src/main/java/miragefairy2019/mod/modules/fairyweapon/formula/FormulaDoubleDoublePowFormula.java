package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.modkt.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleDoublePowFormula implements IFormulaDouble {

    private double a;
    private IFormulaDouble b;

    /**
     * @param a 非負でなければなりません。
     */
    public FormulaDoubleDoublePowFormula(double a, IFormulaDouble b) {
        this.a = a;
        this.b = b;
        if (a < 0) throw new IllegalArgumentException("" + a);
    }

    @Override
    public Double get(IFairyType fairyType) {
        return Math.pow(a, b.get(fairyType));
    }

    @Override
    public Double getMax() {
        return a >= 1 ? Math.pow(a, b.getMax()) : Math.pow(a, b.getMin());
    }

    @Override
    public Double getMin() {
        return a >= 1 ? Math.pow(a, b.getMin()) : Math.pow(a, b.getMax());
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return b.getSources();
    }

}
