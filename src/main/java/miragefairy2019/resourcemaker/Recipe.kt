package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonElementNotNull

fun ModInitializer.makeRecipe(registryName: ResourceName, recipe: DataRecipe) = onMakeResource {
    dirBase.resolve("assets/${registryName.domain}/recipes/${registryName.path}.json").place(recipe.jsonElement)
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
        get() = jsonElementNotNull(
            "type" to type.jsonElement,
            group?.let { "group" to it.jsonElement },
            "pattern" to pattern.map { it.jsonElement }.jsonElement,
            "key" to key.mapValues { it.value.jsonElement }.jsonElement,
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
        get() = jsonElementNotNull(
            "type" to type.jsonElement,
            group?.let { "group" to it.jsonElement },
            "ingredients" to ingredients.map { it.jsonElement }.jsonElement,
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
        get() = jsonElement(
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
        get() = jsonElementNotNull(
            type?.let { "type" to it.jsonElement },
            "item" to item.jsonElement,
            data?.let { "data" to it.jsonElement },
            nbt?.let { "nbt" to it }
        )
}

class DataOrIngredient(
    vararg val ingredients: DataIngredient
) : DataIngredient() {
    override val jsonElement get() = ingredients.map { it.jsonElement }.jsonElement
}


class DataResult(
    val item: String,
    val data: Int? = null,
    val count: Int = 1,
    val nbt: JsonElement? = null
) {
    val jsonElement
        get() = jsonElementNotNull(
            "item" to item.jsonElement,
            data?.let { "data" to it.jsonElement },
            "count" to count.jsonElement,
            nbt?.let { "nbt" to it }
        )
}
