package miragefairy2019.mod3.fairy.api;

import net.minecraft.item.ItemStack;

public final class FairyDropEntry {

    /**
     * 1.0のときに標準的な関係性、0.0のときに完全に無関係。
     * その中間は、例えば「鮭妖精に対する魚カテゴリ」のように上位概念におけるヒットを表現する。
     * 「司書精に対する村人カテゴリ」のように、妖精が下位概念の場合は1.0を超える値を指定する。
     */
    public final double relevance;

    public final ItemStack fairy;

    public FairyDropEntry(double relevance, ItemStack fairy) {
        this.relevance = relevance;
        this.fairy = fairy;
    }

}
