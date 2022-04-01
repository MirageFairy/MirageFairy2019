package miragefairy2019.libkt

import mirrg.kotlin.orNull
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.event.ClickEvent
import net.minecraft.util.text.event.HoverEvent
import java.io.File

// TODO List<ITextComponent> -> 独自コンテナー

// 汎用的なスタイル
fun List<ITextComponent>.setColor(value: TextFormatting) = listOf(toTextComponent().apply { style.color = value })
fun List<ITextComponent>.setObfuscated(value: Boolean) = listOf(toTextComponent().apply { style.obfuscated = value })
fun List<ITextComponent>.setBold(value: Boolean) = listOf(toTextComponent().apply { style.bold = value })
fun List<ITextComponent>.setStrikethrough(value: Boolean) = listOf(toTextComponent().apply { style.strikethrough = value })
fun List<ITextComponent>.setUnderline(value: Boolean) = listOf(toTextComponent().apply { style.underlined = value })
fun List<ITextComponent>.setItalic(value: Boolean) = listOf(toTextComponent().apply { style.italic = value })
fun List<ITextComponent>.setOnClick(value: ClickEvent) = listOf(toTextComponent().apply { style.clickEvent = value })
fun List<ITextComponent>.setOnHover(value: HoverEvent) = listOf(toTextComponent().apply { style.hoverEvent = value })

// 便利なスタイル
val List<ITextComponent>.black get() = setColor(TextFormatting.BLACK)
val List<ITextComponent>.darkBlue get() = setColor(TextFormatting.DARK_BLUE)
val List<ITextComponent>.darkGreen get() = setColor(TextFormatting.DARK_GREEN)
val List<ITextComponent>.darkAqua get() = setColor(TextFormatting.DARK_AQUA)
val List<ITextComponent>.darkRed get() = setColor(TextFormatting.DARK_RED)
val List<ITextComponent>.darkPurple get() = setColor(TextFormatting.DARK_PURPLE)
val List<ITextComponent>.gold get() = setColor(TextFormatting.GOLD)
val List<ITextComponent>.gray get() = setColor(TextFormatting.GRAY)
val List<ITextComponent>.darkGray get() = setColor(TextFormatting.DARK_GRAY)
val List<ITextComponent>.blue get() = setColor(TextFormatting.BLUE)
val List<ITextComponent>.green get() = setColor(TextFormatting.GREEN)
val List<ITextComponent>.aqua get() = setColor(TextFormatting.AQUA)
val List<ITextComponent>.red get() = setColor(TextFormatting.RED)
val List<ITextComponent>.lightPurple get() = setColor(TextFormatting.LIGHT_PURPLE)
val List<ITextComponent>.yellow get() = setColor(TextFormatting.YELLOW)
val List<ITextComponent>.white get() = setColor(TextFormatting.WHITE)
val List<ITextComponent>.obfuscated get() = setObfuscated(true)
val List<ITextComponent>.bold get() = setBold(true)
val List<ITextComponent>.strikethrough get() = setStrikethrough(true)
val List<ITextComponent>.underline get() = setUnderline(true)
val List<ITextComponent>.italic get() = setItalic(true)

// 新DSL
class TextComponentScope { // TODO -> object
    val empty get() = listOf<ITextComponent>()
    operator fun ITextComponent.not() = listOf(this) // TODO !_ -> _()
    operator fun String.not() = !TextComponentString(this) // TODO !_ -> _()
    operator fun File.not() = (!name) // TODO !_ -> _()
        .setOnHover(HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent { !absoluteFile.canonicalPath }))
        .setOnClick(ClickEvent(ClickEvent.Action.OPEN_FILE, absoluteFile.canonicalPath)).underline

    fun format(format: String, vararg args: Any?) = !String.format(format, *args)
    fun translate(translationKey: String, vararg args: Any?) = !TextComponentTranslation(translationKey, *args)

    fun List<List<ITextComponent>>.concat(separator: List<ITextComponent>) = orNull?.reduce { a, b -> a + separator + b } ?: empty // TODO remove
    fun join(vararg textComponents: List<ITextComponent>) = listOf(*textComponents).flatten() // TODO remove: -> mirrg.concat
}

// List<ITextComponent>のITextComponent化
fun List<ITextComponent>.toTextComponent(): ITextComponent = this.fold(TextComponentString("") as ITextComponent) { a, b -> a.appendSibling(b) }

// DSLエントリポイント
inline fun textComponent(block: TextComponentScope.() -> List<ITextComponent>) = TextComponentScope().block().toTextComponent()
inline fun formattedText(block: TextComponentScope.() -> List<ITextComponent>): String = textComponent(block).formattedText
