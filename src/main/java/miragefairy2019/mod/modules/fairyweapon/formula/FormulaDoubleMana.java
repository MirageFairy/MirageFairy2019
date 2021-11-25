package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.fairy.api.IFairyType;
import miragefairy2019.mod3.mana.ManaSetKt;
import miragefairy2019.mod3.mana.api.IManaType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleMana implements IFormulaDouble {

    private IManaType manaType;

    public FormulaDoubleMana(IManaType manaType) {
        this.manaType = manaType;
    }

    @Override
    public Double get(IFairyType fairyType) {
        return ManaSetKt.getMana(fairyType.getManaSet(), manaType);
    }

    @Override
    public Double getMax() {
        return Double.MAX_VALUE;
    }

    @Override
    public Double getMin() {
        return 0.0;
    }

    @Override
    public ISuppliterator<ISource> getSources() {
        return ISuppliterator.of(new SourceMana(manaType));
    }

}
