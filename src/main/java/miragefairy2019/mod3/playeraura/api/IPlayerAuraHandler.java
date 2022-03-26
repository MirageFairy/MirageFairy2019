package miragefairy2019.mod3.playeraura.api;

import miragefairy2019.api.ManaSet;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IPlayerAuraHandler {

    public ManaSet getPlayerAura();

    @Nullable
    public ManaSet getLocalFoodAura(ItemStack itemStack);

    public double getSaturationRate(ItemStack itemStack);

    @Nullable
    public ManaSet simulatePlayerAura(ItemStack itemStack, int healAmount);

    /**
     * @return 新しい順
     */
    public Iterable<IFoodHistoryEntry> getFoodHistory();

}
