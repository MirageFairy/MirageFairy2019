package miragefairy2019.libkt

import mirrg.kotlin.hydrogen.formatAs
import java.time.Duration

val Duration.displayText
    get() = textComponent {
        fun unit(unitName: String) = translate("miragefairy2019.gui.duration.$unitName")
        val millis = toMillis()
        when {
            millis >= 1000 * 60 * 60 * 24 -> ((millis / (1000 * 60 * 60 * 24).toDouble()) formatAs "%.2f")() + unit("days")
            millis >= 1000 * 60 * 60 -> ((millis / (1000 * 60 * 60).toDouble()) formatAs "%.2f")() + unit("hours")
            millis >= 1000 * 60 -> ((millis / (1000 * 60).toDouble()) formatAs "%.2f")() + unit("minutes")
            millis >= 1000 -> ((millis / (1000).toDouble()) formatAs "%.2f")() + unit("seconds")
            else -> (millis.toDouble() formatAs "%.2f")() + unit("milliSeconds")
        }
    }
