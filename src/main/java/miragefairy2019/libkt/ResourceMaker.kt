package miragefairy2019.libkt

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import net.minecraft.block.Block
import net.minecraft.util.ResourceLocation
import java.io.File

class ResourceMaker(val dirBase: File) {
    private val Any.json get() = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(this) + "\n"
    fun File.place(data: Any) = apply { parentFile.mkdirs() }.writeText(data.json)
}


// BlockStates

fun ResourceMaker.getBlockStatesFile(registryName: ResourceLocation) = dirBase.resolve("assets/${registryName.resourceDomain}/blockstates/${registryName.resourcePath}.json")

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
    constructor(model: ResourceLocation, x: Int? = null, y: Int? = null, z: Int? = null) : this(model.toString(), x, y, z)
}

val <B : Block> MakeBlockStatesScope<B>.normal get() = DataBlockStates("normal" to DataBlockState(blockInitializer.registryName))
