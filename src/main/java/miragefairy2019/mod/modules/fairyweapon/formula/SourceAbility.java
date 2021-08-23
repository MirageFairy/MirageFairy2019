package miragefairy2019.mod.modules.fairyweapon.formula;

import miragefairy2019.mod.api.fairyweapon.formula.ISource;
import miragefairy2019.modkt.api.fairy.IAbilityType;
import miragefairy2019.modkt.impl.fairy.AbilityTypeKt;
import net.minecraft.util.text.ITextComponent;

public class SourceAbility implements ISource {

    private IAbilityType abilityType;

    public SourceAbility(IAbilityType abilityType) {
        this.abilityType = abilityType;
    }

    @Override
    public String getIdentifier() {
        return "ability." + abilityType.getName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return AbilityTypeKt.getDisplayName(abilityType);
    }

}
