package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.hex
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.toRgb
import miragefairy2019.libkt.with
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.fairy.FairyTypes
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.mana.sum
import miragefairy2019.modkt.impl.fairy.ColorSet
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.mana
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.io.File

object DebugItems {
    lateinit var itemDebugFairyList: () -> ItemDebugFairyList
    val module: Module = {

        // 妖精一覧デバッグアイテム
        itemDebugFairyList = item({ ItemDebugFairyList() }, "debug_fairy_list") {
            setUnlocalizedName("debugFairyList")
            setCreativeTab { ApiMain.creativeTab }
            setCustomModelResourceLocation(model = ResourceLocation("book"))
        }

    }
}

class ItemDebugFairyList : Item() {
    companion object {
        val Double.f0 get() = this with "%.0f"
        val Double.f3 get() = this with "%.3f"
    }

    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (!world.isRemote) return EnumActionResult.SUCCESS

        fun getLang(lang: String) = ItemDebugFairyList::class.java.getResource("/assets/miragefairy2019/lang/$lang.lang")!!.readText().split("""\r\n?|\n""".toRegex())
            .filter { it.isNotBlank() }
            .map { it.split("=", limit = 2) }
            .filter { it.size == 2 }
            .associate { Pair(it[0], it[1]) }

        val enUs = getLang("en_us")
        val jaJp = getLang("ja_jp")

        val fileDest = File("./debug/fairyList.txt")
        player.sendStatusMessage(textComponent { !"Saved to " + !fileDest }, false)
        fileDest.parentFile.mkdirs()
        fileDest.writeText(
            FairyTypes.instance.variants.joinToString("") { (id, bundle) ->
                val variantRank1 = bundle.main
                val variantRank2 = bundle[1]
                val fairyTypeRank1 = variantRank1.type
                val fairyTypeRank2 = variantRank2.type
                fun color(selector: ColorSet.() -> Int) = variantRank1.colorSet.selector().toRgb().hex
                val motif = fairyTypeRank1.motif!!.resourcePath
                "|${
                    listOf(
                        listOf(
                            id,
                            "&bold(){!FairyImage(#${color { skin }},#${color { bright }},#${color { dark }},#${color { hair }})}",
                            "CENTER:$motif&br()${enUs["mirageFairy2019.fairy.$motif.name"]!!}",
                            "CENTER:${jaJp["mirageFairy2019.fairy.$motif.name"]!!.replace("""(?<![ァ-ヶー])(?=[ァ-ヶー])""".toRegex(), "&br()")}",
                            "CENTER:${variantRank1.rare}",
                            "RIGHT:${fairyTypeRank1.cost.f0}"
                        ),
                        EnumManaType.values().map {
                            "RIGHT:${fairyTypeRank1.mana(it).f3}"
                        },
                        listOf(
                            "RIGHT:${fairyTypeRank1.manaSet.sum.f3}"
                        ),
                        EnumManaType.values().map {
                            val a1 = fairyTypeRank1.mana(it) / fairyTypeRank1.cost * 50
                            val a2 = fairyTypeRank2.mana(it) / fairyTypeRank2.cost * 50
                            "${if (a1 >= 10) "BGCOLOR(#FDD):" else if (a2 >= 10) "BGCOLOR(#DDF):" else ""}RIGHT:${a1.f3}"
                        },
                        listOf(
                            "RIGHT:${(fairyTypeRank1.manaSet.sum / fairyTypeRank1.cost * 50).f3}"
                        ),
                        EnumErgType.values().map {
                            val a1 = fairyTypeRank1.erg(it)
                            val a2 = fairyTypeRank2.erg(it)
                            "${if (a1 >= 10) "BGCOLOR(#FDD):" else if (a2 >= 10) "BGCOLOR(#DDF):" else ""}RIGHT:${a1.f3}"
                        }
                    ).flatten().joinToString("|")
                }|\n"
            }
        )

        return EnumActionResult.SUCCESS
    }
}
