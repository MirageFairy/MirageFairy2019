package miragefairy2019.mod3.mana.api;

import miragefairy2019.mod3.mana.ManaTypeKt;
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
        return ManaTypeKt.getColor(this);
    }

    public TextFormatting getTextColor() {
        return ManaTypeKt.getTextColor(this);
    }

}
