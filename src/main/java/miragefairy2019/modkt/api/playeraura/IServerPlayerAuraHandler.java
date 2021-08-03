package miragefairy2019.modkt.api.playeraura;

import net.minecraft.item.ItemStack;

public interface IServerPlayerAuraHandler extends IPlayerAuraHandler {

    public void load();

    public void save();

    public void onEat(ItemStack itemStack, int healAmount);

    public void send();

}
