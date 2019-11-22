package miragefairy2019.mod.modules.fairyweapon.recipe;

import java.util.Optional;

import miragefairy2019.mod.ModMirageFairy2019;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipesCombining extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{

	public RecipesCombining()
	{
		setRegistryName(new ResourceLocation(ModMirageFairy2019.MODID, "combining"));
	}

	//

	protected static class MatchResult
	{

		public ICombiningItem combiningItem;
		public ItemStack itemStack;
		public ItemStack itemStackPart;

	}

	protected Optional<MatchResult> match(InventoryCrafting inventoryCrafting)
	{
		MatchResult result = new MatchResult();

		boolean[] used = new boolean[inventoryCrafting.getSizeInventory()];

		// CombiningItem探索
		a:
		{
			for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
				if (!used[i]) {

					ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
					Item item = itemStack.getItem();
					if (item instanceof ICombiningItem) {
						if (((ICombiningItem) item).canCombine(itemStack)) {

							result.itemStack = itemStack;
							result.combiningItem = (ICombiningItem) item;
							used[i] = true;
							break a;

						}
					}

				}
			}
			return Optional.empty();
		}

		// Part探索
		a:
		{
			for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
				if (!used[i]) {

					ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
					if (!itemStack.isEmpty()) {
						if (result.combiningItem.canCombineWith(result.itemStack, itemStack)) {

							result.itemStackPart = itemStack;
							used[i] = true;
							break a;

						}
					}

				}
			}
			return Optional.empty();
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
	public boolean matches(InventoryCrafting inventoryCrafting, World world)
	{
		return match(inventoryCrafting).isPresent();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
	{
		MatchResult nResult = match(inventoryCrafting).orElse(null);
		if (nResult == null) return ItemStack.EMPTY;

		ItemStack itemStack = nResult.itemStack.copy();
		nResult.combiningItem.setCombinedPart(itemStack, nResult.itemStackPart);
		return itemStack;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventoryCrafting)
	{
		MatchResult nResult = match(inventoryCrafting).orElse(null);
		if (nResult == null) return NonNullList.create();

		NonNullList<ItemStack> list = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemStack = inventoryCrafting.getStackInSlot(i);

			if (itemStack == nResult.itemStack) {
				// クラフティングアイテムを使用しても耐久が減ったものは残らない
				// 代わりにそれまで合成されていたパーツが出てくる
				list.set(i, nResult.combiningItem.getCombinedPart(nResult.itemStack));
			} else {
				list.set(i, ForgeHooks.getContainerItem(itemStack));
			}

		}

		return list;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width * height >= 2;
	}

}
