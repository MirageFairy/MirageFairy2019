package miragefairy2019.mod.skill;

import javax.annotation.Nullable;

public interface IMastery {

    @Nullable
    public IMastery getParent();

    public String getName();

    public int getCoefficient();

}
