package miragefairy2019.export

import com.google.gson.JsonObject
import miragefairy2019.libkt.OreIngredientComplex
import net.minecraft.util.JsonUtils
import net.minecraftforge.common.crafting.IIngredientFactory
import net.minecraftforge.common.crafting.JsonContext

// TODO move: ResourceMakerによりexportではなくなった
/**
 * 耐久が削れたクラフティングツールを鉱石辞書名にマッチさせるためのIngredient
 */
@Suppress("unused") // リフレクション経由で呼ばれる
class IngredientFactoryOreIngredientComplex : IIngredientFactory {
    override fun parse(context: JsonContext, json: JsonObject) = OreIngredientComplex(JsonUtils.getString(json, "ore"))
}
