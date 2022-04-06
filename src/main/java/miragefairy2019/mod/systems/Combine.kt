package miragefairy2019.mod.systems

import miragefairy2019.api.ICombineAcceptorItem
import miragefairy2019.api.ICombineHandler
import miragefairy2019.api.ICombineResult
import miragefairy2019.lib.RecipeInput
import miragefairy2019.lib.RecipeMatcher
import miragefairy2019.lib.toNonNullList
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.itemStacks
import miragefairy2019.libkt.module
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

    private class Result(val main: RecipeInput<ICombineHandler>, val combineResult: ICombineResult)

    private fun match(inventoryCrafting: InventoryCrafting): Result? {
        val matcher = RecipeMatcher(inventoryCrafting)

        val main = matcher.pull { it.item.castOrNull<ICombineAcceptorItem>()?.getCombineHandler(it.copy(1)) } ?: return null
        val combineResult = matcher.pull { if (it.isEmpty) null else main.tag.combineWith(it.copy(1)) }?.tag
            ?: main.tag.combineWith(EMPTY_ITEM_STACK) // ヒットしなかったので分解モードを試す
            ?: return null // 分解も不能なら中止

        if (matcher.hasRemaining()) return null

        return Result(main, combineResult)
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
            if (i == result.main.index) {
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
