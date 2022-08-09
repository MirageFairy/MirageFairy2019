package miragefairy2019.lib.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.common.ResourceName
import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.NamedScope
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


fun ModScope.makeBlockStates(path: String, creator: MakeBlockStatesScope.() -> DataBlockStates) = onMakeResource {
    place("assets/$modId/blockstates/$path.json", MakeBlockStatesScope(ResourceName(modId, path)).creator().jsonElement)
}

fun NamedScope.makeBlockStates(creator: MakeBlockStatesScope.() -> DataBlockStates) = modScope.onMakeResource {
    place("assets/${resourceName.domain}/blockstates/${resourceName.path}.json", MakeBlockStatesScope(resourceName).creator().jsonElement)
}

class MakeBlockStatesScope(val resourceName: ResourceName)


data class DataBlockStates(
    val forgeMarker: Int? = null,
    val variants: Map<String, DataBlockState>? = null,
    val multipart: List<DataPart>? = null
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

data class DataArrayVariantList(
    val variants: List<DataBlockState>
) : DataVariantList() {
    constructor(vararg variants: DataBlockState) : this(variants.toList())

    override val jsonElement = variants.map { it.jsonElement }.jsonArray
}

data class DataSingleVariantList(
    val variant: DataBlockState
) : DataVariantList() {
    override val jsonElement = variant.jsonElement
}

data class DataBlockState(
    val model: String,
    val x: Int? = null,
    val y: Int? = null,
    val z: Int? = null,
    val custom: JsonElement? = null
) {
    constructor(model: ResourceName, x: Int? = null, y: Int? = null, z: Int? = null) : this(model.toString(), x, y, z)

    val jsonElement = jsonObjectNotNull(
        "model" to model.jsonElement,
        "x" to x?.jsonElement,
        "y" to y?.jsonElement,
        "z" to z?.jsonElement,
        "custom" to custom
    )
}

data class DataPart(
    val `when`: Map<String, JsonElement>? = null,
    val apply: DataBlockState
) {
    val jsonElement = jsonObjectNotNull(
        "when" to `when`?.jsonObject,
        "apply" to apply.jsonElement
    )
}


val MakeBlockStatesScope.normal get() = DataBlockStates(variants = mapOf("normal" to DataBlockState(resourceName)))
val MakeBlockStatesScope.fluid
    get() = DataBlockStates(
        forgeMarker = 1,
        variants = mapOf(
            "fluid" to DataBlockState(
                model = "forge:fluid",
                custom = jsonObject(
                    "fluid" to resourceName.path.jsonElement
                )
            )
        )
    )
