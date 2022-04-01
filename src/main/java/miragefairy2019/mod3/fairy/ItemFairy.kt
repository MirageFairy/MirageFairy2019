package miragefairy2019.mod3.fairy

import miragefairy2019.api.Mana
import miragefairy2019.lib.displayName
import miragefairy2019.lib.entries
import miragefairy2019.lib.get
import miragefairy2019.lib.textColor
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.concat
import miragefairy2019.libkt.concatNotNull
import miragefairy2019.libkt.darkPurple
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.green
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.libkt.white
import miragefairy2019.libkt.withColor
import miragefairy2019.api.IFairyItem
import miragefairy2019.mod.api.fairyweapon.item.IItemFairyWeapon
import miragefairy2019.mod.lib.ItemMulti
import miragefairy2019.mod.lib.ItemVariant
import miragefairy2019.api.IFairyType
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextFormatting.AQUA
import net.minecraft.util.text.TextFormatting.BLUE
import net.minecraft.util.text.TextFormatting.DARK_GRAY
import net.minecraft.util.text.TextFormatting.GREEN
import net.minecraft.util.text.TextFormatting.LIGHT_PURPLE
import net.minecraft.util.text.TextFormatting.RED
import net.minecraft.util.text.TextFormatting.WHITE
import net.minecraft.util.text.TextFormatting.YELLOW
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Optional

class VariantFairy(val id: Int, val colorSet: ColorSet, val type: FairyType, val rare: Int, val rank: Int, val isDilutable: Boolean) : ItemVariant()

val VariantFairy.level get() = rare + rank - 1

fun hasSameId(a: VariantFairy, b: VariantFairy) = a.id == b.id

class ItemFairy : ItemMulti<VariantFairy>(), IFairyItem {
    override fun getMirageFairy(itemStack: ItemStack): Optional<IFairyType> = Optional.ofNullable(getVariant(itemStack)?.type)
    override fun getItemStackDisplayName(itemStack: ItemStack): String = getMirageFairy(itemStack).map {
        translateToLocalFormatted("$unlocalizedName.format", it.displayName.formattedText)
    }.orElseGet { translateToLocal("$unlocalizedName.name") }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return

        fun formatInt(value: Double) = value.toInt().let { if (it == 0 && value > 0) 1 else it }


        // 番号　MOD名
        if (flag.isAdvanced) {
            tooltip += formattedText { "No: ${variant.id} (${variant.type.motif?.resourceDomain ?: "unknown"})"().green } // TODO translate
        }

        // レア　ランク　品種名
        tooltip += formattedText {
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

            concatNotNull(
                "モチーフ: "(), // TODO translate
                "★".repeat(variant.rare)().gold,
                if (flag.isAdvanced) {
                    "★".repeat(variant.rank - 1)().withColor(getRankColor(variant))
                } else {
                    "★".repeat(variant.rank - 1)().gold
                },
                if (flag.isAdvanced) " レア度.${variant.rare}"().gold else null, // TODO translate
                if (flag.isAdvanced) " ランク.${variant.rank}"().withColor(getRankColor(variant)) else null, // TODO translate
                " ${variant.type.motif?.resourcePath ?: "unknown"}"().white
            )
        }

        // マナ
        tooltip += formattedText { "マナ: "().aqua } // TODO translate
        if (flag.isAdvanced) {
            fun f(mana: Mana) = textComponent {
                val raw = variant.type.manaSet[mana] / (variant.type.cost / 50.0)
                val normalized = variant.type.manaSet[mana]
                (format("%.3f", raw) + " ["() + format("%.3f", normalized) + "]"()).withColor(mana.textColor)
            }
            tooltip += formattedText { "        "() + f(Mana.SHINE)() }
            tooltip += formattedText { f(Mana.FIRE)() + "    "() + f(Mana.WIND)() }
            tooltip += formattedText { f(Mana.GAIA)() + "    "() + f(Mana.AQUA)() }
            tooltip += formattedText { "        "() + f(Mana.DARK)() }
        } else {
            fun f(mana: Mana) = textComponent { format("%4d", formatInt(variant.type.manaSet[mana] / (variant.type.cost / 50.0))).withColor(mana.textColor) }
            tooltip += formattedText { "    "() + f(Mana.SHINE)() }
            tooltip += formattedText { f(Mana.FIRE)() + "    "() + f(Mana.WIND)() }
            tooltip += formattedText { f(Mana.GAIA)() + "    "() + f(Mana.AQUA)() }
            tooltip += formattedText { "    "() + f(Mana.DARK)() }
        }

        // コスト
        if (flag.isAdvanced) {
            tooltip += formattedText { format("コスト: %.3f", variant.type.cost).darkPurple } // TODO translate
        } else {
            tooltip += formattedText { format("コスト: %.0f", variant.type.cost).darkPurple } // TODO translate
        }

        // エルグ
        tooltip += formattedText {
            concat(
                "エルグ: "(), // TODO translate
                variant.type.ergSet.entries
                    .filter { formatInt(it.second) >= 10 }
                    .sortedByDescending { it.second }
                    .map {
                        if (flag.isAdvanced) {
                            it.first.displayName() + format("(%.3f [%.3f])", it.second, it.second * (variant.type.cost / 50.0))
                        } else {
                            it.first.displayName()
                        }
                    }
                    .sandwich { ", "() }
                    .flatten()
            ).green
        }

        // 妖精武器のステータス
        Minecraft.getMinecraft().player?.let { player ->
            fun f(itemStackFairyWeapon: ItemStack) {
                val item = itemStackFairyWeapon.item as? IItemFairyWeapon ?: return
                tooltip += formattedText { ("妖精武器: "() + itemStackFairyWeapon.displayName().white).blue } // TODO translate
                item.addInformationFairyWeapon(itemStackFairyWeapon, itemStack, variant.type, world, tooltip, flag)
            }
            f(player.getHeldItem(EnumHand.MAIN_HAND))
            f(player.getHeldItem(EnumHand.OFF_HAND))
        }

    }
}

val ItemStack.fairyVariant get() = (item as? ItemFairy)?.getVariant(this)
