package miragefairy2019.modkt.api.magicstatus;

import miragefairy2019.mod.api.fairy.IFairyType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicStatusFormatter<T> {

    public ITextComponent getDisplayValue(IMagicStatusFunction<T> function, IFairyType fairyType);

}
