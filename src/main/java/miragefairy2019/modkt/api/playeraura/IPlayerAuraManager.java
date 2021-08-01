package miragefairy2019.modkt.api.playeraura;

import miragefairy2019.mod.api.fairy.IManaSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface IPlayerAuraManager {

    /**
     * Server World Only
     */
    public IPlayerAura getServerPlayerAura(EntityPlayer player);

    /**
     * Client World Only
     */
    @SideOnly(Side.CLIENT)
    public IPlayerAura getClientPlayerAura();

    @Nullable
    public IManaSet getFoodAura(ItemStack itemStack);

}
