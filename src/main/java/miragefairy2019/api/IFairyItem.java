package miragefairy2019.api;

import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface IFairyItem {

    // TODO nullable
    public Optional<IFairyType> getMirageFairy(ItemStack itemStack);

}
