package miragefairy2019.mod3.skill.api;

import javax.annotation.Nullable;
import java.time.Instant;

public interface ISkillVariables {

    public int getExp();

    public void setExp(int exp);

    @Nullable
    public Instant getLastMasteryResetTime();

    public void setLastMasteryResetTime(@Nullable Instant lastMasteryResetTime);

    @Nullable
    public Instant getLastAstronomicalObservationTime();

    public void setLastAstronomicalObservationTime(@Nullable Instant lastAstronomicalObservationTime);

}
