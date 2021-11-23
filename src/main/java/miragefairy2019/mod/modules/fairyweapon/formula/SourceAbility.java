package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.mod3.erg.ErgKt;
import miragefairy2019.mod3.erg.api.IErgType;
import net.minecraft.util.text.ITextComponent;

public class SourceAbility implements ISource {

    private IErgType abilityType;

    public SourceAbility(IErgType abilityType) {
        this.abilityType = abilityType;
    }

    @Override
    public String getIdentifier() {
        return "ability." + abilityType.getName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return ErgKt.getDisplayName(abilityType);
    }

}
