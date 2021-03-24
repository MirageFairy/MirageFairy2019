package miragefairy2019.mod.api.fairyweapon.formula;

import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairy.IManaType;
import miragefairy2019.mod.api.fairy.ManaTypes;
import miragefairy2019.mod.modules.fairyweapon.critical.CriticalRate;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaBooleanGreaterThanEqualFormulaDoubleDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaConstantDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaConstantObject;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleAbility;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleAddFormulas;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleCost;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleDoublePowFormula;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaAddDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaMulDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaPowDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMana;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMaxFormulaDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMinFormulaDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMulFormulas;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaIntegerRoundFormulaDouble;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaSelectEntry;
import miragefairy2019.mod.modules.fairyweapon.formula.MagicStatus;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Struct1;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

public class ApiFormula
{

	public static IFormulaDouble val(double value)
	{
		return new FormulaConstantDouble(value);
	}

	public static <T> IFormula<T> val(T value)
	{
		return new FormulaConstantObject<>(value);
	}

	public static IFormulaDouble mana(IManaType manaType)
	{
		return new FormulaDoubleMana(manaType);
	}

	public static IFormulaDouble shine()
	{
		return mana(ManaTypes.shine.get());
	}

	public static IFormulaDouble fire()
	{
		return mana(ManaTypes.fire.get());
	}

	public static IFormulaDouble wind()
	{
		return mana(ManaTypes.wind.get());
	}

	public static IFormulaDouble gaia()
	{
		return mana(ManaTypes.gaia.get());
	}

	public static IFormulaDouble aqua()
	{
		return mana(ManaTypes.aqua.get());
	}

	public static IFormulaDouble dark()
	{
		return mana(ManaTypes.dark.get());
	}

	public static IFormulaDouble abilityRaw(Supplier<IAbilityType> sAbilityType)
	{
		return new FormulaDoubleAbility(sAbilityType.get());
	}

	public static IFormulaDouble ability(Supplier<IAbilityType> sAbilityType)
	{
		return mul(abilityRaw(sAbilityType), div(cost(), 50));
	}

	public static IFormulaDouble cost()
	{
		return new FormulaDoubleCost();
	}

	//

	public static IFormulaDouble limit(IFormulaDouble formula, double max)
	{
		return min(formula, max);
	}

	public static IFormulaDouble limit(IFormulaDouble formula, double max, int distribution)
	{
		return mul(root(div(min(formula, max), max), distribution), max);
	}

	public static IFormulaDouble norm(IFormulaDouble formula, double max)
	{
		return div(min(formula, max), max);
	}

	public static IFormulaDouble norm(IFormulaDouble formula, double max, int distribution)
	{
		return root(div(min(formula, max), max), distribution);
	}

	public static IFormulaDouble scale(IFormulaDouble formula, double max, double newMax)
	{
		return mul(div(min(formula, max), max), newMax);
	}

	public static IFormulaDouble scale(IFormulaDouble formula, double max, double newMax, int distribution)
	{
		return mul(root(div(min(formula, max), max), distribution), newMax);
	}

	public static IFormulaDouble add(IFormulaDouble a, double b)
	{
		return new FormulaDoubleFormulaAddDouble(a, b);
	}

	public static IFormulaDouble add(IFormulaDouble... formulas)
	{
		return new FormulaDoubleAddFormulas(ImmutableArray.ofObjArray(formulas));
	}

	public static IFormulaDouble mul(IFormulaDouble a, double b)
	{
		return new FormulaDoubleFormulaMulDouble(a, b);
	}

	public static IFormulaDouble mul(IFormulaDouble... formulas)
	{
		return new FormulaDoubleMulFormulas(ImmutableArray.ofObjArray(formulas));
	}

	public static IFormulaDouble div(IFormulaDouble a, double b)
	{
		return new FormulaDoubleFormulaMulDouble(a, 1 / b);
	}

	public static IFormulaDouble pow(IFormulaDouble a, double b)
	{
		return new FormulaDoubleFormulaPowDouble(a, b);
	}

	public static IFormulaDouble pow(double a, IFormulaDouble b)
	{
		return new FormulaDoubleDoublePowFormula(a, b);
	}

	public static IFormulaDouble root(IFormulaDouble a, double b)
	{
		return new FormulaDoubleFormulaPowDouble(a, 1 / b);
	}

	public static IFormulaDouble min(IFormulaDouble a, double b)
	{
		return new FormulaDoubleMinFormulaDouble(a, b);
	}

	public static IFormulaDouble max(IFormulaDouble a, double b)
	{
		return new FormulaDoubleMaxFormulaDouble(a, b);
	}

	public static IFormulaInteger round(IFormulaDouble formula)
	{
		return new FormulaIntegerRoundFormulaDouble(formula);
	}

	public static IFormulaDouble select(IFormulaDouble formula, double defaultValue, IFormulaSelectEntry... entries)
	{
		return new FormulaSelect(formula, defaultValue, entries);
	}

	public static IFormulaSelectEntry entry(double threshold, double value)
	{
		return new FormulaSelectEntry(threshold, value);
	}

	public static IFormulaBoolean gte(IFormulaDouble formula, double b)
	{
		return new FormulaBooleanGreaterThanEqualFormulaDoubleDouble(formula, b);
	}

	//

	public static Function<Double, ITextComponent> formatterDouble0()
	{
		return d -> new TextComponentString(String.format("%.0f", d));
	}

	public static Function<Double, ITextComponent> formatterDouble1()
	{
		return d -> new TextComponentString(String.format("%.1f", d));
	}

	public static Function<Double, ITextComponent> formatterDouble2()
	{
		return d -> new TextComponentString(String.format("%.2f", d));
	}

	public static Function<Double, ITextComponent> formatterDouble3()
	{
		return d -> new TextComponentString(String.format("%.3f", d));
	}

	public static Function<Double, ITextComponent> formatterPercent0()
	{
		return d -> new TextComponentString(String.format("%.0f%%", d * 100));
	}

	public static Function<Double, ITextComponent> formatterPercent1()
	{
		return d -> new TextComponentString(String.format("%.1f%%", d * 100));
	}

	public static Function<Double, ITextComponent> formatterPercent2()
	{
		return d -> new TextComponentString(String.format("%.2f%%", d * 100));
	}

	public static Function<Double, ITextComponent> formatterPercent3()
	{
		return d -> new TextComponentString(String.format("%.3f%%", d * 100));
	}

	public static Function<Double, ITextComponent> formatterPitch()
	{
		return d -> new TextComponentString(String.format("%.2f", Math.log(d) / Math.log(2) * 12));
	}

	public static Function<Integer, ITextComponent> formatterInteger()
	{
		return i -> new TextComponentString(Integer.toString(i));
	}

	public static Function<Double, ITextComponent> formatterTick()
	{
		return d -> new TextComponentString(String.format("%.0ft", d));
	}

	public static Function<Boolean, ITextComponent> formatterYesNo()
	{
		return b -> new TextComponentString(b ? "Yes" : "No"); // TODO localize
	}

	public static Function<CriticalRate, ITextComponent> formatterCriticalRate()
	{
		return cr -> {
			Struct1<ITextComponent> sTtextComponent = new Struct1<>(new TextComponentString(""));
			cr.getBar().forEach(cf -> {
				sTtextComponent.x = sTtextComponent.x.appendSibling(new TextComponentString("|")
					.setStyle(new Style().setColor(cf.color)));
			});
			sTtextComponent.x = sTtextComponent.x.appendText(String.format(" (%.2f)", cr.getMean()));
			return sTtextComponent.x;
		};
	}

	//

	public static <T> IMagicStatus<T> createMagicStatus(String name, Function<T, ITextComponent> formatter, IFormula<T> formula)
	{
		return new MagicStatus<T>(name, formatter, formula);
	}

}
