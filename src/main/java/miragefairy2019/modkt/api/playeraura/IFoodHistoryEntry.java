package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.mod3.mana.api.IManaSet;
import net.minecraft.item.ItemStack;

public interface IFoodHistoryEntry {

    public ItemStack getFood();

    public IManaSet getBaseLocalFoodAura();

    public double getHealth();

}
