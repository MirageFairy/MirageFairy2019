package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class SourceCost implements ISource {

    @Override
    public String getIdentifier() {
        return "cost";
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("mirageFairy2019.formula.source.cost.name")
            .setStyle(new Style().setColor(TextFormatting.DARK_PURPLE));
    }
}
