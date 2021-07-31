package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus;
import net.minecraft.util.text.*;

import java.util.function.Function;

public class MagicStatus<T> implements IMagicStatus<T> {

    private String name;
    private Function<T, ITextComponent> formatter;
    private IFormula<T> formula;

    public MagicStatus(String name, Function<T, ITextComponent> formatter, IFormula<T> formula) {
        this.name = name;
        this.formatter = formatter;
        this.formula = formula;
    }

    @Override
    public IFormula<T> getFormula() {
        return formula;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("mirageFairy2019.magic.status." + name + ".name");
    }

    @Override
    public ITextComponent getDisplayString(IFairyType fairyType) {
        T min = formula.getMin();
        T max = formula.getMax();
        T val = formula.get(fairyType);

        TextFormatting color;
        Boolean bold;
        if (min.equals(max)) {
            color = TextFormatting.WHITE;
            bold = null;
        } else if (val.equals(max)) {
            color = TextFormatting.DARK_GREEN;
            bold = true;
        } else if (val.equals(min)) {
            color = TextFormatting.WHITE;
            bold = null;
        } else {
            color = TextFormatting.GREEN;
            bold = null;
        }

        return new TextComponentString("")
                .appendSibling(getDisplayName())
                .appendText(": ")
                .appendSibling(formatter.apply(val)
                        .setStyle(new Style().setColor(color).setBold(bold)))
                .appendText(" (")
                .appendSibling(getDisplaySources())
                .appendText(")");
    }

    private ITextComponent getDisplaySources() {
        return getFormula().getSources()
                .distinct(s -> s.getIdentifier())
                .sortedObj(s -> s.getIdentifier())
                .map(s -> s.getDisplayName())
                .sandwich(new TextComponentString(", "))
                .apply(tcs -> {
                    TextComponentString textComponent = new TextComponentString("");
                    tcs.forEach(textComponent::appendSibling);
                    return textComponent;
                });
    }

}
