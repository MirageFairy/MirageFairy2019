package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.IAbilitySet;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairy.IManaSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public final class FairyType implements IFairyType {

    public final String modid;
    public final int id;
    public final String name;
    public final int rare;
    public final int rank;
    public final double cost;
    public final ManaSet manaSet;
    public final AbilitySet abilitySet;
    public final ColorSet colorSet;

    public final ResourceLocation registryName;

    public FairyType(
            String modid,
            int id,
            String name,
            int rare,
            int rank,
            double cost,
            ManaSet manaSet,
            AbilitySet abilitySet,
            ColorSet colorSet) {
        this.modid = modid;
        this.id = id;
        this.name = name;
        this.rare = rare;
        this.rank = rank;
        this.cost = cost;
        this.manaSet = manaSet;
        this.abilitySet = abilitySet;
        this.colorSet = colorSet;

        this.registryName = new ResourceLocation(modid, name);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ResourceLocation getName() {
        return new ResourceLocation(modid, name);
    }

    @Override
    public int getColor() {
        return colorSet.hair;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public IManaSet getManas() {
        return manaSet;
    }

    @Override
    public IAbilitySet getAbilities() {
        return abilitySet;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("mirageFairy2019.fairy." + name + ".name");
    }

}
