package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull


fun ModInitializer.makeBlockStates(path: String, creator: MakeBlockStatesScope.() -> DataBlockStates) = onMakeResource {
    place("assets/$modId/blockstates/$path.json", MakeBlockStatesScope(ResourceName(modId, path)).creator().jsonElement)
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
