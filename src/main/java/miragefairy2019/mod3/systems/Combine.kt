package miragefairy2019.mod3.systems

import miragefairy2019.api.ICombineAcceptorItem
import miragefairy2019.api.ICombineResult
import miragefairy2019.lib.toNonNullList
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.module
import miragefairy2019.libkt.size
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.castOrNull
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

object Combine {
    val module = module {
        onAddRecipe {
            GameRegistry.findRegistry(IRecipe::class.java).register(RecipesCombine())
        }
    }
}

class RecipesCombine : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        registryName = ResourceLocation(ModMirageFairy2019.MODID, "combine")
    }


    // match

    private class Result(val mainItemStackIndex: Int, val combineResult: ICombineResult)

    private fun match(inventoryCrafting: InventoryCrafting): Result? {
        val used = Array(inventoryCrafting.size) { false }

        // 主体探索
        val (mainItemStackIndex, combineHandler) = run found@{
            inventoryCrafting.itemStacks.forEachIndexed next@{ i, itemStack ->
                if (used[i]) return@next // 使用済みのスロットならスルー
                if (itemStack.isEmpty) return@next // 空欄ならスルー
                val combineHandler = itemStack.item.castOrNull<ICombineAcceptorItem>()?.getCombineHandler(itemStack.copy(1)) ?: return@next // 合成非対応ならスルー
                used[i] = true
                return@found Pair(i, combineHandler) // ヒットした
            }
            return null // ヒットしなかったので中止
        }

        // 部品探索
        val combineResult = run found@{
            inventoryCrafting.itemStacks.forEachIndexed next@{ i, itemStack ->
                if (used[i]) return@next // 使用済みのスロットならスルー
                if (itemStack.isEmpty) return@next // 空欄ならスルー
                val combineResult = combineHandler.combineWith(itemStack.copy(1)) ?: return@next // 合成不能ならスルー
                used[i] = true
                return@found combineResult
            }

            // ヒットしなかったので分解モード
            val combineResult = combineHandler.combineWith(EMPTY_ITEM_STACK) ?: return null // 合成不能なら中止
            combineResult
        }

        // 余りがあった場合は中止
        inventoryCrafting.itemStacks.forEachIndexed next@{ i, itemStack ->
            if (used[i]) return@next // 使用済みのスロットならスルー
            if (itemStack.isEmpty) return@next // 空欄ならスルー
            return null // 余りがあったので中止
        }

        return Result(mainItemStackIndex, combineResult)
    }


    // overrides

    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 1
    override fun matches(inventoryCrafting: InventoryCrafting, world: World) = match(inventoryCrafting) != null
    override fun getRecipeOutput() = EMPTY_ITEM_STACK

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack {
        val result = match(inventoryCrafting) ?: return EMPTY_ITEM_STACK
        return result.combineResult.combinedItem
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): NonNullList<ItemStack> {
        val result = match(inventoryCrafting) ?: return NonNullList.create()
        return inventoryCrafting.itemStacks.mapIndexed { i, itemStack ->
            if (i == result.mainItemStackIndex) {
                // 主体アイテムの場合
                // クラフティングアイテムに合成しても耐久が削れたものは残らない
                // 代わりにそれまで合成されていたパーツが出てくる
                result.combineResult.remainingItem
            } else {
                // それ以外のアイテムは普通にコンテナアイテムが残る
                ForgeHooks.getContainerItem(itemStack)
            }
        }.toNonNullList()
    }

}
