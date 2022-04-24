package mirrg.kotlin.java.hydrogen

import java.util.Optional

val <T : Any> Optional<T>.orNull: T? get() = orElse(null)
