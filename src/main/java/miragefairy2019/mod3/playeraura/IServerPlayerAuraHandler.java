package miragefairy2019.mod3.playeraura;

import net.minecraft.item.ItemStack;

public interface IServerPlayerAuraHandler extends IPlayerAuraHandler {

    public void load();

    public void save();

    public void onReset();

    public void onEat(ItemStack itemStack, int healAmount);

    public void send();

}