package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.modkt.api.mana.IManaSet;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface IFoodAuraContainer {

    public Optional<IManaSet> getFoodAura(ItemStack itemStack);

}
