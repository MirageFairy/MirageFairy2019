package miragefairy2019.mod.api.composite;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IComponent extends Comparable<IComponent> {

    public ResourceLocation getName();

    public ITextComponent getDisplayName();

    @Override
    public default int compareTo(IComponent other) {
        return getName().compareTo(other.getName());
    }

}
