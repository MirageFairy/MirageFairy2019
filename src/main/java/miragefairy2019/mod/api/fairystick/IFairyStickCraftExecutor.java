package miragefairy2019.mod.api.fairystick;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

public interface IFairyStickCraftExecutor
{

	public void onUpdate();

	public void hookOnUpdate(Runnable listener);

	public void onCraft(Consumer<ItemStack> setterItemStackFairyStick);

	public void hookOnCraft(Consumer<Consumer<ItemStack>> listener);

}
