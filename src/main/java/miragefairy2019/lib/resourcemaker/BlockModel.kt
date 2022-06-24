package miragefairy2019.lib.resourcemaker

import miragefairy2019.lib.modinitializer.ModInitializer
import miragefairy2019.lib.modinitializer.NamedInitializer

fun ModInitializer.makeBlockModel(path: String, creator: () -> DataModel) = onMakeResource {
    place("assets/$modId/models/block/$path.json", creator().jsonElement)
}

fun NamedInitializer.makeBlockModel(creator: () -> DataModel) = modInitializer.onMakeResource {
    place("assets/${resourceName.domain}/models/block/${resourceName.path}.json", creator().jsonElement)
}
