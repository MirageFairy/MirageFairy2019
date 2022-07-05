@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package mirrg.kotlin.gson.hydrogen

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import mirrg.kotlin.hydrogen.castOrNull

class JsonWrapper2(val jsonElement: JsonElement?, val path: String = "$") {

    val isUndefined get() = jsonElement == null
    val isArray get() = jsonElement?.castOrNull<JsonArray>()?.let { true } ?: false
    val isObject get() = jsonElement?.castOrNull<JsonObject>()?.let { true } ?: false
    val isNumber get() = jsonElement?.castOrNull<JsonPrimitive>()?.isNumber ?: false
    val isString get() = jsonElement?.castOrNull<JsonPrimitive>()?.isString ?: false
    val isBoolean get() = jsonElement?.castOrNull<JsonPrimitive>()?.isBoolean ?: false
    val isNull get() = jsonElement?.castOrNull<JsonNull>()?.let { true } ?: false

    val type
        get() = when {
            isUndefined -> "Undefined"
            isArray -> "Array"
            isObject -> "Object"
            isNumber -> "Number"
            isString -> "String"
            isBoolean -> "Boolean"
            isNull -> "Null"
            else -> throw IllegalStateException()
        }


    val orNull get() = if (isUndefined || isNull) null else this


    private fun typeMismatch(expectedType: String): Nothing = throw JsonTypeMismatchException("Expected $expectedType, but is $type: $path")

    fun asUndefined() = if (isUndefined) null else typeMismatch("Undefined")
    fun asJsonArray() = jsonElement as? JsonArray ?: typeMismatch("JsonArray")
    fun asJsonObject() = jsonElement as? JsonObject ?: typeMismatch("JsonObject")
    fun asJsonPrimitive() = jsonElement as? JsonPrimitive ?: typeMismatch("JsonPrimitive")
    fun asJsonNull() = jsonElement as? JsonNull ?: typeMismatch("JsonNull")

    fun asBigDecimal() = if (isNumber) asJsonPrimitive().asBigDecimal!! else typeMismatch("Number")
    fun asString() = if (isString) asJsonPrimitive().asString!! else typeMismatch("String")
    fun asBoolean() = if (isBoolean) asJsonPrimitive().asBoolean else typeMismatch("Boolean")
    fun asNull() = if (isNull) null else typeMismatch("Null")

    fun asList(): List<JsonWrapper2> = if (isArray) asJsonArray().toList().mapIndexed { index, item -> JsonWrapper2(item, "$path[$index]") } else typeMismatch("Array")
    fun asMap(): Map<String, JsonWrapper2> = if (isObject) asJsonObject().entrySet().associate { (key, value) -> key to JsonWrapper2(value, "$path.$key") } else typeMismatch("Object")
    fun asInt() = if (isNumber) asJsonPrimitive().asInt else typeMismatch("Number")
    fun asLong() = if (isNumber) asJsonPrimitive().asLong else typeMismatch("Number")
    fun asDouble() = if (isNumber) asJsonPrimitive().asDouble else typeMismatch("Number")


    operator fun get(index: Int) = asJsonArray().let { JsonWrapper2(if (index >= 0 && index < it.size()) it[index] else null, "$path[$index]") }
    operator fun get(key: String) = JsonWrapper(asJsonObject().get(key), "$path.$key")


    private fun getString(jsonElement: JsonElement?): String = when (jsonElement) {
        null -> "undefined"
        is JsonArray -> "[${jsonElement.joinToString(",") { getString(it) }}]"
        is JsonObject -> "{${jsonElement.entrySet().joinToString(",") { """${it.key}:${getString(it.value)}""" }}}"
        is JsonPrimitive -> when {
            jsonElement.isNumber -> jsonElement.asNumber.toString()
            jsonElement.isString -> jsonElement.asString
            jsonElement.isBoolean -> jsonElement.asBoolean.toString()
            else -> throw IllegalStateException()
        }
        is JsonNull -> "null"
        else -> throw IllegalStateException()
    }

    override fun toString() = getString(jsonElement)

}

fun JsonElement?.toJsonWrapper2() = JsonWrapper2(this)
