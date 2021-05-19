package miragefairy2019.mod.common.magic;

import java.util.function.Function;

import net.minecraft.util.text.ITextComponent;

public interface IMagicStatusRangeProvider<T>
{

	public T trim(T value);

	public ITextComponent decorate(T value, Function<T, ITextComponent> formatter);

}
