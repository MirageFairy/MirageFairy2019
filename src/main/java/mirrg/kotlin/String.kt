package mirrg.kotlin

/** 先頭の文字のみを大文字にします。 */
fun String.toUpperCaseHead() = if (isEmpty()) this else take(1).toUpperCase() + drop(1)

/** 先頭の文字のみを小文字にします。 */
fun String.toLowerCaseHead() = if (isEmpty()) this else take(1).toLowerCase() + drop(1)

/** @receiver スネークケースの文字列 */
fun String.toUpperCamelCase(beforeDelimiter: String = "_", afterDelimiter: String = "") = split(beforeDelimiter).join(afterDelimiter) { it.toUpperCaseHead() }

/** @receiver スネークケースの文字列 */
fun String.toLowerCamelCase(beforeDelimiter: String = "_", afterDelimiter: String = "") = toUpperCamelCase(beforeDelimiter, afterDelimiter).toLowerCaseHead()

private val toSnakeCaseRegex = """(?<=.)(?=[A-Z])""".toRegex()

/** @receiver キャメルケースの文字列 */
fun String.toSnakeCase(delimiter: String = "_") = split(toSnakeCaseRegex).map { it.toLowerCase() }.join(delimiter)

val String.notEmptyOrNull get() = ifEmpty { null }
val String.notBlankOrNull get() = ifBlank { null }
