package miragefairy2019.mod3.mana.api;

public enum EnumManaType {
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
