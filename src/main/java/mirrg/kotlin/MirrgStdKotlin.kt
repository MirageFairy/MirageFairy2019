@file:Suppress("unused")

package mirrg.kotlin

import java.math.BigDecimal
import java.net.URLEncoder

fun getClass(name: String): Class<*>? = try {
    Class.forName(name)
} catch (e: ClassNotFoundException) {
    null
}

// 関数型演算子
inline fun <reified O : Any> Any.cast(): O = this as O
inline fun <reified O : Any> Any.castOrNull(): O? = this as? O
inline fun <T : Any?> T?.or(default: () -> T) = this ?: default()

val <T> List<T>.orNull get() = takeIf { isNotEmpty() }

// セーフなjoin
fun Iterable<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Iterable<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun Array<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Array<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun Sequence<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Sequence<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

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

// 中置format
infix fun Byte.formatAs(format: String) = String.format(format, this)
infix fun Short.formatAs(format: String) = String.format(format, this)
infix fun Int.formatAs(format: String) = String.format(format, this)
infix fun Long.formatAs(format: String) = String.format(format, this)
infix fun Float.formatAs(format: String) = String.format(format, this)
infix fun Double.formatAs(format: String) = String.format(format, this)

// 比較
infix fun <T : Comparable<T>> T.min(other: T) = if (this <= other) this else other
infix fun <T : Comparable<T>> T.max(other: T) = if (this >= other) this else other
infix fun <T : Comparable<T>> T.cmp(other: T) = compareTo(other)

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

infix fun Byte.atMost(other: Byte) = coerceAtMost(other)
infix fun Byte.atLeast(other: Byte) = coerceAtLeast(other)
infix fun Short.atMost(other: Short) = coerceAtMost(other)
infix fun Short.atLeast(other: Short) = coerceAtLeast(other)
infix fun Int.atMost(other: Int) = coerceAtMost(other)
infix fun Int.atLeast(other: Int) = coerceAtLeast(other)
infix fun Long.atMost(other: Long) = coerceAtMost(other)
infix fun Long.atLeast(other: Long) = coerceAtLeast(other)
infix fun Float.atMost(other: Float) = coerceAtMost(other)
infix fun Float.atLeast(other: Float) = coerceAtLeast(other)
infix fun Double.atMost(other: Double) = coerceAtMost(other)
infix fun Double.atLeast(other: Double) = coerceAtLeast(other)
infix fun <T : Comparable<T>> T.atMost(other: T) = coerceAtMost(other)
infix fun <T : Comparable<T>> T.atLeast(other: T) = coerceAtLeast(other)
