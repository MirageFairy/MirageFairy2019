package miragefairy2019.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ErgSet {

    @Nonnull
    public final static ErgSet ZERO = new ErgSet(new HashMap<>());

    private final Map<Erg, Double> map;
    private final List<Erg> ergs;
    public final int size;

    public ErgSet(@Nonnull Map<Erg, Double> map) {
        this.map = new HashMap<>(map);
        ergs = new ArrayList<>(this.map.keySet());
        for (Erg erg : ergs) {
            Objects.requireNonNull(erg);
            Objects.requireNonNull(map.get(erg));
        }
        size = ergs.size();
    }

    @Nonnull
    public Erg getErg(int index) {
        return ergs.get(index);
    }

    /**
     * @return 持っていないエルグを指定した場合、0.0。
     */
    @Nonnegative
    public double getValue(Erg erg) {
        return map.getOrDefault(erg, 0.0);
    }

    @Override
    public String toString() {
        return "ErgSet" + map.toString();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErgSet ergSet = (ErgSet) o;
        return map.equals(ergSet.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

}
