package miragefairy2019.mod.modules.fairystick.jei;

import java.util.List;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import miragefairy2019.mod.api.fairystick.ApiFairyStick;
import miragefairy2019.mod.api.fairystick.contents.FairyStickCraftRecipe;
import miragefairy2019.mod.modules.fairystick.ModuleFairyStick;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class PluginFairyStickCraft implements IModPlugin
{

	private static final String UID_FAIRY_STICK_CRAFT = "miragefairy2019.fairyStickCraft";
	private static final String TITLE_FAIRY_STICK_CRAFT = "Fairy Stick Craft";
	private static final String MOD_NAME_FAIRY_STICK_CRAFT = "MirageFairy2019";

	private static class RecipeCategory implements IRecipeCategory<RecipeWrapper>
	{

		private IRecipeCategoryRegistration registry;

		public RecipeCategory(IRecipeCategoryRegistration registry)
		{
			this.registry = registry;
		}

		@Override
		public String getUid()
		{
			return UID_FAIRY_STICK_CRAFT;
		}

		@Override
		public String getTitle()
		{
			return TITLE_FAIRY_STICK_CRAFT;
		}

		@Override
		public String getModName()
		{
			return MOD_NAME_FAIRY_STICK_CRAFT;
		}

		@Override
		public IDrawable getBackground()
		{
			return new IDrawable() {

				@Override
				public int getWidth()
				{
					return 18 * 9;
				}

				@Override
				public int getHeight()
				{
					return 120;
				}

				@Override
				public void draw(Minecraft minecraft, int xOffset, int yOffset)
				{
					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 0, 0);
					for (int i = 0; i < 9; i++) {
						GlStateManager.pushMatrix();
						GlStateManager.translate(18 * i, 0, 0);
						Gui.drawRect(0, 0, 18, 18, 0x4c000000);
						Gui.drawRect(1, 0, 1 + 16, 1, 0x9a000000);
						Gui.drawRect(0, 0, 1, 17, 0x9a000000);
						Gui.drawRect(1, 17, 18, 18, 0xFFFFFFFF);
						Gui.drawRect(17, 1, 18, 17, 0xFFFFFFFF);
						GlStateManager.popMatrix();
					}
					GlStateManager.popMatrix();

					minecraft.fontRenderer.drawString(
						"|",
						getWidth() / 2 - minecraft.fontRenderer.getStringWidth("|") / 2,
						50,
						0x444444);

					minecraft.fontRenderer.drawString(
						"\\|/",
						getWidth() / 2 - minecraft.fontRenderer.getStringWidth("\\|/") / 2,
						50 + 10,
						0x444444);

					GlStateManager.pushMatrix();
					GlStateManager.translate(0, 50 + 20, 0);
					for (int i = 0; i < 9; i++) {
						GlStateManager.pushMatrix();
						GlStateManager.translate(18 * i, 0, 0);
						Gui.drawRect(0, 0, 18, 18, 0x4c000000);
						Gui.drawRect(1, 0, 1 + 16, 1, 0x9a000000);
						Gui.drawRect(0, 0, 1, 17, 0x9a000000);
						Gui.drawRect(1, 17, 18, 18, 0xFFFFFFFF);
						Gui.drawRect(17, 1, 18, 17, 0xFFFFFFFF);
						GlStateManager.popMatrix();
					}
					GlStateManager.popMatrix();
				}

			};
		}

		@Override
		public IDrawable getIcon()
		{
			return registry.getJeiHelpers().getGuiHelper().createDrawableIngredient(ModuleFairyStick.itemStackFairyStick);
		}

		@Override
		public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapper recipeWrapper, IIngredients ingredients)
		{
			IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();

			for (int i = 0; i < recipeWrapper.listListItemStackInput.size(); i++) {
				guiItemStackGroup.init(
					i,
					true,
					18 * (i % 3),
					18 * (i / 3));
			}

			for (int i = 0; i < recipeWrapper.listListItemStackInput.size(); i++) {
				guiItemStackGroup.init(
					recipeWrapper.listListItemStackInput.size() + i,
					false,
					18 * (i % 3),
					50 + 20 + 18 * (i / 3));
			}

			guiItemStackGroup.set(ingredients);
		}

	}

	private static class RecipeWrapper implements IRecipeWrapper
	{

		public List<List<ItemStack>> listListItemStackInput;
		public List<List<ItemStack>> listListItemStackOutput;
		public List<String> listStringInput;
		public List<String> listStringOutput;

		private RecipeWrapper(IModRegistry registry, FairyStickCraftRecipe recipe)
		{
			listListItemStackInput = registry.getJeiHelpers().getStackHelper().expandRecipeItemStackInputs(ISuppliterator.ofIterable(recipe.getConditions())
				.flatMap(condition -> condition.getIngredientsInput())
				.toList());
			listListItemStackOutput = registry.getJeiHelpers().getStackHelper().expandRecipeItemStackInputs(ISuppliterator.ofIterable(recipe.getConditions())
				.flatMap(condition -> condition.getIngredientsOutput())
				.toList());
			listStringInput = ISuppliterator.ofIterable(recipe.getConditions())
				.flatMap(condition -> condition.getStringsInput())
				.toList();
			listStringOutput = ISuppliterator.ofIterable(recipe.getConditions())
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

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		registry.addRecipeCategories(new RecipeCategory(registry));
	}

	@Override
	public void register(IModRegistry registry)
	{
		registry.addRecipes(ApiFairyStick.fairyStickCraftRegistry.getRecipes().toCollection(), UID_FAIRY_STICK_CRAFT);
		registry.handleRecipes(FairyStickCraftRecipe.class, recipe -> new RecipeWrapper(registry, recipe), UID_FAIRY_STICK_CRAFT);
		registry.addRecipeCatalyst(ModuleFairyStick.itemStackFairyStick, UID_FAIRY_STICK_CRAFT);
	}

}
