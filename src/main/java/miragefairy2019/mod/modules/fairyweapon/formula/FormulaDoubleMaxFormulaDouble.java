package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.modkt.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleMaxFormulaDouble implements IFormulaDouble {

    private IFormulaDouble a;
    private double b;

    public FormulaDoubleMaxFormulaDouble(IFormulaDouble a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Double get(IFairyType fairyType) {
        return Math.max(a.get(fairyType), b);
    }

    @Override
    public Double getMax() {
        return Math.max(a.getMax(), b);
    }

    @Override
    public Double getMin() {
        return Math.max(a.getMin(), b);
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return a.getSources();
    }

}
