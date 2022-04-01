package miragefairy2019.libkt

import mirrg.kotlin.orNull
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import java.io.File

// スタイル
// TODO bold()は引数を省略不可に
// TODO 削除 or private
fun <T : ITextComponent> T.color(color: TextFormatting) = apply { this.style.color = color } // TODO 純粋関数に変更
fun <T : ITextComponent> T.obfuscated(value: Boolean = true) = apply { this.style.obfuscated = value }
fun <T : ITextComponent> T.bold(value: Boolean = true) = apply { this.style.bold = value }
fun <T : ITextComponent> T.strikethrough(value: Boolean = true) = apply { this.style.strikethrough = value }
fun <T : ITextComponent> T.underline(value: Boolean = true) = apply { this.style.underlined = value }
fun <T : ITextComponent> T.italic(value: Boolean = true) = apply { this.style.italic = value }
fun <T : ITextComponent> T.onClick(clickEvent: ClickEvent) = apply { this.style.clickEvent = clickEvent }
fun <T : ITextComponent> T.onHover(hoverEvent: HoverEvent) = apply { this.style.hoverEvent = hoverEvent }
val <T : ITextComponent> T.black get() = color(TextFormatting.BLACK)
val <T : ITextComponent> T.darkBlue get() = color(TextFormatting.DARK_BLUE)
val <T : ITextComponent> T.darkGreen get() = color(TextFormatting.DARK_GREEN)
val <T : ITextComponent> T.darkAqua get() = color(TextFormatting.DARK_AQUA)
val <T : ITextComponent> T.darkRed get() = color(TextFormatting.DARK_RED)
val <T : ITextComponent> T.darkPurple get() = color(TextFormatting.DARK_PURPLE)
val <T : ITextComponent> T.gold get() = color(TextFormatting.GOLD)
val <T : ITextComponent> T.gray get() = color(TextFormatting.GRAY)
val <T : ITextComponent> T.darkGray get() = color(TextFormatting.DARK_GRAY)
val <T : ITextComponent> T.blue get() = color(TextFormatting.BLUE)
val <T : ITextComponent> T.green get() = color(TextFormatting.GREEN)
val <T : ITextComponent> T.aqua get() = color(TextFormatting.AQUA)
val <T : ITextComponent> T.red get() = color(TextFormatting.RED)
val <T : ITextComponent> T.lightPurple get() = color(TextFormatting.LIGHT_PURPLE)
val <T : ITextComponent> T.yellow get() = color(TextFormatting.YELLOW)
val <T : ITextComponent> T.white get() = color(TextFormatting.WHITE)
val <T : ITextComponent> T.obfuscated get() = obfuscated()
val <T : ITextComponent> T.bold get() = bold()
val <T : ITextComponent> T.strikethrough get() = strikethrough()
val <T : ITextComponent> T.underline get() = underline()
val <T : ITextComponent> T.italic get() = italic()

fun <T : ITextComponent> List<T>.color(color: TextFormatting) = listOf(textComponent(this).color(color))
fun <T : ITextComponent> List<T>.obfuscated(value: Boolean = true) = listOf(textComponent(this).obfuscated(value))
fun <T : ITextComponent> List<T>.bold(value: Boolean = true) = listOf(textComponent(this).bold(value))
fun <T : ITextComponent> List<T>.strikethrough(value: Boolean = true) = listOf(textComponent(this).strikethrough(value))
fun <T : ITextComponent> List<T>.underline(value: Boolean = true) = listOf(textComponent(this).underline(value))
fun <T : ITextComponent> List<T>.italic(value: Boolean = true) = listOf(textComponent(this).italic(value))
fun <T : ITextComponent> List<T>.onClick(clickEvent: ClickEvent) = listOf(textComponent(this).onClick(clickEvent))
fun <T : ITextComponent> List<T>.onHover(hoverEvent: HoverEvent) = listOf(textComponent(this).onHover(hoverEvent))
val <T : ITextComponent> List<T>.black get() = color(TextFormatting.BLACK)
val <T : ITextComponent> List<T>.darkBlue get() = color(TextFormatting.DARK_BLUE)
val <T : ITextComponent> List<T>.darkGreen get() = color(TextFormatting.DARK_GREEN)
val <T : ITextComponent> List<T>.darkAqua get() = color(TextFormatting.DARK_AQUA)
val <T : ITextComponent> List<T>.darkRed get() = color(TextFormatting.DARK_RED)
val <T : ITextComponent> List<T>.darkPurple get() = color(TextFormatting.DARK_PURPLE)
val <T : ITextComponent> List<T>.gold get() = color(TextFormatting.GOLD)
val <T : ITextComponent> List<T>.gray get() = color(TextFormatting.GRAY)
val <T : ITextComponent> List<T>.darkGray get() = color(TextFormatting.DARK_GRAY)
val <T : ITextComponent> List<T>.blue get() = color(TextFormatting.BLUE)
val <T : ITextComponent> List<T>.green get() = color(TextFormatting.GREEN)
val <T : ITextComponent> List<T>.aqua get() = color(TextFormatting.AQUA)
val <T : ITextComponent> List<T>.red get() = color(TextFormatting.RED)
val <T : ITextComponent> List<T>.lightPurple get() = color(TextFormatting.LIGHT_PURPLE)
val <T : ITextComponent> List<T>.yellow get() = color(TextFormatting.YELLOW)
val <T : ITextComponent> List<T>.white get() = color(TextFormatting.WHITE)
val <T : ITextComponent> List<T>.obfuscated get() = obfuscated()
val <T : ITextComponent> List<T>.bold get() = bold()
val <T : ITextComponent> List<T>.strikethrough get() = strikethrough()
val <T : ITextComponent> List<T>.underline get() = underline()
val <T : ITextComponent> List<T>.italic get() = italic()


// 新DSL
class TextComponentScope {
    val empty get() = listOf<ITextComponent>()
    fun join(vararg textComponents: List<ITextComponent>) = listOf(*textComponents).flatten()
    operator fun ITextComponent.not() = listOf(this)
    operator fun String.not() = !TextComponentString(this)
    operator fun File.not() = (!name).onHover(HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent { !absoluteFile.canonicalPath })).onClick(ClickEvent(ClickEvent.Action.OPEN_FILE, absoluteFile.canonicalPath)).underline
    fun format(format: String, vararg args: Any?) = !String.format(format, *args)
    fun translate(translationKey: String, vararg args: Any?) = !TextComponentTranslation(translationKey, *args)

    fun List<List<ITextComponent>>.concat(separator: List<ITextComponent>) = orNull?.reduce { a, b -> a + separator + b } ?: empty
}

fun textComponent(textComponents: List<ITextComponent>): ITextComponent = textComponents.fold(TextComponentString("") as ITextComponent) { a, b -> a.appendSibling(b) }
inline fun textComponent(block: TextComponentScope.() -> List<ITextComponent>): ITextComponent = textComponent(TextComponentScope().block())
fun textComponent() = textComponent { empty }
fun formattedText(textComponents: List<ITextComponent>): String = textComponent(textComponents).formattedText
inline fun formattedText(block: TextComponentScope.() -> List<ITextComponent>): String = textComponent(block).formattedText
fun formattedText(): String = textComponent().formattedText
