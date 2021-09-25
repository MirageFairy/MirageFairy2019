package miragefairy2019.modkt.api.skill.api;

import javax.annotation.Nullable;

public interface IMastery {

    @Nullable
    public IMastery getParent();

    public String getName();

    public int getCoefficient();

}
