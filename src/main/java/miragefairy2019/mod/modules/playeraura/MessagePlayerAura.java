package miragefairy2019.mod.modules.playeraura;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessagePlayerAura implements IMessage {

    public double shine;
    public double fire;
    public double wind;
    public double gaia;
    public double aqua;
    public double dark;

    public MessagePlayerAura() {

    }

    public MessagePlayerAura(double shine, double fire, double wind, double gaia, double aqua, double dark) {
        this.shine = shine;
        this.fire = fire;
        this.wind = wind;
        this.gaia = gaia;
        this.aqua = aqua;
        this.dark = dark;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shine = buf.readDouble();
        fire = buf.readDouble();
        wind = buf.readDouble();
        gaia = buf.readDouble();
        aqua = buf.readDouble();
        dark = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(shine);
        buf.writeDouble(fire);
        buf.writeDouble(wind);
        buf.writeDouble(gaia);
        buf.writeDouble(aqua);
        buf.writeDouble(dark);
    }

}
