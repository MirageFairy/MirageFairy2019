package miragefairy2019.mod.api.magic;

import miragefairy2019.mod.modules.fairy.EnumAbilityType;
import miragefairy2019.modkt.api.IManaType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicFactorProvider {

    public ITextComponent mana(IManaType manaType);

    public ITextComponent ability(EnumAbilityType abilityType);

    public ITextComponent cost();

}
