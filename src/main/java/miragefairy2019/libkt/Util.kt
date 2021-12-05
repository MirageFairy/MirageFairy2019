package miragefairy2019.libkt

import java.util.Optional

val <T : Any> Optional<T>.orNull: T? get() = orElse(null)