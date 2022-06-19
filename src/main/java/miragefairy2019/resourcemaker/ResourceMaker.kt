package miragefairy2019.resourcemaker

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import miragefairy2019.libkt.BlockInitializer
import miragefairy2019.libkt.ItemInitializer
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.map
import mirrg.kotlin.gson.jsonElement
import net.minecraft.block.Block
import net.minecraft.item.Item
import java.io.File

class ResourceMaker(val dirBase: File) {
    private val Any.json get() = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this) + "\n"
    fun File.place(data: Any) = apply { parentFile.mkdirs() }.writeText(data.json)
}


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
