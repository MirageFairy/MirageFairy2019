package miragefairy2019.modkt.impl.magicstatus

import miragefairy2019.libkt.bold
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod3.magic.api.IMagicStatus
import miragefairy2019.mod3.magic.api.IMagicStatusFormatter
import miragefairy2019.mod3.magic.api.IMagicStatusFunction
import miragefairy2019.mod3.magic.api.IMagicStatusFunctionArguments
import miragefairy2019.modkt.api.erg.IErgSet
import miragefairy2019.modkt.api.erg.IErgType
import miragefairy2019.modkt.api.fairy.IFairyType
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.api.mana.ManaTypes
import miragefairy2019.modkt.impl.fairy.displayName
import miragefairy2019.modkt.impl.mana.displayName
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.DARK_PURPLE
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.RED

class MagicStatus<T>(
        private val name: String,
        private val function: IMagicStatusFunction<T>,
        private val formatter: IMagicStatusFormatter<T>
) : IMagicStatus<T> {
    override fun getName() = name
    override fun getFunction() = function
    override fun getFormatter() = formatter
}

data class MagicStatusFunctionArguments(
        private val skillLevel: Int,
        private val fairyType: IFairyType
) : IMagicStatusFunctionArguments {
    override fun getSkillLevel() = skillLevel
    override fun getFairyType() = fairyType
}


val <T> IMagicStatus<T>.displayName get() = textComponent { translate("mirageFairy2019.magic.status.$name.name") }

fun <T> IMagicStatus<T>.getDisplayValue(arguments: IMagicStatusFunctionArguments): ITextComponent = formatter.getDisplayValue(function, arguments)

val <T> IMagicStatusFunction<T>.defaultValue: T get() = getValue(MagicStatusFunctionArguments(0, ApiFairy.empty()))

val <T> IMagicStatusFunction<T>.factors
    get(): Iterable<ITextComponent> {
        val factors = mutableListOf<ITextComponent>()
        getValue(MagicStatusFunctionArguments(0, object : IFairyType {
            fun add(textComponent: ITextComponent): Double {
                factors.add(textComponent)
                return 0.0
            }

            override fun isEmpty() = throw UnsupportedOperationException()
            override fun getBreed() = throw UnsupportedOperationException()
            override fun getDisplayName() = throw UnsupportedOperationException()
            override fun getColor() = throw UnsupportedOperationException()
            override fun getCost() = add(buildText { translate("mirageFairy2019.formula.source.cost.name").color(DARK_PURPLE) }) // TODO 色変更
            override fun getManaSet() = object : IManaSet {
                override fun getShine() = add(ManaTypes.shine.displayName)
                override fun getFire() = add(ManaTypes.fire.displayName)
                override fun getWind() = add(ManaTypes.wind.displayName)
                override fun getGaia() = add(ManaTypes.gaia.displayName)
                override fun getAqua() = add(ManaTypes.aqua.displayName)
                override fun getDark() = add(ManaTypes.dark.displayName) // TODO 色変更
            }

            override fun getErgSet() = object : IErgSet {
                override fun getEntries() = throw UnsupportedOperationException()
                override fun getPower(type: IErgType) = add(type.displayName)
            }
        }))
        return factors
    }


fun <T : Comparable<T>> IMagicStatusFormatter<T>.coloredBySign(colorPositive: TextFormatting, colorNegative: TextFormatting) = IMagicStatusFormatter<T> { function, fairyType ->
    val value = function.getValue(fairyType)
    val defaultValue = function.defaultValue
    val displayValue = this@coloredBySign.getDisplayValue(function, fairyType)
    when {
        value > defaultValue -> displayValue.color(colorPositive)
        value < defaultValue -> displayValue.color(colorNegative)
        else -> displayValue
    }
}

val <T : Comparable<T>> IMagicStatusFormatter<T>.positive get() = coloredBySign(GREEN, RED)
val <T : Comparable<T>> IMagicStatusFormatter<T>.negative get() = coloredBySign(RED, GREEN)

fun IMagicStatusFormatter<Boolean>.boolean(isPositive: Boolean) = IMagicStatusFormatter<Boolean> { function, fairyType ->
    val value = function.getValue(fairyType)
    val displayValue = this@boolean.getDisplayValue(function, fairyType)
    displayValue.color(if (value xor !isPositive) GREEN else RED)
}

val IMagicStatusFormatter<Boolean>.positiveBoolean get() = boolean(true)
val IMagicStatusFormatter<Boolean>.negativeBoolean get() = boolean(false)

fun <T : Comparable<T>> IMagicStatus<T>.ranged(min: T, max: T) = object : IMagicStatus<T> {
    override fun getName() = this@ranged.name
    override fun getFunction() = IMagicStatusFunction { this@ranged.function.getValue(it).coerceIn(min, max) }
    override fun getFormatter() = IMagicStatusFormatter<T> { function, fairyType ->
        val value = function.getValue(fairyType)
        val defaultValue = function.defaultValue
        val displayValue = this@ranged.formatter.getDisplayValue(function, fairyType)
        when (value) {
            defaultValue -> displayValue
            min -> displayValue.bold
            max -> displayValue.bold
            else -> displayValue
        }
    }
}
