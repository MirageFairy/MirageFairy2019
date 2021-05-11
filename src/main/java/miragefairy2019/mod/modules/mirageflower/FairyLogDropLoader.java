package miragefairy2019.mod.modules.mirageflower;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropRegistry;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import miragefairy2019.mod.modules.mirageflower.fairylogdrop.FairyLogDropConditionCanRain;
import miragefairy2019.mod.modules.mirageflower.fairylogdrop.FairyLogDropConditionHasBiomeType;
import miragefairy2019.mod.modules.mirageflower.fairylogdrop.FairyLogDropConditionOverworld;
import miragefairy2019.mod.modules.mirageflower.fairylogdrop.FairyLogDropRecipe;
import net.minecraftforge.common.BiomeDictionary;

public class FairyLogDropLoader
{

	private IFairyLogDropRegistry registry;

	public FairyLogDropLoader(IFairyLogDropRegistry registry)
	{
		this.registry = registry;
	}

	public void init()
	{

		// 時間帯
		mrfld(0.1, () -> daytime).bind(o());
		mrfld(0.1, () -> night).bind(o());
		mrfld(0.1, () -> morning).bind(o());

		// 天候
		mrfld(0.1, () -> fine).bind(o());
		mrfld(0.1, () -> rain).bind(o()).bind(r());
		mrfld(0.02, () -> thunder).bind(o()).bind(r());

		// バイオーム
		mrfld(1, () -> plains).bind(b(PLAINS));
		mrfld(1, () -> forest).bind(b(FOREST));
		mrfld(1, () -> taiga).bind(b(CONIFEROUS));
		mrfld(1, () -> desert).bind(b(SANDY));
		mrfld(1, () -> mountain).bind(b(MOUNTAIN));

	}

	private Monad<FairyLogDropRecipe> mrfld(double rate, Supplier<VariantFairy[]> sArrayVariantFairy)
	{
		FairyLogDropRecipe recipe = new FairyLogDropRecipe(rate, () -> sArrayVariantFairy.get()[0].createItemStack());
		registry.addRecipe(recipe);
		return Monad.of(recipe);
	}

	private Function<FairyLogDropRecipe, Monad<FairyLogDropRecipe>> b(BiomeDictionary.Type biome)
	{
		return recipe -> {
			recipe.addCondition(new FairyLogDropConditionHasBiomeType(biome));
			return Monad.of(recipe);
		};
	}

	private Function<FairyLogDropRecipe, Monad<FairyLogDropRecipe>> o()
	{
		return recipe -> {
			recipe.addCondition(new FairyLogDropConditionOverworld());
			return Monad.of(recipe);
		};
	}

	private Function<FairyLogDropRecipe, Monad<FairyLogDropRecipe>> r()
	{
		return recipe -> {
			recipe.addCondition(new FairyLogDropConditionCanRain());
			return Monad.of(recipe);
		};
	}

}
