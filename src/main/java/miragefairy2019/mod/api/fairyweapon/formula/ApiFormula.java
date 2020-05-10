package miragefairy2019.mod.api.fairyweapon.formula;

import java.util.function.Function;

import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairy.IManaType;
import miragefairy2019.mod.modules.fairyweapon.formula.FormulaConstantDouble;
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
import miragefairy2019.mod.modules.fairyweapon.formula.MagicStatus;
import mirrg.boron.util.struct.ImmutableArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ApiFormula
{

	public static IFormulaDouble val(double value)
	{
		return new FormulaConstantDouble(value);
	}

	public static IFormulaDouble source(IManaType manaType)
	{
		return new FormulaDoubleMana(manaType);
	}

	public static IFormulaDouble source(IManaType manaType, int max)
	{
		return min(source(manaType), max);
	}

	public static IFormulaDouble source(IManaType manaType, int max, int distribution)
	{
		return mul(root(div(source(manaType), max), distribution), max);
	}

	public static IFormulaDouble source(IAbilityType abilityType)
	{
		return mul(new FormulaDoubleAbility(abilityType), div(cost(), 50));
	}

	public static IFormulaDouble source(IAbilityType abilityType, int max)
	{
		return min(source(abilityType), max);
	}

	public static IFormulaDouble source(IAbilityType abilityType, int max, int distribution)
	{
		return mul(root(div(source(abilityType), max), distribution), max);
	}

	public static IFormulaDouble cost()
	{
		return new FormulaDoubleCost();
	}

	//

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
