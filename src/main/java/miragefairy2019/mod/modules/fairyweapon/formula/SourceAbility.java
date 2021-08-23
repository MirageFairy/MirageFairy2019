package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.modkt.api.fairy.IErgType;
import miragefairy2019.modkt.impl.fairy.ErgTypeKt;
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
        return ErgTypeKt.getDisplayName(abilityType);
    }

}
