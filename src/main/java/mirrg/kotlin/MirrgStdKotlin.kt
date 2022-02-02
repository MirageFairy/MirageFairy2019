@file:Suppress("unused")

package mirrg.kotlin

inline fun <reified O : Any> Any.cast(): O = this as O
inline fun <reified O : Any> Any.castOrNull(): O? = this as? O
inline fun <T : Any?> T?.or(default: () -> T) = this ?: default()

fun Iterable<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Iterable<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun Array<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Array<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)
fun Sequence<CharSequence>.join(separator: CharSequence = ", ") = joinToString(separator)
fun <T> Sequence<T>.join(separator: CharSequence = ", ", transform: (T) -> CharSequence) = joinToString(separator, transform = transform)

infix fun Byte.formatAs(format: String) = String.format(format, this)
infix fun Short.formatAs(format: String) = String.format(format, this)
infix fun Int.formatAs(format: String) = String.format(format, this)
infix fun Long.formatAs(format: String) = String.format(format, this)
infix fun Float.formatAs(format: String) = String.format(format, this)
infix fun Double.formatAs(format: String) = String.format(format, this)
