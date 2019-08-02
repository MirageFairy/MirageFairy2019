package miragefairy2019.mod.api.fairy;

import java.util.Optional;

import net.minecraft.item.ItemStack;

public interface IItemMirageFairy
{

	public Optional<MirageFairyType> getMirageFairy(ItemStack itemStack);

}
