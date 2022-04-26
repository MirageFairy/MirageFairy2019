@file:Suppress("unused")

package  mirrg.kotlin.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive


// 文字列とJavaオブジェクトの相互変換

fun JsonElement.toJson(block: GsonBuilder.() -> Unit): String = GsonBuilder().apply { block() }.create().toJson(this)
val JsonElement.json get() = toJson { }
fun String.fromJson(block: GsonBuilder.() -> Unit): Any? = GsonBuilder().apply { block() }.create().fromJson(this, Any::class.java)
val String.fromJson get() = fromJson { }

@JvmName("fromJsonAs")
inline fun <reified T : Any> String.fromJson(block: GsonBuilder.() -> Unit): T? = GsonBuilder().apply { block() }.create().fromJson(this, T::class.java)


// JsonElement化

// List
fun jsonElement(items: List<JsonElement>) = JsonArray().also { items.forEach { item -> it.add(item) } }
fun jsonElementNotNull(items: List<JsonElement?>) = jsonElement(items.filterNotNull())
fun jsonElement(vararg items: JsonElement) = jsonElement(items.toList())
fun jsonElementNotNull(vararg items: JsonElement?) = jsonElementNotNull(items.toList())
val List<JsonElement>.jsonElement get() = jsonElement(this)

// Map
fun jsonElement(entries: List<Pair<String, JsonElement>>) = JsonObject().also { entries.forEach { entry -> it.add(entry.first, entry.second) } }
fun jsonElementNotNull(entries: List<Pair<String, JsonElement>?>) = jsonElement(entries.filterNotNull())
fun jsonElement(vararg entries: Pair<String, JsonElement>) = jsonElement(entries.toList())
fun jsonElementNotNull(vararg entries: Pair<String, JsonElement>?) = jsonElementNotNull(entries.toList())
val List<Pair<String, JsonElement>>.jsonElement get() = jsonElement(this)
val Map<String, JsonElement>.jsonElement get() = jsonElement(this.entries.map { it.key to it.value })

// プリミティブ
val Number.jsonElement get() = JsonPrimitive(this)
val String.jsonElement get() = JsonPrimitive(this)
val Boolean.jsonElement get() = JsonPrimitive(this)
val Nothing?.jsonElement: JsonElement get() = JsonNull.INSTANCE
