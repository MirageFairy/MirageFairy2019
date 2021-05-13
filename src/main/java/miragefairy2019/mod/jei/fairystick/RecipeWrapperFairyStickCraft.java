package miragefairy2019.mod.jei.fairystick;

import java.util.List;

import mezz.jei.api.IModRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import miragefairy2019.mod.common.fairystick.FairyStickCraftRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class RecipeWrapperFairyStickCraft implements IRecipeWrapper
{

	public List<List<ItemStack>> listListItemStackInput;
	public List<List<ItemStack>> listListItemStackOutput;
	public List<String> listStringInput;
	public List<String> listStringOutput;

	public RecipeWrapperFairyStickCraft(IModRegistry registry, FairyStickCraftRecipe recipe)
	{
		listListItemStackInput = registry.getJeiHelpers().getStackHelper().expandRecipeItemStackInputs(recipe.getConditions()
			.flatMap(condition -> condition.getIngredientsInput())
			.toList());
		listListItemStackOutput = registry.getJeiHelpers().getStackHelper().expandRecipeItemStackInputs(recipe.getConditions()
			.flatMap(condition -> condition.getIngredientsOutput())
			.toList());
		listStringInput = recipe.getConditions()
			.flatMap(condition -> condition.getStringsInput())
			.toList();
		listStringOutput = recipe.getConditions()
			.flatMap(condition -> condition.getStringsOutput())
			.toList();
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInputLists(VanillaTypes.ITEM, listListItemStackInput);
		ingredients.setOutputLists(VanillaTypes.ITEM, listListItemStackOutput);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		for (int i = 0; i < listStringInput.size(); i++) {
			minecraft.fontRenderer.drawString(
				listStringInput.get(i),
				0,
				18 + 9 * i,
				0x444444);
		}
		for (int i = 0; i < listStringOutput.size(); i++) {
			minecraft.fontRenderer.drawString(
				listStringOutput.get(i),
				0,
				50 + 20 + 18 + 9 * i,
				0x444444);
		}
	}

}
