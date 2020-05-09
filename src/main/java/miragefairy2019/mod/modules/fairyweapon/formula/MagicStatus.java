package miragefairy2019.mod.modules.fairyweapon.formula;

import java.util.function.Function;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class MagicStatus<T> implements IMagicStatus<T>
{

	private String name;
	private Function<T, ITextComponent> formatter;
	private IFormula<T> formula;

	public MagicStatus(String name, Function<T, ITextComponent> formatter, IFormula<T> formula)
	{
		this.name = name;
		this.formatter = formatter;
		this.formula = formula;
	}

	@Override
	public IFormula<T> getFormula()
	{
		return formula;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation("mirageFairy2019.magic.status." + name + ".name");
	}

	@Override
	public ITextComponent getDisplayString(IFairyType fairyType)
	{
		return new TextComponentString("")
			.appendSibling(getDisplayName())
			.appendText(": ")
			.appendSibling(formatter.apply(get(fairyType)))
			.appendText(" (")
			.appendSibling(getDisplaySources())
			.appendText(")");
	}

	private ITextComponent getDisplaySources()
	{
		return getFormula().getSources()
			.distinct(s -> s.getIdentifier())
			.sortedObj(s -> s.getIdentifier())
			.map(s -> s.getDisplayName())
			.sandwich(new TextComponentString(", "))
			.apply(tcs -> {
				TextComponentString textComponent = new TextComponentString("");
				tcs.forEach(textComponent::appendSibling);
				return textComponent;
			});
	}

}
