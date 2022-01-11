package miragefairy2019.libkt

import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient

val Item.ingredient: Ingredient get() = ItemStack(this, 1, OreDictionary.WILDCARD_VALUE).ingredient
val Block.ingredient: Ingredient?
    get() {
        val item = Item.getItemFromBlock(this)
        if (item == Items.AIR) return null
        return item.ingredient
    }
val ItemStack.ingredient: Ingredient get() = Ingredient.fromStacks(this)
val String.oreIngredient: OreIngredient get() = OreIngredient(this)
