package miragefairy2019.mod.systems

import miragefairy2019.api.IManualRepairAcceptorItem
import miragefairy2019.lib.RecipeBase
import miragefairy2019.lib.RecipeInput
import miragefairy2019.lib.RecipeMatcher
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.castOrNull
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

val manualRepairModule = module {
    onAddRecipe {
        GameRegistry.findRegistry(IRecipe::class.java).register(RecipeManualRepair(ResourceLocation(ModMirageFairy2019.MODID, "manual_repair")))
    }
}

class RecipeManualRepair(registryName: ResourceLocation) : RecipeBase<RecipeManualRepair.Result>(registryName) {
    class Result(val target: RecipeInput<IManualRepairAcceptorItem>)

    override fun match(matcher: RecipeMatcher): Result? {

        // 手入れ対象
        val target = matcher.pull { itemStack ->
            val item = itemStack.item.castOrNull<IManualRepairAcceptorItem>() ?: return@pull null
            if (!item.canManualRepair(itemStack)) return@pull null
            item
        } ?: return null

        // 手入れ素材のリスト
        val requirements = target.tag.getManualRepairRequirements(target.itemStack)
        if (requirements.size == 0) return null // 手入れ用素材が登録されていない

        // 手入れ素材判定
        requirements.forEach { requirement ->
            matcher.pullMatched { requirement.test(it) } ?: return null
        }

        if (matcher.hasRemaining()) return null

        return Result(target)
    }

    override fun canFit(width: Int, height: Int) = width * height >= 1
    override fun getCraftingResult(result: Result) = result.target.tag.getManualRepairedItem(result.target.itemStack)

    override fun getRemainingItem(result: Result, index: Int, itemStack: ItemStack) = if (index == result.target.index) {
        EMPTY_ITEM_STACK // 手入れ対象のアイテムはコンテナを残さない
    } else {
        super.getRemainingItem(result, index, itemStack)
    }
}
