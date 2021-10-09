package miragefairy2019.mod3.magic.api;

import net.minecraft.util.text.ITextComponent;

public interface IMagicStatusFormatter<T> {

    public ITextComponent getDisplayValue(IMagicStatusFunction<T> function, IMagicStatusFunctionArguments arguments);

}
