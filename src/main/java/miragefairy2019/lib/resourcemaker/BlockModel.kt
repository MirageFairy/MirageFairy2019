package miragefairy2019.lib.resourcemaker

import miragefairy2019.lib.modinitializer.ModScope
import miragefairy2019.lib.modinitializer.NamedScope

fun ModScope.makeBlockModel(path: String, creator: () -> DataModel) = onMakeResource {
    place("assets/$modId/models/block/$path.json", creator().jsonElement)
}

fun NamedScope.makeBlockModel(creator: () -> DataModel) = modScope.onMakeResource {
    place("assets/${resourceName.domain}/models/block/${resourceName.path}.json", creator().jsonElement)
}
