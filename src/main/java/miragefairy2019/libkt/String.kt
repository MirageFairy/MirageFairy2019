package miragefairy2019.libkt

infix fun Double.with(format: String) = String.format(format, this)
infix fun Float.with(format: String) = String.format(format, this)
infix fun Long.with(format: String) = String.format(format, this)
infix fun Int.with(format: String) = String.format(format, this)
infix fun Short.with(format: String) = String.format(format, this)
infix fun Byte.with(format: String) = String.format(format, this)
infix fun Boolean.with(format: String) = String.format(format, this)
infix fun String.with(format: String) = String.format(format, this)

val String.upperCase get() = toUpperCase()
val String.lowerCase get() = toLowerCase()

/** 先頭の文字のみを大文字にします。 */
val String.upperCaseHead get() = if (isEmpty()) this else take(1).upperCase + drop(1)

/** 先頭の文字のみを小文字にします。 */
val String.lowerCaseHead get() = if (isEmpty()) this else take(1).lowerCase + drop(1)

/** @receiver スネークケースの文字列 */
val String.upperCamelCase get() = split('_').joinToString("") { it.upperCaseHead }

/** @receiver スネークケースの文字列 */
val String.lowerCamelCase get() = upperCamelCase.lowerCaseHead
