package miragefairy2019.libkt

import java.io.File
import java.util.Optional
import java.util.Random

@Suppress("unused")
fun Any?.toUnit() = Unit

val <T : Any> Optional<T>.orNull: T? get() = orElse(null)

val File.existsOrNull get() = if (exists()) this else null
fun File.mkdirsParent() = canonicalFile.parentFile.mkdirs()

fun <K, V : Any> MutableMap<K, V>.setOrRemove(key: K, value: V?): Unit = if (value != null) set(key, value) else remove(key).toUnit()

fun Random.nextInt(range: IntRange) = range.first + nextInt(range.last - range.first + 1)
