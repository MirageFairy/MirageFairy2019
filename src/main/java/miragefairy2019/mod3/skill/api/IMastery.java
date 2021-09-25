package miragefairy2019.mod3.skill.api;

import javax.annotation.Nullable;

public interface IMastery {

    @Nullable
    public IMastery getParent();

    public String getName();

    public int getCoefficient();

}
