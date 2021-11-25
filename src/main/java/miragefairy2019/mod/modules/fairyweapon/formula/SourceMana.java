package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.mana.ManaTypeKt;
import miragefairy2019.mod3.mana.api.EnumManaType;
import net.minecraft.util.text.ITextComponent;

public class SourceMana implements ISource {

    private EnumManaType manaType;

    public SourceMana(EnumManaType manaType) {
        this.manaType = manaType;
    }

    @Override
    public String getIdentifier() {
        return "mana." + manaType;
    }

    @Override
    public ITextComponent getDisplayName() {
        return ManaTypeKt.getDisplayName(manaType);
    }

}
