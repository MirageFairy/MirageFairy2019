package miragefairy2019.common

data class OreName(val string: String)

fun String.toOreName() = OreName(this)
