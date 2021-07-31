package miragefairy2019.mod.common.magic;

import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.api.magic.IMagicFactorProvider;
import net.minecraft.util.text.ITextComponent;

public class MagicStatusRanged<T> extends MagicStatus<T> {

    private Function<T, ITextComponent> formatter;
    private IMagicStatusRangeProvider<T> rangeProvider;

    public MagicStatusRanged(
            String registerName,
            Supplier<T> getter,
            Function<IMagicFactorProvider, ITextComponent> getterFormula,
            Function<T, ITextComponent> formatter,
            IMagicStatusRangeProvider<T> rangeProvider) {
        super(registerName, getter, getterFormula);
        this.formatter = formatter;
        this.rangeProvider = rangeProvider;
    }

    @Override
    public T get() {
        return rangeProvider.trim(super.get());
    }

    @Override
    public ITextComponent getDisplayValue() {
        return rangeProvider.decorate(get(), formatter);
    }

}
