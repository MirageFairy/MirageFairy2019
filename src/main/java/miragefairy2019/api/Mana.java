package miragefairy2019.api;

public enum Mana {
    SHINE,
    FIRE,
    WIND,
    GAIA,
    AQUA,
    DARK,
    ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
