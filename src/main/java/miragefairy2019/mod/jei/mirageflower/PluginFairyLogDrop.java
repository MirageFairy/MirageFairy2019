package miragefairy2019.mod.jei.mirageflower;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropRecipe;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class PluginFairyLogDrop implements IModPlugin {

    public static final String uid = "miragefairy2019.fairyLogDrop";
    public static final String title = "Fairy Log Drop";
    public static final String modName = "MirageFairy2019";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new RecipeCategoryFairyLogDrop(uid, title, modName, registry, () -> new ItemStack(ApiMirageFlower.itemBlockFairyLog)));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(ApiMirageFlower.fairyLogDropRegistry.getRecipes().toCollection(), uid);
        registry.handleRecipes(FairyLogDropRecipe.class, recipe -> new RecipeWrapperFairyLogDrop(registry, recipe), uid);
    }

}
