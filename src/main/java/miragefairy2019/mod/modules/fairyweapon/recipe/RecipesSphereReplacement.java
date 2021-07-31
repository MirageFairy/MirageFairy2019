package miragefairy2019.mod.modules.fairyweapon.recipe;

import java.util.Optional;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairyweapon.recipe.ISphereReplacementItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipesSphereReplacement extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public RecipesSphereReplacement() {
        setRegistryName(new ResourceLocation(ModMirageFairy2019.MODID, "sphere_replacement"));
    }

    //

    protected static class MatchResult {

        public ISphereReplacementItem sphereReplacementItem;
        public ItemStack itemStackSphereReplacement;
        public NonNullList<Ingredient> repairmentSpheres;

    }

    protected Optional<MatchResult> match(InventoryCrafting inventoryCrafting) {
        MatchResult result = new MatchResult();

        boolean[] used = new boolean[inventoryCrafting.getSizeInventory()];

        // 妖精武器探索
        a:
        {
            for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
                if (!used[i]) {

                    ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
                    Item item = itemStack.getItem();
                    if (item instanceof ISphereReplacementItem) {
                        if (((ISphereReplacementItem) item).canSphereReplace(itemStack)) {

                            result.itemStackSphereReplacement = itemStack;
                            result.sphereReplacementItem = (ISphereReplacementItem) item;
                            used[i] = true;
                            break a;

                        }
                    }

                }
            }
            return Optional.empty();
        }

        // スフィア探索
        result.repairmentSpheres = result.sphereReplacementItem.getRepairmentSpheres(result.itemStackSphereReplacement);
        if (result.repairmentSpheres.size() == 0) return Optional.empty();
        for (Ingredient sphere : result.repairmentSpheres) {

            a:
            {
                for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
                    if (!used[i]) {

                        ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
                        if (sphere.test(itemStack)) {

                            used[i] = true;
                            break a;

                        }

                    }
                }
                return Optional.empty();
            }

        }

        // 余りがあってはならない
        for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
            if (!used[i]) {
                if (!inventoryCrafting.getStackInSlot(i).isEmpty()) {
                    return Optional.empty();
                }
            }
        }

        return Optional.of(result);
    }

    //

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return match(inventoryCrafting).isPresent();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        MatchResult nResult = match(inventoryCrafting).orElse(null);
        if (nResult == null) return ItemStack.EMPTY;
        return nResult.sphereReplacementItem.getSphereReplacedItem(nResult.itemStackSphereReplacement);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting) {
        MatchResult nResult = match(inventoryCrafting).orElse(null);
        if (nResult == null) return NonNullList.create();

        NonNullList<ItemStack> list = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemStack = inventoryCrafting.getStackInSlot(i);

            if (itemStack == nResult.itemStackSphereReplacement) {
                // ステッキを使用してもステッキは消費される
            } else {
                list.set(i, ForgeHooks.getContainerItem(itemStack));
            }

        }

        return list;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }

}
