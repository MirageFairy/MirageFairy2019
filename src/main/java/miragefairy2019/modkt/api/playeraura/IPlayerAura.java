package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.mod.api.fairy.IManaSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IPlayerAura {

    public IManaSet getAura();

    public void setAura(IManaSet aura);

    @Nullable
    public IManaSet getFoodAura(ItemStack itemStack);

    /**
     * Server World Only
     */
    public void load(EntityPlayer player);

    /**
     * Server World Only
     */
    public void save(EntityPlayer player);

    /**
     * Server World Only
     */
    public void onEat(EntityPlayerMP player, ItemStack itemStack, int healAmount);

    /**
     * Server World Only
     */
    public void send(EntityPlayerMP player);

}
