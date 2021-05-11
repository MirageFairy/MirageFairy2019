package miragefairy2019.mod.jei.mirageflower;

import java.util.List;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropRecipe;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class RecipeWrapperFairyLogDrop implements IRecipeWrapper
{

	private FairyLogDropRecipe recipe;

	public RecipeWrapperFairyLogDrop(IModRegistry registry, FairyLogDropRecipe recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(VanillaTypes.ITEM, new ItemStack(ModuleMirageFlower.itemBlockFairyLog));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getItemStackOutput());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		List<String> descriptions = recipe.getConditions()
			.slice(2)
			.map(sc -> sc
				.map(c -> c.getLocalizedDescription())
				.join(", "))
			.toList();

		String string = String.format("%.0f%%", recipe.getRate() * 100);
		minecraft.fontRenderer.drawString(string, 40 - 2 - minecraft.fontRenderer.getStringWidth(string), 4, 0x444444);

		minecraft.fontRenderer.drawString(recipe.getItemStackOutput().getDisplayName(), 60, 0, 0x444444);

		for (int i = 0; i < descriptions.size(); i++) {
			minecraft.fontRenderer.drawString(descriptions.get(i), 60, 10 + 10 * i, 0x444444);
		}
	}

}
