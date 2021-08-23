package miragefairy2019.mod.api.magic;

import miragefairy2019.modkt.api.IManaType;
import miragefairy2019.modkt.api.fairy.IAbilityType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicFactorProvider {

    public ITextComponent mana(IManaType manaType);

    public ITextComponent ability(IAbilityType abilityType);

    public ITextComponent cost();

}
