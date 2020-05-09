package miragefairy2019.mod.api.fairyweapon.formula;

import java.util.function.Function;

import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairy.IManaType;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleAddFormulas;
import miragefairy2019.mod.modules.fairyweapon.formula.MagicStatus;
import mirrg.boron.util.struct.ImmutableArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ApiFormula
{

	public static <T> IFormula<T> val(T value)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaConstant<>(value);
	}

	public static IFormula<Double> source(IManaType manaType)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMana(manaType);
	}

	public static IFormula<Double> source(IManaType manaType, int max)
	{
		return min(source(manaType), max);
	}

	public static IFormula<Double> source(IManaType manaType, int max, int distribution)
	{
		return mul(root(div(source(manaType), max), distribution), max);
	}

	public static IFormula<Double> source(IAbilityType abilityType)
	{
		return mul(new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleAbility(abilityType), div(cost(), 50));
	}

	public static IFormula<Double> source(IAbilityType abilityType, int max)
	{
		return min(source(abilityType), max);
	}

	public static IFormula<Double> source(IAbilityType abilityType, int max, int distribution)
	{
		return mul(root(div(source(abilityType), max), distribution), max);
	}

	public static IFormula<Double> cost()
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleCost();
	}

	//

	public static IFormula<Double> add(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaAddDouble(a, b);
	}

	@SafeVarargs
	public static IFormula<Double> add(IFormula<Double>... formulas)
	{
		return new FormulaDoubleAddFormulas(ImmutableArray.ofObjArray(formulas));
	}

	public static IFormula<Double> mul(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaMulDouble(a, b);
	}

	@SafeVarargs
	public static IFormula<Double> mul(IFormula<Double>... formulas)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMulFormulas(ImmutableArray.ofObjArray(formulas));
	}

	public static IFormula<Double> div(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaMulDouble(a, 1 / b);
	}

	public static IFormula<Double> pow(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaPowDouble(a, b);
	}

	public static IFormula<Double> pow(double a, IFormula<Double> b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleDoublePowFormula(a, b);
	}

	public static IFormula<Double> root(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleFormulaPowDouble(a, 1 / b);
	}

	public static IFormula<Double> min(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMinFormulaDouble(a, b);
	}

	public static IFormula<Double> max(IFormula<Double> a, double b)
	{
		return new miragefairy2019.mod.modules.fairyweapon.formula.FormulaDoubleMaxFormulaDouble(a, b);
	}

	//

	public static Function<Double, ITextComponent> formatterPitch()
	{
		return d -> new TextComponentString(String.format("%.2f", Math.log(d) / Math.log(2) * 12));
	}

	//

	public static <T> IMagicStatus<T> createMagicStatus(String name, Function<T, ITextComponent> formatter, IFormula<T> formula)
	{
		return new MagicStatus<T>(name, formatter, formula);
	}

}
