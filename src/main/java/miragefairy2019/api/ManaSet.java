package miragefairy2019.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class ManaSet {

    @Nonnull
    public static ManaSet ZERO = new ManaSet(0, 0, 0, 0, 0, 0);

    @Nonnegative
    public final double shine;
    @Nonnegative
    public final double fire;
    @Nonnegative
    public final double wind;
    @Nonnegative
    public final double gaia;
    @Nonnegative
    public final double aqua;
    @Nonnegative
    public final double dark;

    public ManaSet(double shine, double fire, double wind, double gaia, double aqua, double dark) {
        this.shine = shine;
        this.fire = fire;
        this.wind = wind;
        this.gaia = gaia;
        this.aqua = aqua;
        this.dark = dark;
    }

    @Nonnull
    @Override
    public String toString() {
        return "ManaSet{" +
            "shine=" + shine +
            ", fire=" + fire +
            ", wind=" + wind +
            ", gaia=" + gaia +
            ", aqua=" + aqua +
            ", dark=" + dark +
            '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManaSet manaSet = (ManaSet) o;
        return Double.compare(manaSet.shine, shine) == 0 &&
            Double.compare(manaSet.fire, fire) == 0 &&
            Double.compare(manaSet.wind, wind) == 0 &&
            Double.compare(manaSet.gaia, gaia) == 0 &&
            Double.compare(manaSet.aqua, aqua) == 0 &&
            Double.compare(manaSet.dark, dark) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shine, fire, wind, gaia, aqua, dark);
    }

}
