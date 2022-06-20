package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


fun ModInitializer.makeRecipe(path: String, creator: () -> DataRecipe) = onMakeResource {
    place("assets/$modId/recipes/$path.json", creator().jsonElement)
}


abstract class DataRecipe {
    abstract val jsonElement: JsonElement
}

class DataShapedRecipe(
    val group: String? = null,
    val pattern: List<String>,
    val key: Map<String, DataIngredient>,
    val result: DataResult
) : DataRecipe() {
    val type = "forge:ore_shaped"
    override val jsonElement
        get() = jsonObjectNotNull(
            "type" to type.jsonElement,
            group?.let { "group" to it.jsonElement },
            "pattern" to pattern.map { it.jsonElement }.jsonArray,
            "key" to key.mapValues { it.value.jsonElement }.jsonObject,
            "result" to result.jsonElement
        )
}

class DataShapelessRecipe(
    val group: String? = null,
    val ingredients: List<DataIngredient>,
    val result: DataResult
) : DataRecipe() {
    val type = "forge:ore_shapeless"
    override val jsonElement
        get() = jsonObjectNotNull(
            "type" to type.jsonElement,
            group?.let { "group" to it.jsonElement },
            "ingredients" to ingredients.map { it.jsonElement }.jsonArray,
            "result" to result.jsonElement
        )
}


abstract class DataIngredient {
    abstract val jsonElement: JsonElement
}

class DataOreIngredient(
    val type: String = "forge:ore_dict",
    val ore: String
) : DataIngredient() {
    override val jsonElement
        get() = jsonObject(
            "type" to type.jsonElement,
            "ore" to ore.jsonElement
        )
}

class DataSimpleIngredient(
    val type: String? = null,
    val item: String,
    val data: Int? = null,
    val nbt: JsonElement? = null
) : DataIngredient() {
    override val jsonElement
        get() = jsonObjectNotNull(
            type?.let { "type" to it.jsonElement },
            "item" to item.jsonElement,
            data?.let { "data" to it.jsonElement },
            nbt?.let { "nbt" to it }
        )
}

class DataOrIngredient(
    vararg val ingredients: DataIngredient
) : DataIngredient() {
    override val jsonElement get() = ingredients.map { it.jsonElement }.jsonArray
}


class DataResult(
    val item: String,
    val data: Int? = null,
    val count: Int = 1,
    val nbt: JsonElement? = null
) {
    val jsonElement
        get() = jsonObjectNotNull(
            "item" to item.jsonElement,
            data?.let { "data" to it.jsonElement },
            "count" to count.jsonElement,
            nbt?.let { "nbt" to it }
        )
}
