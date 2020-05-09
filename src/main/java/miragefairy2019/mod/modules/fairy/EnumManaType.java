package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.IManaType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public enum EnumManaType implements IManaType
{
	shine(TextFormatting.WHITE),
	fire(TextFormatting.RED),
	wind(TextFormatting.GREEN),
	gaia(TextFormatting.YELLOW),
	aqua(TextFormatting.BLUE),
	dark(TextFormatting.DARK_GRAY),
	;

	public final TextFormatting textColor;

	private EnumManaType(TextFormatting textColor)
	{
		this.textColor = textColor;
	}

	@Override
	public String getName()
	{
		return name();
	}

	@Override
	public TextFormatting getTextColor()
	{
		return textColor;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation("mirageFairy2019.mana." + getName() + ".name")
			.setStyle(new Style().setColor(getTextColor()));
	}

}
