package miragefairy2019.mod.fairy

import miragefairy2019.api.ErgSet
import miragefairy2019.api.IFairyItem
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.IFairyWeaponItem
import miragefairy2019.api.Mana
import miragefairy2019.api.ManaSet
import miragefairy2019.lib.IColoredItem
import miragefairy2019.lib.displayName
import miragefairy2019.lib.entries
import miragefairy2019.lib.get
import miragefairy2019.lib.textColor
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.concat
import miragefairy2019.libkt.concatNotNull
import miragefairy2019.libkt.darkGray
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
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
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

class VariantFairy(
    val fairyCard: FairyCard,
    private val motif: ResourceLocation?,
    private val displayName: ITextComponent,
    private val baseManaSet: ManaSet,
    private val ergSet: ErgSet,
    val rank: Int
) : ItemVariant(), IFairySpec {
    override fun isEmpty() = false
    override fun getMotif() = motif
    override fun getDisplayName() = displayName
    override fun getColor() = fairyCard.colorSet.hair
    override fun getCost() = fairyCard.cost.toDouble()
    override fun getManaSet() = baseManaSet
    override fun getErgSet() = ergSet
}

val VariantFairy.id get() = fairyCard.id
val VariantFairy.rare get() = fairyCard.rare
val VariantFairy.colorSet get() = fairyCard.colorSet

val VariantFairy.level get() = rare + rank - 1

fun hasSameId(a: VariantFairy, b: VariantFairy) = a.id == b.id

class ItemFairy(val dressColor: Int) : ItemMulti<VariantFairy>(), IColoredItem, IFairyItem {
    override fun getMirageFairy(itemStack: ItemStack) = getVariant(itemStack)
    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val fairySpec = getMirageFairy(itemStack) ?: return translateToLocal("$unlocalizedName.name")
        return translateToLocalFormatted("$unlocalizedName.format", fairySpec.displayName.formattedText)
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return

        fun formatInt(value: Double) = value.toInt().let { if (it == 0 && value > 0) 1 else it }


        // 番号　MOD名
        if (flag.isAdvanced) {
            tooltip += formattedText { "No: ${variant.id} (${variant.motif?.resourceDomain ?: "unknown"})"().green } // TODO translate
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
                " ${variant.motif?.resourcePath ?: "unknown"}"().white
            )
        }

        // マナ
        tooltip += formattedText { "マナ: "().aqua } // TODO translate
        if (flag.isAdvanced) {
            fun f(mana: Mana) = textComponent {
                val raw = variant.manaSet[mana]
                val normalized = variant.manaSet[mana] * variant.cost / 50.0
                (format("%.3f", raw) + " ["() + format("%.3f", normalized) + "]"()).withColor(mana.textColor)
            }
            tooltip += formattedText { "        "() + f(Mana.SHINE)() }
            tooltip += formattedText { f(Mana.FIRE)() + "    "() + f(Mana.WIND)() }
            tooltip += formattedText { f(Mana.GAIA)() + "    "() + f(Mana.AQUA)() }
            tooltip += formattedText { "        "() + f(Mana.DARK)() }
        } else {
            fun f(mana: Mana) = textComponent { format("%4d", formatInt(variant.manaSet[mana])).withColor(mana.textColor) }
            tooltip += formattedText { "    "() + f(Mana.SHINE)() }
            tooltip += formattedText { f(Mana.FIRE)() + "    "() + f(Mana.WIND)() }
            tooltip += formattedText { f(Mana.GAIA)() + "    "() + f(Mana.AQUA)() }
            tooltip += formattedText { "    "() + f(Mana.DARK)() }
        }

        // コスト
        if (flag.isAdvanced) {
            tooltip += formattedText { format("コスト: %.3f", variant.cost).darkGray } // TODO translate
        } else {
            tooltip += formattedText { format("コスト: %.0f", variant.cost).darkGray } // TODO translate
        }

        // エルグ
        tooltip += formattedText {
            concat(
                "エルグ: "(), // TODO translate
                variant.ergSet.entries
                    .filter { formatInt(it.second) >= 10 }
                    .sortedByDescending { it.second }
                    .map {
                        if (flag.isAdvanced) {
                            it.first.displayName() + format("(%.3f [%.3f])", it.second, it.second * (variant.cost / 50.0))
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
                val item = itemStackFairyWeapon.item as? IFairyWeaponItem ?: return
                tooltip += formattedText { ("妖精武器: "() + itemStackFairyWeapon.displayName().white).blue } // TODO translate
                tooltip += NonNullList.create<String>().also { item.addInformationFairyWeapon(itemStackFairyWeapon, itemStack, variant, world, it, flag) }
            }
            f(player.getHeldItem(EnumHand.MAIN_HAND))
            f(player.getHeldItem(EnumHand.OFF_HAND))
        }

    }

    @SideOnly(Side.CLIENT)
    override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
        val variant = getVariant(itemStack) ?: return 0xFFFFFF
        return when (tintIndex) {
            0 -> variant.colorSet.skin
            1 -> dressColor
            2 -> variant.colorSet.dark
            3 -> variant.colorSet.bright
            4 -> variant.colorSet.hair
            else -> 0xFFFFFF
        }
    }
}

val ItemStack.fairyVariant get() = (item as? ItemFairy)?.getVariant(this)
