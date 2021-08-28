package miragefairy2019.modkt.impl.magicstatus

import miragefairy2019.libkt.bold
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.IFairyType
import miragefairy2019.modkt.api.erg.IErgSet
import miragefairy2019.modkt.api.erg.IErgType
import miragefairy2019.modkt.api.magicstatus.IMagicStatus
import miragefairy2019.modkt.api.magicstatus.IMagicStatusFormatter
import miragefairy2019.modkt.api.magicstatus.IMagicStatusFunction
import miragefairy2019.modkt.api.mana.IManaSet
import miragefairy2019.modkt.api.mana.ManaTypes
import miragefairy2019.modkt.impl.fairy.displayName
import miragefairy2019.modkt.impl.mana.displayName
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TextFormatting.*

class MagicStatus<T>(
        private val name: String,
        private val function: IMagicStatusFunction<T>,
        private val formatter: IMagicStatusFormatter<T>
) : IMagicStatus<T> {
    override fun getName() = name
    override fun getFunction() = function
    override fun getFormatter() = formatter
}

open class MagicStatusAdapter<T>(internal val parent: IMagicStatus<T>) : IMagicStatus<T> {
    override fun getName(): String = parent.name
    override fun getFunction(): IMagicStatusFunction<T> = parent.function
    override fun getFormatter(): IMagicStatusFormatter<T> = parent.formatter
}


val <T> IMagicStatus<T>.displayName get() = buildText { translate("mirageFairy2019.magic.status.$name.name") }

val <T> IMagicStatusFunction<T>.defaultValue: T get() = getValue(ApiFairy.empty())

val <T> IMagicStatusFunction<T>.factors
    get(): Iterable<ITextComponent> {
        val factors = mutableListOf<ITextComponent>()
        getValue(object : IFairyType {
            fun add(textComponent: ITextComponent): Double {
                factors.add(textComponent)
                return 0.0
            }

            override fun isEmpty() = throw UnsupportedOperationException()
            override fun getName() = throw UnsupportedOperationException()
            override fun getColor() = throw UnsupportedOperationException()
            override fun getCost() = add(buildText { translate("mirageFairy2019.formula.source.cost.name").color(DARK_PURPLE) })
            override fun getManas() = object : IManaSet {
                override fun getShine() = add(ManaTypes.shine.displayName)
                override fun getFire() = add(ManaTypes.fire.displayName)
                override fun getWind() = add(ManaTypes.wind.displayName)
                override fun getGaia() = add(ManaTypes.gaia.displayName)
                override fun getAqua() = add(ManaTypes.aqua.displayName)
                override fun getDark() = add(ManaTypes.dark.displayName)
            }

            override fun getAbilities() = object : IErgSet {
                override fun getEntries() = throw UnsupportedOperationException()
                override fun getPower(type: IErgType) = add(type.displayName)
            }

            override fun getDisplayName() = throw UnsupportedOperationException()
        })
        return factors
    }


fun <T : Comparable<T>> IMagicStatus<T>.coloredBySign(colorPositive: TextFormatting, colorNegative: TextFormatting): IMagicStatus<T> = object : MagicStatusAdapter<T>(this) {
    override fun getFormatter() = IMagicStatusFormatter<T> { function, fairyType ->
        val value = function.getValue(fairyType)
        val defaultValue = function.defaultValue
        val displayValue = parent.formatter.getDisplayValue(function, fairyType)
        when {
            value > defaultValue -> displayValue.color(colorPositive)
            value < defaultValue -> displayValue.color(colorNegative)
            else -> displayValue
        }
    }
}

fun <T : Comparable<T>> IMagicStatus<T>.positive() = coloredBySign(GREEN, RED)
fun <T : Comparable<T>> IMagicStatus<T>.negative() = coloredBySign(RED, GREEN)

fun IMagicStatus<Boolean>.boolean(isPositive: Boolean): IMagicStatus<Boolean> = object : MagicStatusAdapter<Boolean>(this) {
    override fun getFormatter() = IMagicStatusFormatter<Boolean> { function, fairyType ->
        val value = function.getValue(fairyType)
        val displayValue = parent.formatter.getDisplayValue(function, fairyType)
        displayValue.color(if (value xor !isPositive) GREEN else RED)
    }
}

fun IMagicStatus<Boolean>.positiveBoolean() = boolean(true)
fun IMagicStatus<Boolean>.negativeBoolean() = boolean(false)

fun <T : Comparable<T>> IMagicStatus<T>.ranged(min: T, max: T): IMagicStatus<T> = object : MagicStatusAdapter<T>(this) {
    override fun getFunction() = IMagicStatusFunction { parent.function.getValue(it).coerceIn(min, max) }
    override fun getFormatter() = IMagicStatusFormatter<T> { function, fairyType ->
        val value = function.getValue(fairyType)
        val defaultValue = function.defaultValue
        val displayValue = parent.formatter.getDisplayValue(function, fairyType)
        when {
            value == defaultValue -> displayValue
            value == min -> displayValue.bold()
            value == max -> displayValue.bold()
            else -> displayValue
        }
    }
}


fun getActualFairyType(fairyType: IFairyType, playerAura: IManaSet): IFairyType = object : IFairyType {
    override fun isEmpty(): Boolean = fairyType.isEmpty
    override fun getName(): ResourceLocation = fairyType.name
    override fun getColor(): Int = fairyType.color
    override fun getCost(): Double = fairyType.cost
    override fun getManas() = object : IManaSet {
        override fun getShine() = fairyType.manas.shine + playerAura.shine
        override fun getFire() = fairyType.manas.fire + playerAura.fire
        override fun getWind() = fairyType.manas.wind + playerAura.wind
        override fun getGaia() = fairyType.manas.gaia + playerAura.gaia
        override fun getAqua() = fairyType.manas.aqua + playerAura.aqua
        override fun getDark() = fairyType.manas.dark + playerAura.dark
    }

    override fun getAbilities(): IErgSet = fairyType.abilities
    override fun getDisplayName(): ITextComponent = fairyType.displayName
}
