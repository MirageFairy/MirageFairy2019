package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairy.IManaType;
import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import net.minecraft.util.text.ITextComponent;

public class SourceMana implements ISource {

    private IManaType manaType;

    public SourceMana(IManaType manaType) {
        this.manaType = manaType;
    }

    @Override
    public String getIdentifier() {
        return "mana." + manaType.getName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return manaType.getDisplayName();
    }

}
