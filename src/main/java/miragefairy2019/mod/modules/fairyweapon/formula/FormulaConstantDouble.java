package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.fairy.api.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaConstantDouble implements IFormulaDouble {

    private double value;

    public FormulaConstantDouble(double value) {
        this.value = value;
    }

    @Override
    public Double get(IFairyType fairyType) {
        return value;
    }

    @Override
    public Double getMax() {
        return value;
    }

    @Override
    public Double getMin() {
        return value;
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return ISuppliterator.empty();
    }

}
