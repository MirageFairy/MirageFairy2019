package miragefairy2019.mod.app;

import miragefairy2019.mod.EventRegistryMod;
import miragefairy2019.mod.api.ApiFairy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModuleFairy
{

	public static void init(EventRegistryMod erMod)
	{
		erMod.createItemStack.register(ic -> {
			ApiFairy.itemStackMirageFairyMain = new ItemStack(Items.LEAD); // TODO
		});
	}

}
