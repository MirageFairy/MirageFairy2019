package miragefairy2019.libkt

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.util.text.TextFormatting

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
fun <T : ITextComponent> T.color(color: TextFormatting) = run { this.style.color = color; this }
fun <T : ITextComponent> T.bold(value: Boolean = true) = run { this.style.bold = value; this }
