@file:Suppress("unused")

package  mirrg.kotlin.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive


// 文字列とJavaオブジェクトの相互変換

@Deprecated("暗黙のレシーバにより意図せず呼び出される可能性があります。", ReplaceWith("this.jsonElement.toJson()", "mirrg.kotlin.gson.jsonElement"))
fun Any?.toJson(block: GsonBuilder.() -> Unit): String = GsonBuilder().apply { block() }.create().toJson(this)

@Deprecated("暗黙のレシーバにより意図せず呼び出される可能性があります。", ReplaceWith("this.jsonElement.json", "mirrg.kotlin.gson.jsonElement"))
val Any?.json
    get() = toJson { }

fun JsonElement.toJson(block: GsonBuilder.() -> Unit): String = GsonBuilder().apply { block() }.create().toJson(this)
val JsonElement.json get() = toJson { }
fun String.fromJson(block: GsonBuilder.() -> Unit): Any? = GsonBuilder().apply { block() }.create().fromJson(this, Any::class.java)
val String.fromJson get() = fromJson { }

@JvmName("fromJsonAs")
inline fun <reified T : Any> String.fromJson(block: GsonBuilder.() -> Unit): T? = GsonBuilder().apply { block() }.create().fromJson(this, T::class.java)


// JsonElement化

// List
fun jsonElement(items: List<JsonElement>) = JsonArray().also { items.forEach { items -> it.add(items) } }
fun jsonElement(vararg items: JsonElement) = jsonElement(items.toList())
val List<JsonElement>.jsonElement get() = jsonElement(this)

// Map
fun jsonElement(entries: List<Pair<String, JsonElement>>) = JsonObject().also { entries.forEach { entry -> it.add(entry.first, entry.second) } }
fun jsonElement(vararg entries: Pair<String, JsonElement>) = jsonElement(entries.toList())
val List<Pair<String, JsonElement>>.jsonElement get() = jsonElement(this)
val Map<String, JsonElement>.jsonElement get() = jsonElement(this.entries.map { it.key to it.value })

// プリミティブ
val Number.jsonElement get() = JsonPrimitive(this)
val String.jsonElement get() = JsonPrimitive(this)
val Boolean.jsonElement get() = JsonPrimitive(this)
val Nothing?.jsonElement: JsonElement get() = JsonNull.INSTANCE
