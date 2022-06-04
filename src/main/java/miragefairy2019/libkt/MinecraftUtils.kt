package miragefairy2019.libkt

import net.minecraft.util.text.translation.I18n

fun canTranslate(key: String): Boolean = I18n.canTranslate(key)
fun translateToLocal(key: String): String = I18n.translateToLocal(key).trim()
fun translateToLocalFormatted(key: String, vararg format: Any?): String = I18n.translateToLocalFormatted(key, *format).trim()
