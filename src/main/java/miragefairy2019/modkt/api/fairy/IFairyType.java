package miragefairy2019.modkt.api.fairy;

import miragefairy2019.mod3.erg.api.IErgSet;
import miragefairy2019.modkt.api.mana.IManaSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface IFairyType {

    /**
     * @return この妖精タイプに妖精が「不在」か否か。
     */
    public boolean isEmpty();

    /**
     * この妖精の品種名を返します。
     * この妖精が特定の品種から派生した妖精である場合、派生元の品種を継承します。
     * この妖精タイプがどの実際の妖精にも基づかないか、複数の妖精の混合などである場合、nullを返します。
     *
     * @return 妖精の品種を表す内部名称。妖精が雑種の場合はnull。
     */
    @Nullable
    public ResourceLocation getBreed();

    public ITextComponent getDisplayName();

    public int getColor();

    public double getCost();

    public IManaSet getManaSet();

    public IErgSet getErgSet();

}
