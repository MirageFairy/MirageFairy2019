@file:Suppress("unused")

package mirrg.kotlin

import java.math.BigDecimal
import java.net.URLEncoder

// 関数型演算子
inline fun <reified O : Any> Any.cast(): O = this as O
inline fun <reified O : Any> Any.castOrNull(): O? = this as? O
inline fun <T : Any?> T?.or(default: () -> T) = this ?: default()

// セーフなjoin
fun Iterable<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Iterable<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun Array<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Array<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun Sequence<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Sequence<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

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
val String.urlEncode: String get() = URLEncoder.encode(this, Charsets.UTF_8.name())


// 数学

infix fun BigDecimal.isSameAs(other: BigDecimal) = (this cmp other) == 0
infix fun BigDecimal.isNotSameAs(other: BigDecimal) = (this cmp other) != 0
