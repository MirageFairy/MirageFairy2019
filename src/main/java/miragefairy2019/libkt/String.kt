package miragefairy2019.libkt

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
