package miragefairy2019.mod3.magic.api;

import miragefairy2019.modkt.api.fairy.IFairyType;
import net.minecraft.util.text.ITextComponent;

public interface IMagicStatusFormatter<T> {

    public ITextComponent getDisplayValue(IMagicStatusFunction<T> function, IFairyType fairyType);

}
