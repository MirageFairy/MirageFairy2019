package miragefairy2019.api;

import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public interface IFairyCentrifugeCraftProcess {

    @Nonnull
    public ITextComponent getName();

    public double getNorma();

    public double getScore(@Nonnull IFairyCentrifugeCraftArguments arguments);

}
