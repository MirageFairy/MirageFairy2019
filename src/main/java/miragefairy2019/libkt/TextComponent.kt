package miragefairy2019.libkt

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import java.io.File

// TODO List<ITextComponent> -> 独自コンテナー

// 汎用的なスタイル
fun List<ITextComponent>.withColor(value: TextFormatting) = listOf(toTextComponent().apply { style.color = value })
fun List<ITextComponent>.withObfuscated(value: Boolean) = listOf(toTextComponent().apply { style.obfuscated = value })
fun List<ITextComponent>.withBold(value: Boolean) = listOf(toTextComponent().apply { style.bold = value })
fun List<ITextComponent>.withStrikethrough(value: Boolean) = listOf(toTextComponent().apply { style.strikethrough = value })
fun List<ITextComponent>.withUnderline(value: Boolean) = listOf(toTextComponent().apply { style.underlined = value })
fun List<ITextComponent>.withItalic(value: Boolean) = listOf(toTextComponent().apply { style.italic = value })
fun List<ITextComponent>.withClickEvent(value: ClickEvent) = listOf(toTextComponent().apply { style.clickEvent = value })
fun List<ITextComponent>.withHoverEvent(value: HoverEvent) = listOf(toTextComponent().apply { style.hoverEvent = value })

// 便利なスタイル
val List<ITextComponent>.black get() = withColor(TextFormatting.BLACK)
val List<ITextComponent>.darkBlue get() = withColor(TextFormatting.DARK_BLUE)
val List<ITextComponent>.darkGreen get() = withColor(TextFormatting.DARK_GREEN)
val List<ITextComponent>.darkAqua get() = withColor(TextFormatting.DARK_AQUA)
val List<ITextComponent>.darkRed get() = withColor(TextFormatting.DARK_RED)
val List<ITextComponent>.darkPurple get() = withColor(TextFormatting.DARK_PURPLE)
val List<ITextComponent>.gold get() = withColor(TextFormatting.GOLD)
val List<ITextComponent>.gray get() = withColor(TextFormatting.GRAY)
val List<ITextComponent>.darkGray get() = withColor(TextFormatting.DARK_GRAY)
val List<ITextComponent>.blue get() = withColor(TextFormatting.BLUE)
val List<ITextComponent>.green get() = withColor(TextFormatting.GREEN)
val List<ITextComponent>.aqua get() = withColor(TextFormatting.AQUA)
val List<ITextComponent>.red get() = withColor(TextFormatting.RED)
val List<ITextComponent>.lightPurple get() = withColor(TextFormatting.LIGHT_PURPLE)
val List<ITextComponent>.yellow get() = withColor(TextFormatting.YELLOW)
val List<ITextComponent>.white get() = withColor(TextFormatting.WHITE)
val List<ITextComponent>.obfuscated get() = withObfuscated(true)
val List<ITextComponent>.bold get() = withBold(true)
val List<ITextComponent>.strikethrough get() = withStrikethrough(true)
val List<ITextComponent>.underline get() = withUnderline(true)
val List<ITextComponent>.italic get() = withItalic(true)

// 新DSL
object TextComponentScope {
    val empty get() = listOf<ITextComponent>()
    operator fun ITextComponent.invoke() = listOf(this)
    operator fun String.invoke() = TextComponentString(this)()
    operator fun File.invoke() = name()
        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent { absoluteFile.canonicalPath() }))
        .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_FILE, absoluteFile.canonicalPath)).underline

    fun format(format: String, vararg args: Any?) = String.format(format, *args)()
    fun translate(translationKey: String, vararg args: Any?) = TextComponentTranslation(translationKey, *args)()
}

// List<ITextComponent>のITextComponent化
fun List<ITextComponent>.toTextComponent(): ITextComponent = this.fold(TextComponentString("") as ITextComponent) { a, b -> a.appendSibling(b) }

// DSLエントリポイント
inline fun textComponent(block: TextComponentScope.() -> List<ITextComponent>) = TextComponentScope.block().toTextComponent()
inline fun formattedText(block: TextComponentScope.() -> List<ITextComponent>): String = textComponent(block).formattedText
