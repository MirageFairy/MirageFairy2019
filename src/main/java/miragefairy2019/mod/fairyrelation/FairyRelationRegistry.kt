package miragefairy2019.mod.fairyrelation

import miragefairy2019.mod.fairy.FairyCard
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.common.BiomeDictionary

object FairyRelationRegistries {
    val biomeType = FairyRelationRegistry<BiomeDictionary.Type>()
    val entity = FairyRelationRegistry<(Entity) -> Boolean>()
    val block = FairyRelationRegistry<Block>()
    val blockState = FairyRelationRegistry<IBlockState>()
    val item = FairyRelationRegistry<Item>()
    val itemStack = FairyRelationRegistry<(ItemStack) -> Boolean>()
    val ingredient = FairyRelationRegistry<Ingredient>()
}

class FairyRelationRegistry<T> : Iterable<FairyRelationEntry<T>> {
    val entries = mutableListOf<FairyRelationEntry<T>>()
    override fun iterator() = entries.iterator()
}

class FairyRelationEntry<out T>(
    private val fairySupplier: () -> FairyCard,
    private val keySupplier: () -> T,
    /**
     * キーと妖精の概念的な近さ（関係性）を表す値です。
     * 1.0のときに標準的な関係性で、0.0のときに完全に無関係です。
     * この値は通常0.5、2.0、4.0のような2の整数乗の値を指定します。
     *
     * その中間は、例えば「『パン』と『食品という概念に対する妖精』」のような、妖精側が通常よりも抽象的である関係を表します。
     * これは、『パン』に対する『パンの妖精』の関係性が1.0であり、それよりも優先度を低くするためです。
     *
     * 逆に「『帯電したクリーパー』と『帯電したクリーパーの妖精』」のような、特定の状態を表す妖精は1よりも大きな数を設定します。
     * これは、『帯電したクリーパー』は『クリーパー』の一種であるため、『クリーパーの妖精』との関係性が1.0と計算されるためです。
     */
    val relevance: Double,
    /**
     * キーの実現難易度に応じた妖精のドロップ率の係数です。
     * この値は、単純にキーに該当している状態を維持するのが困難な場合にバランス調整のために1よりも大きな値を設定してください。
     *
     * 例えば、『木の棒』はインベントリにただ置いておけばよいため、1.0です。
     * 一方『エンダーマン』のようなその場に長時間にわたって保持しておくのが困難な対象がキーの場合には1.0よりも大きな値を設定します。
     * 逆に『平原バイオーム』のような条件を回避する方が難しいキーの場合、低い値に設定します。
     */
    val weight: Double
) {
    val fairyCard get() = fairySupplier()
    val key get() = keySupplier()
}
