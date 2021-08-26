package miragefairy2019.modkt.api.mana;

import net.minecraft.util.text.TextFormatting;

public interface IManaType {

    /**
     * @return すべて小文字
     */
    public String getName();

    public int getColor();

    public TextFormatting getTextColor();

}
