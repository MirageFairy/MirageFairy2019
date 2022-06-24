package mirrg.kotlin

import mirrg.kotlin.hydrogen.join

private val toSnakeCaseRegex = """(?<=.)(?=[A-Z])""".toRegex()

/** @receiver キャメルケースの文字列 */
fun String.toSnakeCase(delimiter: String = "_") = split(toSnakeCaseRegex).map { it.toLowerCase() }.join(delimiter)
