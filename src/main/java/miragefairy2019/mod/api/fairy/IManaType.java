package miragefairy2019.mod.api.fairy;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public interface IManaType
{

	/**
	 * @return すべて小文字
	 */
	public String getName();

	public TextFormatting getTextColor();

	public ITextComponent getDisplayName();

}
