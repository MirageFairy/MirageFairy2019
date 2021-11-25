package miragefairy2019.mod3.erg.api;

import net.minecraft.util.text.TextFormatting;

public abstract class EnumErgType {

    /**
     * @return すべて小文字
     */
    public abstract String getName();

    @Override
    public String toString() {
        return getName().toLowerCase();
    }

    public abstract TextFormatting getTextColor();

}
