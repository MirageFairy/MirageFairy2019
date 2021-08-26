package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.modkt.api.mana.IManaType;
import miragefairy2019.modkt.impl.mana.ManaTypeKt;
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
