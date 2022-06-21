package miragefairy2019.common

data class ResourceName(val domain: String, val path: String) {
    override fun toString() = "$domain:$path"
}

fun ResourceName.map(function: (String) -> String) = ResourceName(domain, function(path))
