package miragefairy2019.mod.common.magic;

import net.minecraft.util.text.ITextComponent;

import java.util.function.Function;

public interface IMagicStatusRangeProvider<T> {

    public T trim(T value);

    public ITextComponent decorate(T value, Function<T, ITextComponent> formatter);

}
