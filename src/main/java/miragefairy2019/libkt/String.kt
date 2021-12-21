package miragefairy2019.libkt

infix fun Double.with(format: String) = String.format(format, this)
infix fun Float.with(format: String) = String.format(format, this)
infix fun Long.with(format: String) = String.format(format, this)
infix fun Int.with(format: String) = String.format(format, this)
infix fun Short.with(format: String) = String.format(format, this)
infix fun Byte.with(format: String) = String.format(format, this)
infix fun Boolean.with(format: String) = String.format(format, this)
infix fun String.with(format: String) = String.format(format, this)
