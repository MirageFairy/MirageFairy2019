package miragefairy2019.mod.common.magic;

import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.api.magic.IMagicFactorProvider;
import miragefairy2019.mod.api.magic.IMagicStatus;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class MagicStatus<T> implements IMagicStatus<T> {

    private String registerName;
    private Supplier<T> getter;
    private Function<IMagicFactorProvider, ITextComponent> getterFormula;

    public MagicStatus(String registerName, Supplier<T> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula) {
        this.registerName = registerName;
        this.getter = getter;
        this.getterFormula = getterFormula;
    }

    @Override
    public ITextComponent getLocalizedName() {
        return new TextComponentTranslation("mirageFairy2019.magic.status." + registerName + ".name");
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    public ITextComponent getDisplayValue() {
        return new TextComponentString("" + get());
    }

    @Override
    public ITextComponent getFormula(IMagicFactorProvider magicFactorProvider) {
        return getterFormula.apply(magicFactorProvider);
    }

}
