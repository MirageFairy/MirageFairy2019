package miragefairy2019.libkt

import net.minecraftforge.oredict.OreIngredient

class OreIngredientComplex(ore: String) : OreIngredient(ore) {
    // これがtrueになっていると、subItemsを参照するようになるのでクラフティングツールが反応しなくなる
    override fun isSimple(): Boolean = false
}
