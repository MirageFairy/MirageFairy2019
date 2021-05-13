package miragefairy2019.mod.common.fairystick;

import java.util.function.Supplier;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEventBus;
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
	public boolean test(IFairyStickCraftEnvironment environment, IFairyStickCraftEventBus eventBus)
	{
		eventBus.hookOnCraft(() -> {

			if (!environment.getWorld().isRemote) {
				EntityItem entityitem = new EntityItem(
					environment.getWorld(),
					environment.getBlockPos().getX() + 0.5,
					environment.getBlockPos().getY() + 0.5,
					environment.getBlockPos().getZ() + 0.5,
					sItemStack.get().copy());
				entityitem.setNoPickupDelay();
				environment.getWorld().spawnEntity(entityitem);
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
