package miragefairy2019.libkt

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import miragefairy2019.mod.lib.ItemMulti
import miragefairy2019.mod.lib.ItemVariant
import mirrg.kotlin.gson.jsonElement
import mirrg.kotlin.gson.jsonElementNotNull
import net.minecraft.block.Block
import net.minecraft.item.Item
import java.io.File

class ResourceMaker(val dirBase: File) {
    private val Any.json get() = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this) + "\n"
    fun File.place(data: Any) = apply { parentFile.mkdirs() }.writeText(data.json)
}


// Recipe

fun ResourceMaker.getRecipeFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/recipes/${registryName.path}.json")

fun ModInitializer.makeRecipe(registryName: ResourceName, recipe: DataRecipe) = onMakeResource {
    getRecipeFile(registryName).place(recipe.jsonElement)
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
    val item: String,
    val data: Int? = null
) : DataIngredient() {
    override val jsonElement
        get() = jsonElementNotNull(
            "item" to item.jsonElement,
            data?.let { "data" to it.jsonElement }
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
    val count: Int = 1
) {
    val jsonElement
        get() = jsonElementNotNull(
            "item" to item.jsonElement,
            data?.let { "data" to it.jsonElement },
            "count" to count.jsonElement
        )
}


// BlockStates

fun ResourceMaker.getBlockStatesFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/blockstates/${registryName.path}.json")

fun <B : Block> BlockInitializer<B>.makeBlockStates(creator: MakeBlockStatesScope<B>.() -> DataBlockStates) = modInitializer.onMakeResource {
    getBlockStatesFile(resourceName).place(MakeBlockStatesScope(this@makeBlockStates).creator())
}

class MakeBlockStatesScope<B : Block>(val blockInitializer: BlockInitializer<B>)

data class DataBlockStates(
    @Expose val variants: Map<String, DataBlockState>? = null,
    @Expose val multipart: List<DataPart>? = null
)

data class DataBlockState(
    @Expose val model: String,
    @Expose val x: Int? = null,
    @Expose val y: Int? = null,
    @Expose val z: Int? = null
) {
    constructor(model: ResourceName, x: Int? = null, y: Int? = null, z: Int? = null) : this(model.toString(), x, y, z)
}

val <B : Block> MakeBlockStatesScope<B>.normal get() = DataBlockStates(variants = mapOf("normal" to DataBlockState(blockInitializer.resourceName)))

data class DataPart(val `when`: Map<String, Any>? = null, val apply: DataBlockState)


// Item Model

fun ResourceMaker.getItemModelFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/models/item/${registryName.path}.json")

fun <I : Item> ItemInitializer<I>.makeItemModel(creator: MakeItemModelScope<I>.() -> DataItemModel) = modInitializer.onMakeResource {
    getItemModelFile(registryName).place(MakeItemModelScope(this@makeItemModel).creator())
}

class MakeItemModelScope<I : Item>(val itemInitializer: ItemInitializer<I>)

fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.makeItemVariantModel(creator: MakeItemVariantModelScope<I, V>.() -> DataItemModel) = itemInitializer.modInitializer.onMakeResource {
    getItemModelFile(registryName).place(MakeItemVariantModelScope(this@makeItemVariantModel).creator())
}

class MakeItemVariantModelScope<I : ItemMulti<V>, V : ItemVariant>(val itemVariantInitializer: ItemVariantInitializer<I, V>)

data class DataItemModel(
    @Expose val parent: String,
    @Expose val elements: JsonElement? = null,
    @Expose val textures: Map<String, String>? = null
)

fun <I : Item> MakeItemModelScope<I>.getStandardItemModel(parent: String) = DataItemModel(
    parent = parent,
    textures = mapOf("layer0" to "${itemInitializer.registryName.domain}:items/${itemInitializer.registryName.path}")
)

fun <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.getStandardItemModel(parent: String) = DataItemModel(
    parent = parent,
    textures = mapOf("layer0" to "${itemVariantInitializer.registryName.domain}:items/${itemVariantInitializer.registryName.path}")
)

val <I : Item> MakeItemModelScope<I>.generated get() = getStandardItemModel("item/generated")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.generated get() = getStandardItemModel("item/generated")
val <I : Item> MakeItemModelScope<I>.handheld get() = getStandardItemModel("item/handheld")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.handheld get() = getStandardItemModel("item/handheld")
val <I : Item> MakeItemModelScope<I>.block get() = DataItemModel(parent = "${itemInitializer.registryName.domain}:block/${itemInitializer.registryName.path}")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.block get() = DataItemModel(parent = "${itemVariantInitializer.registryName.domain}:block/${itemVariantInitializer.registryName.path}")
