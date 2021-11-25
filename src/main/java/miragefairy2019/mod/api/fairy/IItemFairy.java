package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod3.fairy.api.IFairyType;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface IItemFairy {

    public Optional<IFairyType> getMirageFairy2019Fairy(ItemStack itemStack);

}
