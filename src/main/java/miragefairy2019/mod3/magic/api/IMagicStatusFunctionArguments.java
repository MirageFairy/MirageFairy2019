package miragefairy2019.mod3.magic.api;

import miragefairy2019.mod3.skill.api.IMastery;
import miragefairy2019.modkt.api.fairy.IFairyType;

public interface IMagicStatusFunctionArguments {

    public int getSkillLevel(IMastery mastery);

    public IFairyType getFairyType();

}
