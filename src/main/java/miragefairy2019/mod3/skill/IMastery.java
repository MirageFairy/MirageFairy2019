package miragefairy2019.mod3.skill;

import javax.annotation.Nullable;

public interface IMastery {

    @Nullable
    public IMastery getParent();

    public String getName();

    public int getCoefficient();

}
