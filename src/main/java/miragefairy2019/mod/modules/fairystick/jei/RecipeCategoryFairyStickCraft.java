package miragefairy2019.mod.modules.fairystick.jei;

import java.util.function.Supplier;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class RecipeCategoryFairyStickCraft implements IRecipeCategory<RecipeWrapperFairyStickCraft>
{

	private String uid;
	private String title;
	private String modName;
	private IRecipeCategoryRegistration registry;
	private Supplier<ItemStack> sItemStackIcon;

	public RecipeCategoryFairyStickCraft(String uid, String title, String modName, IRecipeCategoryRegistration registry, Supplier<ItemStack> sItemStackIcon)
	{
		this.uid = uid;
		this.title = title;
		this.modName = modName;
		this.registry = registry;
		this.sItemStackIcon = sItemStackIcon;
	}

	@Override
	public String getUid()
	{
		return uid;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public String getModName()
	{
		return modName;
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
		return registry.getJeiHelpers().getGuiHelper().createDrawableIngredient(sItemStackIcon.get());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperFairyStickCraft recipeWrapper, IIngredients ingredients)
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
