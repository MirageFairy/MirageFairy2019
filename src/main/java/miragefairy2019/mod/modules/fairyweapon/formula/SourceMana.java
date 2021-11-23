package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.mana.ManaTypeKt;
import miragefairy2019.mod3.mana.api.IManaType;
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
        return ManaTypeKt.getDisplayName(manaType);
    }

}
