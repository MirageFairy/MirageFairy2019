package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.modkt.api.fairy.IFairyType;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleAddFormulas implements IFormulaDouble {

    private ImmutableArray<IFormulaDouble> formulas;

    public FormulaDoubleAddFormulas(ImmutableArray<IFormulaDouble> formulas) {
        this.formulas = formulas;
    }

    @Override
    public Double get(IFairyType fairyType) {
        double a = 0;
        for (IFormulaDouble formula : formulas) {
            a += formula.get(fairyType);
        }
        return a;
    }

    @Override
    public Double getMax() {
        double a = 0;
        for (IFormulaDouble formula : formulas) {
            a += formula.getMax();
        }
        return a;
    }

    @Override
    public Double getMin() {
        double a = 0;
        for (IFormulaDouble formula : formulas) {
            a += formula.getMin();
        }
        return a;
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return formulas.suppliterator()
                .flatMap(f -> f.getSources());
    }

}
