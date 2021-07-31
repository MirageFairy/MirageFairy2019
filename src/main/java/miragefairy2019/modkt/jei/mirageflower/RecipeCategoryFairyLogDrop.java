package miragefairy2019.modkt.jei.mirageflower;

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

import java.util.function.Supplier;

public class RecipeCategoryFairyLogDrop implements IRecipeCategory<RecipeWrapperFairyLogDrop> {

    private String uid;
    private String title;
    private String modName;
    private IRecipeCategoryRegistration registry;
    private Supplier<ItemStack> sItemStackIcon;

    public RecipeCategoryFairyLogDrop(String uid, String title, String modName, IRecipeCategoryRegistration registry, Supplier<ItemStack> sItemStackIcon) {
        this.uid = uid;
        this.title = title;
        this.modName = modName;
        this.registry = registry;
        this.sItemStackIcon = sItemStackIcon;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getModName() {
        return modName;
    }

    @Override
    public IDrawable getBackground() {
        return new IDrawable() {

            @Override
            public int getWidth() {
                return 18 * 9;
            }

            @Override
            public int getHeight() {
                return 18 * 1;
            }

            @Override
            public void draw(Minecraft minecraft, int xOffset, int yOffset) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 0);
                Gui.drawRect(0, 0, 18, 18, 0x4c000000);
                Gui.drawRect(1, 0, 1 + 16, 1, 0x9a000000);
                Gui.drawRect(0, 0, 1, 17, 0x9a000000);
                Gui.drawRect(1, 17, 18, 18, 0xFFFFFFFF);
                Gui.drawRect(17, 1, 18, 17, 0xFFFFFFFF);
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.translate(40, 0, 0);
                Gui.drawRect(0, 0, 18, 18, 0x4c000000);
                Gui.drawRect(1, 0, 1 + 16, 1, 0x9a000000);
                Gui.drawRect(0, 0, 1, 17, 0x9a000000);
                Gui.drawRect(1, 17, 18, 18, 0xFFFFFFFF);
                Gui.drawRect(17, 1, 18, 17, 0xFFFFFFFF);
                GlStateManager.popMatrix();
            }

        };
    }

    @Override
    public IDrawable getIcon() {
        return registry.getJeiHelpers().getGuiHelper().createDrawableIngredient(sItemStackIcon.get());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperFairyLogDrop recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();

        guiItemStackGroup.init(0, true, 0, 0);
        guiItemStackGroup.init(1, false, 40, 0);

        guiItemStackGroup.set(ingredients);
    }

}
