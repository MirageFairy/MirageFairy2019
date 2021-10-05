package miragefairy2019.libkt

inline fun <O, I : O, S : O> Iterable<I>.sandwich(separator: () -> S): List<O> {
    val input = toList()
    val output = mutableListOf<O>()
    var isFirst = true
    input.forEach {
        if (isFirst) {
            isFirst = false
        } else {
            output += separator()
        }
        output += it
    }
    return output
}
