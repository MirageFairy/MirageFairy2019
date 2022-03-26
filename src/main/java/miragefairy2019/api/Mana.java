package miragefairy2019.api;

import javax.annotation.Nonnull;

public enum Mana {
    SHINE,
    FIRE,
    WIND,
    GAIA,
    AQUA,
    DARK,
    ;

    @Nonnull
    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
