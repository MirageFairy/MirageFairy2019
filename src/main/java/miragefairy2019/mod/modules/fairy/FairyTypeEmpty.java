package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.modkt.api.IManaSet;
import miragefairy2019.modkt.api.erg.IErgSet;
import miragefairy2019.modkt.impl.ManaSet;
import miragefairy2019.modkt.impl.fairy.ErgSet;
import mirrg.boron.util.struct.ImmutableArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class FairyTypeEmpty implements IFairyType {

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation("minecraft", "special_empty");
    }

    @Override
    public int getColor() {
        return 0xFFFFFF;
    }

    @Override
    public double getCost() {
        return 50;
    }

    @Override
    public IManaSet getManas() {
        return new ManaSet(0, 0, 0, 0, 0, 0);
    }

    @Override
    public IErgSet getAbilities() {
        return new ErgSet(ImmutableArray.empty());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString("empty");
    }

}
