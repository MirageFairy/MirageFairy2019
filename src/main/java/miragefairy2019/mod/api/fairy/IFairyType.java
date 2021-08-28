package miragefairy2019.mod.api.fairy;

import miragefairy2019.modkt.api.erg.IErgSet;
import miragefairy2019.modkt.api.erg.IErgType;
import miragefairy2019.modkt.api.mana.IManaSet;
import miragefairy2019.modkt.api.mana.IManaType;
import miragefairy2019.modkt.impl.ManaSetKt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IFairyType {

    public boolean isEmpty();

    public ResourceLocation getName();

    public int getColor();

    public double getCost();

    public IManaSet getManas();

    public default double mana(IManaType manaType) {
        return ManaSetKt.getMana(getManas(), manaType);
    }

    public IErgSet getAbilities();

    public default double erg(IErgType ergType) {
        return getAbilities().getPower(ergType);
    }

    public ITextComponent getDisplayName();

}
