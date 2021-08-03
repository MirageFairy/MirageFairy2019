package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.mod.api.fairy.IManaSet;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IPlayerAuraHandler {

    public IManaSet getPlayerAura();

    @Nullable
    public IManaSet getLocalFoodAura(ItemStack itemStack);

}
