@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package mirrg.kotlin.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import mirrg.kotlin.hydrogen.castOrNull
import mirrg.kotlin.hydrogen.join
import mirrg.kotlin.isNotSameAs
import java.math.BigDecimal

// 本体

class JsonDecompositionException(message: String) : IllegalStateException(message)

/**
 * @param jsonElement nullの場合、存在しない要素を表します。
 * Json内でnullが明示された場合はJsonNullの状態を持ちます。
 */
class JsonWrapper(
    val jsonElement: JsonElement?,
    val path: String
) {
    override fun toString(): String = jsonElement.toString()

    // 型チェック
    // 厳密に一致する場合のみtrue、かつjsonElementが対応する子クラスであることを保証
    val isUndefined get() = jsonElement == null
    val isNumber get() = jsonElement?.castOrNull<JsonPrimitive>()?.isNumber ?: false
    val isString get() = jsonElement?.castOrNull<JsonPrimitive>()?.isString ?: false
    val isBoolean get() = jsonElement?.castOrNull<JsonPrimitive>()?.isBoolean ?: false
    val isNull get() = jsonElement?.castOrNull<JsonNull>()?.let { true } ?: false
    val isArray get() = jsonElement?.castOrNull<JsonArray>()?.let { true } ?: false
    val isObject get() = jsonElement?.castOrNull<JsonObject>()?.let { true } ?: false

    // キャスト
    // キャストできない場合は特殊化例外
    val primitiveElement get() = jsonElement as? JsonPrimitive ?: e("JsonPrimitive")
    val nullElement get() = jsonElement as? JsonNull ?: e("JsonNull")
    val arrayElement get() = jsonElement as? JsonArray ?: e("JsonArray")
    val objectElement get() = jsonElement as? JsonObject ?: e("JsonObject")

    // 取得プロパティ
    // キャストできない場合に発生する例外は必ず特殊化クラス
    // 省略時も例外を出す
    val asUndefined get() = if (isUndefined) null else e("Undefined")
    val asInt get() = if (isNumber) primitiveElement.asInt else e("Number")
    val asLong get() = if (isNumber) primitiveElement.asLong else e("Number")
    val asDouble get() = if (isNumber) primitiveElement.asDouble else e("Number")
    val asBigDecimal get() = if (isNumber) primitiveElement.asBigDecimal!! else e("Number")
    val asString get() = if (isString) primitiveElement.asString!! else e("String")
    val asBoolean get() = if (isBoolean) primitiveElement.asBoolean else e("Boolean")
    val asNull get() = if (isNull) null else e("Null")
    val asList: List<JsonWrapper> get() = if (isArray) arrayElement.toList().mapIndexed { index, item -> JsonWrapper(item, "$path[$index]") } else e("Array")
    val asMap: Map<String, JsonWrapper> get() = if (isObject) objectElement.entrySet().associate { (key, value) -> key to JsonWrapper(value, "$path.$key") } else e("Object")

    // 「undefinedもしくはnullのときに」nullを返す
    val orNull get() = if (isUndefined || isNull) null else this

    // オブジェクトと配列は子要素をJsonWrapperで覆って返す
    operator fun get(index: Int) = JsonWrapper(if (index >= 0 && index < arrayElement.size()) arrayElement.get(index) else null, "$path[$index]")
    operator fun get(key: String) = JsonWrapper(objectElement.get(key), "$path.$key")
}

private val JsonWrapper.type
    get() = when {
        isUndefined -> "Undefined"
        isNumber -> "Number"
        isString -> "String"
        isBoolean -> "Boolean"
        isNull -> "Null"
        isArray -> "Array"
        isObject -> "Object"
        else -> throw IllegalStateException()
    }

private fun JsonWrapper.e(expectedType: String): Nothing = throw JsonDecompositionException("Expected $expectedType, but is a $type: $path")

val JsonWrapper.toString: String
    get() = when {
        isUndefined -> "undefined"
        isNumber -> asBigDecimal.toString()
        isString -> asString
        isBoolean -> asBoolean.toString()
        isNull -> "null"
        isArray -> "[" + asList.join { it.toString } + "]"
        isObject -> "{" + asMap.entries.join { it.key + "=" + it.value.toString } + "}"
        else -> throw IllegalStateException()
    }

val JsonWrapper.toInt: Int
    get() = when {
        isUndefined -> 0
        isNumber -> asInt
        isString -> asString.toInt()
        isBoolean -> if (asBoolean) 1 else 0
        isNull -> 0
        isArray -> e("Number")
        isObject -> e("Number")
        else -> throw IllegalStateException()
    }

val JsonWrapper.toDouble: Double
    get() = when {
        isUndefined -> 0.0
        isNumber -> asDouble
        isString -> asString.toDouble()
        isBoolean -> if (asBoolean) 1.0 else 0.0
        isNull -> 0.0
        isArray -> e("Number")
        isObject -> e("Number")
        else -> throw IllegalStateException()
    }

val JsonWrapper.toBigDecimal: BigDecimal
    get() = when {
        isUndefined -> BigDecimal.ZERO
        isNumber -> asBigDecimal
        isString -> asString.toBigDecimal()
        isBoolean -> if (asBoolean) BigDecimal.ONE else BigDecimal.ZERO
        isNull -> BigDecimal.ZERO
        isArray -> e("Number")
        isObject -> e("Number")
        else -> throw IllegalStateException()
    }

val JsonWrapper.toBoolean: Boolean
    get() = when {
        isUndefined -> false
        isNumber -> asBigDecimal isNotSameAs BigDecimal.ZERO
        isString -> asString.isNotEmpty()
        isBoolean -> asBoolean
        isNull -> false
        isArray -> true
        isObject -> true
        else -> throw IllegalStateException()
    }

// 文字列のJsonWrapper化
val String.jsonWrapper get() = (if (this.isBlank()) null else GsonBuilder().create().fromJson(this, JsonElement::class.java)).jsonWrapper
val JsonElement?.jsonWrapper get() = JsonWrapper(this, "_")
