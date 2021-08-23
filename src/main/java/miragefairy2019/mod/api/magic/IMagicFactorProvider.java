package miragefairy2019.mod.api.magic;

import miragefairy2019.modkt.api.IManaType;
import miragefairy2019.modkt.api.fairy.IErgType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicFactorProvider {

    public ITextComponent mana(IManaType manaType);

    public ITextComponent ability(IErgType abilityType);

    public ITextComponent cost();

}
