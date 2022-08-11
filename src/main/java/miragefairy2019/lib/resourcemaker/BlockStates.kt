package miragefairy2019.lib.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.common.ResourceName
import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.NamedScope
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


fun ModScope.makeBlockStates(path: String, creator: MakeBlockStatesScope.() -> DataModelBlockDefinition) = onMakeResource {
    place("assets/$modId/blockstates/$path.json", MakeBlockStatesScope(ResourceName(modId, path)).creator().jsonElement)
}

fun NamedScope.makeBlockStates(creator: MakeBlockStatesScope.() -> DataModelBlockDefinition) = modScope.onMakeResource {
    place("assets/${resourceName.domain}/blockstates/${resourceName.path}.json", MakeBlockStatesScope(resourceName).creator().jsonElement)
}

class MakeBlockStatesScope(val resourceName: ResourceName)


/** [net.minecraft.client.renderer.block.model.ModelBlockDefinition] */
data class DataModelBlockDefinition(
    val forgeMarker: Int? = null,
    val variants: Map<String, DataVariantList>? = null,
    val multipart: List<DataSelector>? = null
) {
    val jsonElement = jsonObjectNotNull(
        "forge_marker" to forgeMarker?.jsonElement,
        "variants" to variants?.mapValues { (_, it) -> it.jsonElement }?.jsonObject,
        "multipart" to multipart?.map { it.jsonElement }?.jsonArray
    )
}

abstract class DataVariantList {
    abstract val jsonElement: JsonElement
}

/** [net.minecraft.client.renderer.block.model.VariantList] */
data class DataRandomVariantList(
    val variants: List<DataVariant>
) : DataVariantList() {
    constructor(vararg variants: DataVariant) : this(variants.toList())

    override val jsonElement = variants.map { it.jsonElement }.jsonArray
}

/** [net.minecraft.client.renderer.block.model.VariantList] */
data class DataSingleVariantList(
    val variant: DataVariant
) : DataVariantList() {
    override val jsonElement = variant.jsonElement
}

/** [net.minecraft.client.renderer.block.model.Variant] */
data class DataVariant(
    val model: String,
    val x: Int? = null,
    val y: Int? = null,
    val custom: JsonElement? = null
) {
    constructor(model: ResourceName, x: Int? = null, y: Int? = null) : this(model.toString(), x, y)

    val jsonElement = jsonObjectNotNull(
        "model" to model.jsonElement,
        "x" to x?.jsonElement,
        "y" to y?.jsonElement,
        "custom" to custom
    )
}

/** [net.minecraft.client.renderer.block.model.multipart.Selector] */
data class DataSelector(
    val `when`: Map<String, JsonElement>? = null,
    val apply: DataVariant
) {
    val jsonElement = jsonObjectNotNull(
        "when" to `when`?.jsonObject,
        "apply" to apply.jsonElement
    )
}


val MakeBlockStatesScope.normal get() = DataModelBlockDefinition(variants = mapOf("normal" to DataSingleVariantList(DataVariant(resourceName))))
val MakeBlockStatesScope.fluid
    get() = DataModelBlockDefinition(
        forgeMarker = 1,
        variants = mapOf(
            "fluid" to DataSingleVariantList(
                DataVariant(
                    model = "forge:fluid",
                    custom = jsonObject(
                        "fluid" to resourceName.path.jsonElement
                    )
                )
            )
        )
    )
