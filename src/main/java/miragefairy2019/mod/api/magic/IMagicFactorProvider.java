package miragefairy2019.mod.api.magic;

import miragefairy2019.mod.modules.fairy.EnumAbilityType;
import miragefairy2019.mod.modules.fairy.EnumManaType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicFactorProvider {

    public ITextComponent mana(EnumManaType manaType);

    public ITextComponent ability(EnumAbilityType abilityType);

    public ITextComponent cost();

}
