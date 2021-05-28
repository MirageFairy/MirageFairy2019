package miragefairy2019.mod.api.playeraura;

import java.util.Optional;

import miragefairy2019.mod.api.fairy.IManaSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPlayerAuraManager
{

	/**
	 * Server World Only
	 */
	public IPlayerAura getServerPlayerAura(EntityPlayer player);

	/**
	 * Client World Only
	 */
	@SideOnly(Side.CLIENT)
	public IPlayerAura getClientPlayerAura();

	public Optional<IManaSet> getFoodAura(ItemStack itemStack);

}
