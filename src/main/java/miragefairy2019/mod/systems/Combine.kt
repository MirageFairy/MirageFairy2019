package miragefairy2019.mod.systems

import miragefairy2019.api.ICombineAcceptorItem
import miragefairy2019.api.ICombineHandler
import miragefairy2019.api.ICombineResult
import miragefairy2019.lib.RecipeBase
import miragefairy2019.lib.RecipeInput
import miragefairy2019.lib.RecipeMatcher
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

object Combine {
    val module = module {
        onAddRecipe {
            GameRegistry.findRegistry(IRecipe::class.java).register(RecipesCombine(ResourceLocation(ModMirageFairy2019.MODID, "combine")))
        }
    }
}

class RecipesCombine(registryName: ResourceLocation) : RecipeBase<RecipesCombine.Result>(registryName) {
    class Result(val main: RecipeInput<ICombineHandler>, val combineResult: ICombineResult)

    override fun match(matcher: RecipeMatcher): Result? {

        val main = matcher.pull { it.item.castOrNull<ICombineAcceptorItem>()?.getCombineHandler(it.copy(1)) } ?: return null
        val combineResult = matcher.pull { if (it.isEmpty) null else main.tag.combineWith(it.copy(1)) }?.tag
            ?: main.tag.combineWith(EMPTY_ITEM_STACK) // ヒットしなかったので分解モードを試す
            ?: return null // 分解も不能なら中止

        if (matcher.hasRemaining()) return null

        return Result(main, combineResult)
    }

    override fun canFit(width: Int, height: Int) = width * height >= 1
    override fun getCraftingResult(result: Result) = result.combineResult.combinedItem

    override fun getRemainingItem(result: Result, index: Int, itemStack: ItemStack) = if (index == result.main.index) {
        // 主体アイテムの場合
        // クラフティングアイテムに合成しても耐久が削れたものは残らない
        // 代わりにそれまで合成されていたパーツが出てくる
        result.combineResult.remainingItem
    } else {
        super.getRemainingItem(result, index, itemStack)
    }
}
