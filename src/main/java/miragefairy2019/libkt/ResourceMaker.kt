package miragefairy2019.libkt

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import miragefairy2019.mod.lib.multi.ItemMulti
import miragefairy2019.mod.lib.multi.ItemVariant
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
    getRecipeFile(registryName).place(recipe)
}


abstract class DataRecipe

data class DataShapedRecipe(
    @Expose val group: String? = null,
    @Expose val pattern: List<String>,
    @Expose val key: Map<String, DataIngredient>,
    @Expose val result: DataResult
) : DataRecipe() {
    @Expose
    val type = "forge:ore_shaped"
}

data class DataShapelessRecipe(
    @Expose val group: String? = null,
    @Expose val ingredients: List<DataIngredient>,
    @Expose val result: DataResult
) : DataRecipe() {
    @Expose
    val type = "forge:ore_shapeless"
}


abstract class DataIngredient

data class DataOreIngredient(
    @Expose val type: String = "forge:ore_dict",
    @Expose val ore: String
) : DataIngredient()

data class DataSimpleIngredient(
    @Expose val item: String,
    @Expose val data: Int? = null
) : DataIngredient()

data class DataResult(
    @Expose val item: String,
    @Expose val data: Int? = null,
    @Expose val count: Int = 1
)


// BlockStates

fun ResourceMaker.getBlockStatesFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/blockstates/${registryName.path}.json")

fun <B : Block> BlockInitializer<B>.makeBlockStates(creator: MakeBlockStatesScope<B>.() -> DataBlockStates) = modInitializer.onMakeResource {
    getBlockStatesFile(registryName).place(MakeBlockStatesScope(this@makeBlockStates).creator())
}

class MakeBlockStatesScope<B : Block>(val blockInitializer: BlockInitializer<B>)

data class DataBlockStates(
    @Expose val variants: Map<String, DataBlockState>
) {
    constructor(vararg pairs: Pair<String, DataBlockState>) : this(pairs.toMap())
    constructor(pairs: Iterable<Pair<String, DataBlockState>>) : this(pairs.toMap())
}

data class DataBlockState(
    @Expose val model: String,
    @Expose val x: Int? = null,
    @Expose val y: Int? = null,
    @Expose val z: Int? = null
) {
    constructor(model: ResourceName, x: Int? = null, y: Int? = null, z: Int? = null) : this(model.toString(), x, y, z)
}

val <B : Block> MakeBlockStatesScope<B>.normal get() = DataBlockStates("normal" to DataBlockState(blockInitializer.registryName))


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
    @Expose val textures: Map<String, String>? = null
)

fun <I : Item> MakeItemModelScope<I>.getStandardItemModel(parent: String) = DataItemModel(
    parent,
    mapOf("layer0" to "${itemInitializer.registryName.domain}:items/${itemInitializer.registryName.path}")
)

fun <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.getStandardItemModel(parent: String) = DataItemModel(
    parent,
    mapOf("layer0" to "${itemVariantInitializer.registryName.domain}:items/${itemVariantInitializer.registryName.path}")
)

val <I : Item> MakeItemModelScope<I>.generated get() = getStandardItemModel("item/generated")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.generated get() = getStandardItemModel("item/generated")
val <I : Item> MakeItemModelScope<I>.handheld get() = getStandardItemModel("item/handheld")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.handheld get() = getStandardItemModel("item/handheld")
