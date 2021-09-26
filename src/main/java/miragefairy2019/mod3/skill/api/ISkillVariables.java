package miragefairy2019.mod3.skill.api;

import javax.annotation.Nullable;
import java.time.Instant;

public interface ISkillVariables {

    @Nullable
    public Instant getLastAstronomicalObservationTime();

    public void setLastAstronomicalObservationTime(@Nullable Instant lastAstronomicalObservationTime);

}
