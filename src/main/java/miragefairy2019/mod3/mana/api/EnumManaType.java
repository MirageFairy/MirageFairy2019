package miragefairy2019.mod3.mana.api;

import net.minecraft.util.text.TextFormatting;

public enum EnumManaType {
    shine,
    fire,
    wind,
    gaia,
    aqua,
    dark,
    ;

    /**
     * @return すべて小文字
     */
    public String getName() {
        return name().toLowerCase();
    }

    public int getColor() {
        switch (this) {
            case shine:
                return 0x007068;
            case fire:
                return 0xFF0000;
            case wind:
                return 0x107A00;
            case gaia:
                return 0x664E00;
            case aqua:
                return 0x3535FF;
            case dark:
                return 0xB000D3;
            default:
                throw null;
        }
    }

    public TextFormatting getTextColor() {
        switch (this) {
            case shine:
                return TextFormatting.WHITE;
            case fire:
                return TextFormatting.RED;
            case wind:
                return TextFormatting.GREEN;
            case gaia:
                return TextFormatting.YELLOW;
            case aqua:
                return TextFormatting.BLUE;
            case dark:
                return TextFormatting.DARK_GRAY;
            default:
                throw null;
        }
    }

}
