package miragefairy2019.libkt

import net.minecraft.util.text.ITextComponent
import java.time.Duration

val Duration.displayText: ITextComponent
    get() {
        val prefix = "miragefairy2019.gui.duration"
        val millis = toMillis()
        if (millis >= 1_000 * 60 * 60 * 24) return textComponent { !((millis / (1_000 * 60 * 60 * 24).toDouble()) with "%.2f") + translate("$prefix.days") }
        if (millis >= 1_000 * 60 * 60) return textComponent { !((millis / (1_000 * 60 * 60).toDouble()) with "%.2f") + translate("$prefix.hours") }
        if (millis >= 1_000 * 60) return textComponent { !((millis / (1_000 * 60).toDouble()) with "%.2f") + translate("$prefix.minutes") }
        if (millis >= 1_000) return textComponent { !((millis / (1_000).toDouble()) with "%.2f") + translate("$prefix.seconds") }
        return textComponent { !(millis.toDouble() with "%.2f") + translate("$prefix.milliSeconds") }
    }
