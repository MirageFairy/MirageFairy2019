package miragefairy2019.mod.api.fairystick.contents;

import java.util.function.Supplier;

import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

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
					fairyStickCraft.getPos().getX() + 0.5,
					fairyStickCraft.getPos().getY() + 0.5,
					fairyStickCraft.getPos().getZ() + 0.5,
					sItemStack.get().copy());
				entityitem.setNoPickupDelay();
				fairyStickCraft.getWorld().spawnEntity(entityitem);
			}

		});
		return true;
	}

}
