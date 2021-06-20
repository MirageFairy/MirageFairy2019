package miragefairy2019.mod.modules.playeraura;

import miragefairy2019.mod.api.ApiPlayerAura;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketPlayerAura implements IMessageHandler<MessagePlayerAura, IMessage>
{

	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessagePlayerAura message, MessageContext ctx)
	{
		if (ctx.side == Side.CLIENT) {
			ApiPlayerAura.playerAuraManager.getClientPlayerAura().setAura(message.shine, message.fire, message.wind, message.gaia, message.aqua, message.dark);
		}
		return null;
	}

}
