package miragefairy2019.mod3.fairy

import miragefairy2019.libkt.TextComponentBuilder
import miragefairy2019.libkt.buildText
import miragefairy2019.libkt.color
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod.api.fairyweapon.item.IItemFairyWeapon
import miragefairy2019.mod.lib.ItemMulti
import miragefairy2019.mod.lib.ItemVariant
import miragefairy2019.mod3.erg.displayName
import miragefairy2019.mod3.fairy.api.IFairyType
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.displayName
import miragefairy2019.mod3.mana.getMana
import miragefairy2019.mod3.mana.textColor
import mirrg.boron.util.UtilsString
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting.AQUA
import net.minecraft.util.text.TextFormatting.BLUE
import net.minecraft.util.text.TextFormatting.DARK_GRAY
import net.minecraft.util.text.TextFormatting.DARK_PURPLE
import net.minecraft.util.text.TextFormatting.GOLD
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.LIGHT_PURPLE
import net.minecraft.util.text.TextFormatting.RED
import net.minecraft.util.text.TextFormatting.WHITE
import net.minecraft.util.text.TextFormatting.YELLOW
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Optional

class VariantFairy(val id: Int, val colorSet: ColorSet, val type: FairyType, val rare: Int, val rank: Int) : ItemVariant()

val VariantFairy.level get() = rare + rank - 1

fun hasSameId(a: VariantFairy, b: VariantFairy) = a.id == b.id

class ItemFairy : ItemMulti<VariantFairy>(), IItemFairy {
    override fun getMirageFairy2019Fairy(itemStack: ItemStack): Optional<IFairyType> = Optional.ofNullable(getVariant(itemStack)?.type)
    override fun getItemStackDisplayName(itemStack: ItemStack): String = getMirageFairy2019Fairy(itemStack).map { translateToLocalFormatted("$unlocalizedName.format", it.displayName.formattedText) }.orElseGet { translateToLocal("$unlocalizedName.name") }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return
        fun tooltip(block: TextComponentBuilder.() -> Unit) = run { tooltip.add(buildText { block() }.formattedText); Unit }
        fun formatInt(value: Double) = value.toInt().let { if (it == 0 && value > 0) 1 else it }


        // 番号　MOD名
        if (flag.isAdvanced) {
            tooltip {
                text("No: ${variant.id} (${variant.type.motif?.resourceDomain ?: "unknown"})").color(GREEN) // TODO translate
            }
        }

        // レア　ランク　品種名
        tooltip {
            fun getRankColor(variant: VariantFairy) = when (variant.rank) {
                1 -> RED
                2 -> BLUE
                3 -> GREEN
                4 -> YELLOW
                5 -> DARK_GRAY
                6 -> WHITE
                7 -> AQUA
                else -> LIGHT_PURPLE
            }

            text("モチーフ: ") // TODO translate
            text(UtilsString.repeat("★", variant.rare)).color(GOLD)
            if (flag.isAdvanced) {
                text(UtilsString.repeat("★", variant.rank - 1)).color(getRankColor(variant))
            } else {
                text(UtilsString.repeat("★", variant.rank - 1)).color(GOLD)
            }
            if (flag.isAdvanced) text(" レア度.${variant.rare}").color(GOLD) // TODO translate
            if (flag.isAdvanced) text(" ランク.${variant.rank}").color(getRankColor(variant)) // TODO translate
            text(" ${variant.type.motif?.resourcePath ?: "unknown"}").color(WHITE)
        }

        // マナ
        tooltip {
            text("マナ: ").color(AQUA) // TODO translate
        }
        if (flag.isAdvanced) {
            fun f(manaType: EnumManaType) = buildText { format("%s:%.3f", manaType.displayName.unformattedText, variant.type.manaSet.getMana(manaType)).color(manaType.textColor) }
            tooltip {
                text("        ")
                text(f(EnumManaType.SHINE))
            }
            tooltip {
                text(f(EnumManaType.FIRE))
                text("    ")
                text(f(EnumManaType.WIND))
            }
            tooltip {
                text(f(EnumManaType.GAIA))
                text("    ")
                text(f(EnumManaType.AQUA))
            }
            tooltip {
                text("        ")
                text(f(EnumManaType.DARK))
            }
        } else {
            fun f(manaType: EnumManaType) = buildText { format("%4d", formatInt(variant.type.manaSet.getMana(manaType))).color(manaType.textColor) }
            tooltip {
                text("    ")
                text(f(EnumManaType.SHINE))
            }
            tooltip {
                text(f(EnumManaType.FIRE))
                text("    ")
                text(f(EnumManaType.WIND))
            }
            tooltip {
                text(f(EnumManaType.GAIA))
                text("    ")
                text(f(EnumManaType.AQUA))
            }
            tooltip {
                text("    ")
                text(f(EnumManaType.DARK))
            }
        }

        // コスト
        tooltip {
            if (flag.isAdvanced) {
                format("コスト: %.3f", variant.type.cost).color(DARK_PURPLE) // TODO translate
            } else {
                format("コスト: %.0f", variant.type.cost).color(DARK_PURPLE) // TODO translate
            }
        }

        // エルグ
        fun <T> Iterable<T>.sandwich(separator: T) = mapIndexed { i, it -> if (i == 0) listOf(it) else listOf(separator, it) }.flatten()
        tooltip {
            text {
                text("エルグ: ") // TODO translate
                variant.type.ergSet.entries
                    .filter { formatInt(it.power) >= 10 }
                    .sortedByDescending { it.power }
                    .map {
                        buildText {
                            text(it.type.displayName)
                            if (flag.isAdvanced) format("(%.3f)", it.power)
                        }
                    }
                    .sandwich(buildText { text(", ") })
                    .forEach { text(it) }
            }.color(GREEN)
        }

        // 妖精武器のステータス
        Minecraft.getMinecraft().player?.let { player ->
            fun f(itemStackFairyWeapon: ItemStack) {
                val item = itemStackFairyWeapon.item as? IItemFairyWeapon ?: return
                tooltip {
                    text {
                        text("妖精武器: ") // TODO translate
                        text(TextComponentString(itemStackFairyWeapon.displayName)).color(WHITE)
                    }.color(BLUE)
                }
                item.addInformationFairyWeapon(itemStackFairyWeapon, itemStack, variant.type, world, tooltip, flag)
            }
            f(player.getHeldItem(EnumHand.MAIN_HAND))
            f(player.getHeldItem(EnumHand.OFF_HAND))
        }

    }
}

val ItemStack.fairyVariant get() = (item as? ItemFairy)?.getVariant(this)
