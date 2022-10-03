package miragefairy2019.mod.aura

import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main

lateinit var itemAuraBook: () -> ItemAuraBook

val auraModule = module {

    // オーラ検査キット
    itemAuraBook = item({ ItemAuraBook() }, "aura_book") {
        setUnlocalizedName("auraBook")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        makeItemModel { generated }
        makeRecipe {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:writable_book"),
                    DataOreIngredient(ore = "mirageFairyCrystal"),
                    DataOreIngredient(ore = "dustMiragium"),
                    DataOreIngredient(ore = "gemQuartz")
                ),
                result = DataResult(item = "miragefairy2019:aura_book")
            )
        }
    }
    onMakeLang { enJa("item.auraBook.name", "Aura Test Kit", "オーラ検査キット") }

}
