package miragefairy2019.mod.jei.fairystick;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import miragefairy2019.mod.api.fairystick.ApiFairyStick;
import miragefairy2019.mod.api.fairystick.contents.FairyStickCraftRecipe;
import miragefairy2019.mod.modules.fairystick.ModuleFairyStick;

@JEIPlugin
public class PluginFairyStickCraft implements IModPlugin
{

	public static final String uid = "miragefairy2019.fairyStickCraft";
	public static final String title = "Fairy Stick Craft";
	public static final String modName = "MirageFairy2019";

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		registry.addRecipeCategories(new RecipeCategoryFairyStickCraft(uid, title, modName, registry, () -> ModuleFairyStick.itemStackFairyStick));
	}

	@Override
	public void register(IModRegistry registry)
	{
		registry.addRecipes(ApiFairyStick.fairyStickCraftRegistry.getRecipes().toCollection(), uid);
		registry.handleRecipes(FairyStickCraftRecipe.class, recipe -> new RecipeWrapperFairyStickCraft(registry, recipe), uid);
		registry.addRecipeCatalyst(ModuleFairyStick.itemStackFairyStick, uid);
	}

}
