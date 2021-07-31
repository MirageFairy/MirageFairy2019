package miragefairy2019.mod.api.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicStatus<T> {

    public IFormula<T> getFormula();

    public default T get(IFairyType fairyType) {
        return getFormula().get(fairyType);
    }

    public ITextComponent getDisplayName();

    public ITextComponent getDisplayString(IFairyType fairyType);

}
