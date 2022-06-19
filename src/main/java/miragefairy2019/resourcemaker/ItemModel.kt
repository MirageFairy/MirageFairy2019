package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import miragefairy2019.libkt.ItemInitializer
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.map
import mirrg.kotlin.gson.jsonElement
import net.minecraft.item.Item

fun ModInitializer.makeItemModel(resourceName: ResourceName, creator: () -> JsonElement) = onMakeResource {
    dirBase.resolve("assets/${resourceName.domain}/models/item/${resourceName.path}.json").place(creator())
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

@Deprecated("Deleting")
fun <I : Item> ItemInitializer<I>.makeItemModel(creator: MakeItemModelScope<I>.() -> DataItemModel) = modInitializer.onMakeResource {
    dirBase.resolve("assets/${registryName.domain}/models/item/${registryName.path}.json").place(MakeItemModelScope(this@makeItemModel).creator())
}

class MakeItemModelScope<I : Item>(val itemInitializer: ItemInitializer<I>)

@Deprecated("Deleting")
fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.makeItemVariantModel(creator: MakeItemVariantModelScope<I, V>.() -> DataItemModel) = itemInitializer.modInitializer.onMakeResource {
    dirBase.resolve("assets/${registryName.domain}/models/item/${registryName.path}.json").place(MakeItemVariantModelScope(this@makeItemVariantModel).creator())
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
