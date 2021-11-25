package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.erg.api.EnumErgType;
import miragefairy2019.mod3.fairy.api.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class FormulaDoubleAbility implements IFormulaDouble {

    private EnumErgType abilityType;

    public FormulaDoubleAbility(EnumErgType abilityType) {
        this.abilityType = abilityType;
    }

    @Override
    public Double get(IFairyType fairyType) {
        return fairyType.getErgSet().getPower(abilityType);
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
        return ISuppliterator.of(new SourceAbility(abilityType));
    }

}
