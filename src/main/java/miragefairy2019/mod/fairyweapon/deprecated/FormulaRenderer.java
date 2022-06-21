package miragefairy2019.mod.fairyweapon.deprecated;

import net.minecraft.util.text.ITextComponent;

public interface FormulaRenderer<T> {

    public ITextComponent render(FormulaArguments formulaArguments, Formula<T> formula);

}
