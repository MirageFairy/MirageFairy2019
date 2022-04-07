package miragefairy2019.mod.systems

import miragefairy2019.api.IManualRepairAcceptorItem
import miragefairy2019.lib.RecipeInput
import miragefairy2019.lib.RecipeMatcher
import miragefairy2019.lib.toNonNullList
import miragefairy2019.libkt.EMPTY_ITEM_STACK
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

object ManualRepair {
    val module = module {
        onAddRecipe {
            GameRegistry.findRegistry(IRecipe::class.java).register(RecipeManualRepair())
        }
    }
}

class RecipeManualRepair : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        registryName = ResourceLocation(ModMirageFairy2019.MODID, "manual_repair")
    }


    // match

    private class Result(val target: RecipeInput<IManualRepairAcceptorItem>)

    private fun match(inventoryCrafting: InventoryCrafting): Result? {
        val matcher = RecipeMatcher(inventoryCrafting)

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


    // overrides

    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 1
    override fun matches(inventoryCrafting: InventoryCrafting, world: World) = match(inventoryCrafting) != null
    override fun getRecipeOutput() = EMPTY_ITEM_STACK

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack {
        val result = match(inventoryCrafting) ?: return EMPTY_ITEM_STACK
        return result.target.tag.getManualRepairedItem(result.target.itemStack)
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): NonNullList<ItemStack> {
        val result = match(inventoryCrafting) ?: return NonNullList.create()
        return inventoryCrafting.itemStacks.mapIndexed { index, itemStack ->
            if (index == result.target.index) {
                EMPTY_ITEM_STACK // 手入れ対象のアイテムはコンテナを残さない
            } else {
                ForgeHooks.getContainerItem(itemStack)
            }
        }.toNonNullList()
    }

}
