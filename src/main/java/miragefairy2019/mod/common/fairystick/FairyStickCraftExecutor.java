package miragefairy2019.mod.common.fairystick;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import net.minecraft.item.ItemStack;

public class FairyStickCraftExecutor implements IFairyStickCraftExecutor
{

	private List<Consumer<Consumer<ItemStack>>> listenersOnCraft = new ArrayList<>();

	@Override
	public void onUpdate()
	{
		listenersOnUpdate.forEach(Runnable::run);
	}

	@Override
	public void hookOnUpdate(Runnable listener)
	{
		listenersOnUpdate.add(listener);
	}

	//

	private List<Runnable> listenersOnUpdate = new ArrayList<>();

	@Override
	public void hookOnCraft(Consumer<Consumer<ItemStack>> listener)
	{
		listenersOnCraft.add(listener);
	}

	@Override
	public void onCraft(Consumer<ItemStack> setterItemStackFairyStick)
	{
		listenersOnCraft.forEach(t -> t.accept(setterItemStackFairyStick));
	}

}
