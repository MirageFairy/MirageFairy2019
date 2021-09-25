package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaBoolean;
import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.modkt.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaBooleanGreaterThanEqualFormulaDoubleDouble implements IFormulaBoolean {

    private IFormulaDouble a;
    private double b;

    public FormulaBooleanGreaterThanEqualFormulaDoubleDouble(IFormulaDouble a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Boolean get(IFairyType fairyType) {
        return a.get(fairyType) >= b;
    }

    @Override
    public Boolean getMax() {
        return a.getMax() >= b;
    }

    @Override
    public Boolean getMin() {
        return a.getMin() >= b;
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return a.getSources();
    }

}
