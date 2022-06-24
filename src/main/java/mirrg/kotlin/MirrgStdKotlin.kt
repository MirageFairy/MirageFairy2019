@file:Suppress("unused")

package mirrg.kotlin

import mirrg.kotlin.hydrogen.cmp
import java.math.BigDecimal
import java.net.URLEncoder

fun getClass(name: String): Class<*>? = try {
    Class.forName(name)
} catch (e: ClassNotFoundException) {
    null
}


fun <T> Iterable<T>.separate(separators: Iterable<T>): List<T> {
    val i = iterator()
    if (!i.hasNext()) return emptyList()
    val left = mutableListOf<T>()
    left += i.next()
    while (i.hasNext()) {
        left += separators
        left += i.next()
    }
    return left
}

fun <T> Iterable<T>.separateWith(vararg separators: T) = separate(separators.asIterable())

fun <T> Iterable<Iterable<T>>.concatWith(separator: Iterable<T>): List<T> {
    val i = iterator()
    if (!i.hasNext()) return emptyList()
    val left = mutableListOf<T>()
    left += i.next()
    while (i.hasNext()) {
        left += separator
        left += i.next()
    }
    return left
}

@Deprecated(message = "削除予定", replaceWith = ReplaceWith("this.concatWith(separator)"))
fun <T> Iterable<Iterable<T>>.concat(vararg separator: T) = concatWith(separator.asIterable())
fun <T> Iterable<Iterable<T>>.concatWith(vararg separator: T) = concatWith(separator.asIterable())
fun <T> concat(vararg listOfList: Iterable<T>) = listOfList.asIterable().flatten()
fun <T> concatNotNull(vararg listOfList: Iterable<T>?) = listOfList.filterNotNull().flatten()


// 文字列

val ByteArray.utf8String get() = toString(Charsets.UTF_8)
val String.utf8ByteArray get() = toByteArray()

@Deprecated("removing", ReplaceWith("urlEncoded"))
val String.urlEncode
    get() = urlEncoded
val String.urlEncoded: String get() = URLEncoder.encode(this, Charsets.UTF_8.name())
val String.htmlEscaped
    get() = this
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")


// 数学

infix fun BigDecimal.isSameAs(other: BigDecimal) = (this cmp other) == 0
infix fun BigDecimal.isNotSameAs(other: BigDecimal) = (this cmp other) != 0
