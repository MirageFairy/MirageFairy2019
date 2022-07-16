package miragefairy2019.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFairySpec {

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
    public ResourceLocation getMotif();

    @Nonnull
    public ITextComponent getDisplayName();

    /**
     * @return 0xRRGGBB
     */
    public int getColor();

    public double getCost();

    @Nonnull
    public ManaSet getManaSet();

    @Nonnull
    public ErgSet getErgSet();

}
