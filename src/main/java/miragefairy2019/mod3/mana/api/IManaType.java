package miragefairy2019.mod3.mana.api;

import net.minecraft.util.text.TextFormatting;

public interface IManaType {

    /**
     * @return すべて小文字
     */
    public String getName();

    public int getColor();

    public TextFormatting getTextColor();

}
