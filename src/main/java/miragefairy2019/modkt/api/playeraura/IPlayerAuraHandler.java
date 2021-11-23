package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.mod3.mana.api.IManaSet;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IPlayerAuraHandler {

    public IManaSet getPlayerAura();

    @Nullable
    public IManaSet getLocalFoodAura(ItemStack itemStack);

    public double getSaturationRate(ItemStack itemStack);

    @Nullable
    public IManaSet simulatePlayerAura(ItemStack itemStack, int healAmount);

    /**
     * @return 新しい順
     */
    public Iterable<IFoodHistoryEntry> getFoodHistory();

}
