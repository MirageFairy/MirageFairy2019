package miragefairy2019.mod.modules.fairyweapon;

public enum EnumTargetExecutability {
    EFFECTIVE(0x00FF00),
    OVERFLOWED(0xFF8800),
    INVALID(0xFF0000),
    ;

    public final int color;

    private EnumTargetExecutability(int color) {
        this.color = color;
    }

}
