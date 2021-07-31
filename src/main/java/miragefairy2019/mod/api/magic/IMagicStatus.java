package miragefairy2019.mod.api.magic;

import net.minecraft.util.text.ITextComponent;

public interface IMagicStatus<T> {

    public ITextComponent getLocalizedName();

    public T get();

    public ITextComponent getDisplayValue();

    public ITextComponent getFormula(IMagicFactorProvider magicFactorProvider);

}
