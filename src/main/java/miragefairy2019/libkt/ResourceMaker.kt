package miragefairy2019.libkt

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import mirrg.kotlin.gson.jsonElement
import net.minecraft.block.Block
import net.minecraft.item.Item
import java.io.File

class ResourceMaker(val dirBase: File) {
    private val Any.json get() = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this) + "\n"
    fun File.place(data: Any) = apply { parentFile.mkdirs() }.writeText(data.json)
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


// Block Model

fun ResourceMaker.getBlockModelFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/models/block/${registryName.path}.json")

fun ModInitializer.makeBlockModel(resourceName: ResourceName, creator: () -> JsonElement) = onMakeResource {
    getBlockModelFile(resourceName).place(creator())
}

fun <B : Block> BlockInitializer<B>.makeBlockModel(creator: MakeBlockModelScope<B>.() -> JsonElement) = modInitializer.onMakeResource {
    getBlockModelFile(resourceName).place(MakeBlockModelScope(this@makeBlockModel).creator())
}

class MakeBlockModelScope<B : Block>(val blockInitializer: BlockInitializer<B>)


// Item Model

fun ResourceMaker.getItemModelFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/models/item/${registryName.path}.json")

fun ModInitializer.makeItemModel(resourceName: ResourceName, creator: () -> JsonElement) = onMakeResource {
    getItemModelFile(resourceName).place(creator())
}

fun ModInitializer.makeGeneratedItemModel(resourceName: ResourceName) = makeItemModel(resourceName) { ItemModel.generated(resourceName.map { "items/$it" }) }
fun ModInitializer.makeHandheldItemModel(resourceName: ResourceName) = makeItemModel(resourceName) { ItemModel.handheld(resourceName.map { "items/$it" }) }
fun ModInitializer.makeBlockItemModel(resourceName: ResourceName) = makeItemModel(resourceName) { ItemModel.block(resourceName.map { "block/$it" }) }
fun ModInitializer.makeFluidItemModel(resourceName: ResourceName) = makeItemModel(resourceName) { ItemModel.generated(resourceName.map { "blocks/${it}_still" }) }

object ItemModel

fun ItemModel.generated(resourceName: ResourceName) = jsonElement(
    "parent" to "item/generated".jsonElement,
    "textures" to jsonElement(
        "layer0" to "$resourceName".jsonElement
    )
)

fun ItemModel.handheld(resourceName: ResourceName) = jsonElement(
    "parent" to "item/handheld".jsonElement,
    "textures" to jsonElement(
        "layer0" to "$resourceName".jsonElement
    )
)

fun ItemModel.block(parent: ResourceName) = jsonElement(
    "parent" to "$parent".jsonElement
)

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
