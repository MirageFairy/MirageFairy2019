package miragefairy2019.libkt

import mirrg.kotlin.formatAs
import net.minecraft.util.text.ITextComponent
import java.time.Duration

val Duration.displayText: ITextComponent
    get() {
        return textComponent {
            val prefix = "miragefairy2019.gui.duration"
            val millis = toMillis()
            when {
                millis >= 1000 * 60 * 60 * 24 -> !((millis / (1000 * 60 * 60 * 24).toDouble()) formatAs "%.2f") + translate("$prefix.days")
                millis >= 1000 * 60 * 60 -> !((millis / (1000 * 60 * 60).toDouble()) formatAs "%.2f") + translate("$prefix.hours")
                millis >= 1000 * 60 -> !((millis / (1000 * 60).toDouble()) formatAs "%.2f") + translate("$prefix.minutes")
                millis >= 1000 -> !((millis / (1000).toDouble()) formatAs "%.2f") + translate("$prefix.seconds")
                else -> !(millis.toDouble() formatAs "%.2f") + translate("$prefix.milliSeconds")
            }
        }
    }
