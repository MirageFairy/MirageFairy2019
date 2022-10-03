package miragefairy2019.mod.aura

import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeItemModel
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
    }
    onMakeLang { enJa("item.auraBook.name", "Aura Test Kit", "オーラ検査キット") }

}
