package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName

fun ModInitializer.makeBlockModel(resourceName: ResourceName, creator: () -> JsonElement) = onMakeResource {
    fun ResourceMaker.getBlockModelFile(registryName: ResourceName) = dirBase.resolve("assets/${registryName.domain}/models/block/${registryName.path}.json")
    getBlockModelFile(resourceName).place(creator())
}
