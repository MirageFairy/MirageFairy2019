package miragefairy2019.mod3.playeraura.api;

import miragefairy2019.api.ManaSet;
import net.minecraft.item.ItemStack;

public interface IFoodHistoryEntry {

    public ItemStack getFood();

    public ManaSet getBaseLocalFoodAura();

    public double getHealth();

}
