package miragefairy2019.libkt

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import java.io.File


class TextComponentWrapper(val textComponents: List<ITextComponent>) {
    constructor(textComponent: ITextComponent) : this(listOf(textComponent))

    companion object {
        val EMPTY = TextComponentWrapper(listOf())
    }
}

fun TextComponentWrapper.toTextComponent(): ITextComponent = textComponents.fold(TextComponentString("") as ITextComponent) { a, b -> a.appendSibling(b) }

val TextComponentWrapper.isEmpty get() = textComponents.all { it.unformattedText.isEmpty() }
val TextComponentWrapper.isNotEmpty get() = !isEmpty
operator fun TextComponentWrapper.plus(other: TextComponentWrapper) = TextComponentWrapper(textComponents + other.textComponents)
fun concat(vararg textComponentWrappers: TextComponentWrapper) = textComponentWrappers.asIterable().flatten()
fun concatNotNull(vararg textComponentWrappers: TextComponentWrapper?) = textComponentWrappers.filterNotNull().flatten()
fun Iterable<TextComponentWrapper>.flatten() = TextComponentWrapper(this.map { it.textComponents }.flatten())

// 汎用的なスタイル
fun TextComponentWrapper.withColor(value: TextFormatting) = TextComponentWrapper(toTextComponent().apply { style.color = value })
fun TextComponentWrapper.withObfuscated(value: Boolean) = TextComponentWrapper(toTextComponent().apply { style.obfuscated = value })
fun TextComponentWrapper.withBold(value: Boolean) = TextComponentWrapper(toTextComponent().apply { style.bold = value })
fun TextComponentWrapper.withStrikethrough(value: Boolean) = TextComponentWrapper(toTextComponent().apply { style.strikethrough = value })
fun TextComponentWrapper.withUnderline(value: Boolean) = TextComponentWrapper(toTextComponent().apply { style.underlined = value })
fun TextComponentWrapper.withItalic(value: Boolean) = TextComponentWrapper(toTextComponent().apply { style.italic = value })
fun TextComponentWrapper.withClickEvent(value: ClickEvent) = TextComponentWrapper(toTextComponent().apply { style.clickEvent = value })
fun TextComponentWrapper.withHoverEvent(value: HoverEvent) = TextComponentWrapper(toTextComponent().apply { style.hoverEvent = value })

// 便利なスタイル
val TextComponentWrapper.black get() = withColor(TextFormatting.BLACK)
val TextComponentWrapper.darkBlue get() = withColor(TextFormatting.DARK_BLUE)
val TextComponentWrapper.darkGreen get() = withColor(TextFormatting.DARK_GREEN)
val TextComponentWrapper.darkAqua get() = withColor(TextFormatting.DARK_AQUA)
val TextComponentWrapper.darkRed get() = withColor(TextFormatting.DARK_RED)
val TextComponentWrapper.darkPurple get() = withColor(TextFormatting.DARK_PURPLE)
val TextComponentWrapper.gold get() = withColor(TextFormatting.GOLD)
val TextComponentWrapper.gray get() = withColor(TextFormatting.GRAY)
val TextComponentWrapper.darkGray get() = withColor(TextFormatting.DARK_GRAY)
val TextComponentWrapper.blue get() = withColor(TextFormatting.BLUE)
val TextComponentWrapper.green get() = withColor(TextFormatting.GREEN)
val TextComponentWrapper.aqua get() = withColor(TextFormatting.AQUA)
val TextComponentWrapper.red get() = withColor(TextFormatting.RED)
val TextComponentWrapper.lightPurple get() = withColor(TextFormatting.LIGHT_PURPLE)
val TextComponentWrapper.yellow get() = withColor(TextFormatting.YELLOW)
val TextComponentWrapper.white get() = withColor(TextFormatting.WHITE)
val TextComponentWrapper.obfuscated get() = withObfuscated(true)
val TextComponentWrapper.bold get() = withBold(true)
val TextComponentWrapper.strikethrough get() = withStrikethrough(true)
val TextComponentWrapper.underline get() = withUnderline(true)
val TextComponentWrapper.italic get() = withItalic(true)


object TextComponentScope {
    val empty get() = TextComponentWrapper.EMPTY
    operator fun ITextComponent.invoke() = TextComponentWrapper(this)
    operator fun String.invoke() = TextComponentString(this)()
    operator fun File.invoke() = name()
        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent { absoluteFile.canonicalPath() }))
        .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_FILE, absoluteFile.canonicalPath)).underline

    fun format(format: String, vararg args: Any?) = String.format(format, *args)()
    fun translate(translationKey: String, vararg args: Any?) = TextComponentTranslation(translationKey, *args)()
}

// DSLエントリポイント
inline fun textComponent(block: TextComponentScope.() -> TextComponentWrapper) = TextComponentScope.block().toTextComponent()
inline fun formattedText(block: TextComponentScope.() -> TextComponentWrapper): String = textComponent(block).formattedText
