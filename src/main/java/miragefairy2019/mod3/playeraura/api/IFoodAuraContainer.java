package miragefairy2019.mod3.playeraura.api;

import miragefairy2019.mod3.mana.api.IManaSet;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface IFoodAuraContainer {

    public Optional<IManaSet> getFoodAura(ItemStack itemStack);

}
