package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.mod.api.fairy.IManaSet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IPlayerAuraManager {

    /**
     * Client World Only
     */
    @SideOnly(Side.CLIENT)
    public IClientPlayerAuraHandler getClientPlayerAuraHandler();

    /**
     * Client World Only
     */
    @SideOnly(Side.CLIENT)
    public void setClientPlayerAuraModelJson(String json);

    /**
     * Server World Only
     */
    public IServerPlayerAuraHandler getServerPlayerAuraHandler(EntityPlayerMP player);

    @Nullable
    public IManaSet getGlobalFoodAura(ItemStack itemStack);

    /**
     * Server World Only
     */
    public void unloadAllServerPlayerAuraHandlers();

}
