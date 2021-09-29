package miragefairy2019.libkt

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting


// 旧DSL
class TextComponentBuilder {
    private val textComponents = mutableListOf<ITextComponent>()
    fun build() = textComponents.fold(TextComponentString("") as ITextComponent) { a, b -> a.appendSibling(b) }!!
    fun <T : ITextComponent> text(textComponent: T) = textComponent.also { textComponents.add(it) }
    fun text(text: String) = text(TextComponentString(text))
    fun format(format: String, vararg args: Any?) = text(String.format(format, *args))
    fun translate(translationKey: String, vararg args: Any?) = text(TextComponentTranslation(translationKey, *args))
    fun text(block: TextComponentBuilder.() -> Unit) = text(TextComponentBuilder().also { it.block() }.build())
}

fun buildText(block: TextComponentBuilder.() -> Unit): ITextComponent = TextComponentBuilder().also { it.block() }.build()


// スタイル
fun <T : ITextComponent> T.color(color: TextFormatting) = apply { this.style.color = color }
fun <T : ITextComponent> T.obfuscated(value: Boolean = true) = apply { this.style.obfuscated = value }
fun <T : ITextComponent> T.bold(value: Boolean = true) = apply { this.style.bold = value }
fun <T : ITextComponent> T.strikethrough(value: Boolean = true) = apply { this.style.strikethrough = value }
fun <T : ITextComponent> T.underline(value: Boolean = true) = apply { this.style.underlined = value }
fun <T : ITextComponent> T.italic(value: Boolean = true) = apply { this.style.italic = value }
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
    operator fun ITextComponent.not() = listOf(this)
    operator fun String.not() = !TextComponentString(this)
    fun format(format: String, vararg args: Any?) = !String.format(format, *args)
    fun translate(translationKey: String, vararg args: Any?) = !TextComponentTranslation(translationKey, *args)
}

fun textComponent(textComponents: List<ITextComponent>): ITextComponent = textComponents.fold(TextComponentString("") as ITextComponent) { a, b -> a.appendSibling(b) }
fun textComponent(block: TextComponentScope.() -> List<ITextComponent>): ITextComponent = textComponent(TextComponentScope().block())
fun textComponent() = textComponent { empty }
fun formattedText(textComponents: List<ITextComponent>) = textComponent(textComponents).formattedText
fun formattedText(block: TextComponentScope.() -> List<ITextComponent>) = textComponent(block).formattedText
fun formattedText() = textComponent().formattedText
