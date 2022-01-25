package miragefairy2019.libkt

import java.util.Optional

val <T : Any> Optional<T>.orNull: T? get() = orElse(null)

inline fun <reified O : Any> Any.cast(): O = this as O
inline fun <reified O : Any> Any.castOrNull(): O? = this as? O
