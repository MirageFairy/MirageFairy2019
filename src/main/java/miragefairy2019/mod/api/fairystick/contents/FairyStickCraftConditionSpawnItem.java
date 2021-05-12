package miragefairy2019.mod.api.fairystick.contents;

import java.util.function.Supplier;

import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class FairyStickCraftConditionSpawnItem implements IFairyStickCraftCondition
{

	private Supplier<ItemStack> sItemStack;

	public FairyStickCraftConditionSpawnItem(Supplier<ItemStack> sItemStack)
	{
		this.sItemStack = sItemStack;
	}

	@Override
	public boolean test(IFairyStickCraft fairyStickCraft)
	{
		fairyStickCraft.hookOnCraft(() -> {

			if (!fairyStickCraft.getWorld().isRemote) {
				EntityItem entityitem = new EntityItem(
					fairyStickCraft.getWorld(),
					fairyStickCraft.getBlockPos().getX() + 0.5,
					fairyStickCraft.getBlockPos().getY() + 0.5,
					fairyStickCraft.getBlockPos().getZ() + 0.5,
					sItemStack.get().copy());
				entityitem.setNoPickupDelay();
				fairyStickCraft.getWorld().spawnEntity(entityitem);
			}

		});
		return true;
	}

	@Override
	public ISuppliterator<Ingredient> getIngredientsOutput()
	{
		return ISuppliterator.of(Ingredient.fromStacks(sItemStack.get()));
	}

}
