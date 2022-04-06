package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.api.Erg;
import miragefairy2019.api.Mana;
import miragefairy2019.mod.skill.IMastery;

public interface IMagicStatusFunctionArguments {

    public int getSkillLevel(IMastery mastery);

    public double getCost();

    public double getManaValue(Mana mana);

    public double getErgValue(Erg erg);

}
