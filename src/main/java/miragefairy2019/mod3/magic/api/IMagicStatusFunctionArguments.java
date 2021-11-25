package miragefairy2019.mod3.magic.api;

import miragefairy2019.mod3.fairy.api.IFairyType;
import miragefairy2019.mod3.skill.api.IMastery;

public interface IMagicStatusFunctionArguments {

    public int getSkillLevel(IMastery mastery);

    public IFairyType getFairyType();

}
