package miragefairy2019.resourcemaker

import com.google.gson.annotations.Expose
import miragefairy2019.libkt.BlockInitializer
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import net.minecraft.block.Block

@Deprecated("")
fun <B : Block> BlockInitializer<B>.makeBlockStates(creator: MakeBlockStatesScope.() -> DataBlockStates) = modInitializer.makeBlockStates(resourceName, creator)


fun ModInitializer.makeBlockStates(resourceName: ResourceName, creator: MakeBlockStatesScope.() -> DataBlockStates) = onMakeResource {
    dirBase.resolve("assets/${resourceName.domain}/blockstates/${resourceName.path}.json").place(MakeBlockStatesScope(resourceName).creator())
}

class MakeBlockStatesScope(val resourceName: ResourceName)


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

data class DataPart(val `when`: Map<String, Any>? = null, val apply: DataBlockState)


val MakeBlockStatesScope.normal get() = DataBlockStates(variants = mapOf("normal" to DataBlockState(resourceName)))
