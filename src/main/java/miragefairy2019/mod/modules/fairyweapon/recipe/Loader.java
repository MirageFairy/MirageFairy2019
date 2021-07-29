package miragefairy2019.mod.modules.fairyweapon.recipe;

import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Loader
{

	public static void init(EventRegistryMod erMod)
	{
		erMod.addRecipe.register(() -> {

			// スフィア交換レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesSphereReplacement());

			// 妖精搭乗レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesCombining());
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesUncombining());

		});
	}

}
