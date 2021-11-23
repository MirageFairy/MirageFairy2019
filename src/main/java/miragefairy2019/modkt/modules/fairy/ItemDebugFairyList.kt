package miragefairy2019.modkt.modules.fairy

import miragefairy2019.libkt.hex
import miragefairy2019.libkt.stringFormat
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.toRgb
import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.modkt.api.mana.ManaTypes
import miragefairy2019.modkt.impl.fairy.ColorSet
import miragefairy2019.modkt.impl.fairy.erg
import miragefairy2019.modkt.impl.fairy.mana
import miragefairy2019.modkt.impl.sum
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.io.File

class ItemDebugFairyList : Item() {
    companion object {
        val Double.f0 get() = stringFormat("%.0f")
        val Double.f3 get() = stringFormat("%.3f")
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
        player.sendStatusMessage(textComponent { !"Saved to " + !fileDest.absolutePath }, false)
        fileDest.parentFile.mkdirs()
        fileDest.writeText(
                FairyTypes.instance.variants.joinToString("") { (id, b) ->
                    val v = b.main
                    val f = v.type
                    fun color(selector: ColorSet.() -> Int) = v.colorSet.selector().toRgb().hex
                    val type = f.breed!!.resourcePath
                    "|${
                        listOf(
                                listOf(
                                        id,
                                        "&bold(){!FairyImage(#${color { skin }},#${color { bright }},#${color { dark }},#${color { hair }})}",
                                        "CENTER:$type&br()${enUs["mirageFairy2019.fairy.$type.name"]!!}",
                                        "CENTER:${jaJp["mirageFairy2019.fairy.$type.name"]!!.replace("""(?<![ァ-ヶー])(?=[ァ-ヶー])""".toRegex(), "&br()")}",
                                        "CENTER:${v.rare}",
                                        "RIGHT:${f.cost.f0}"
                                ),
                                ManaTypes.values.map {
                                    "RIGHT:${f.mana(it).f3}"
                                },
                                listOf(
                                        "RIGHT:${f.manaSet.sum.f3}"
                                ),
                                ManaTypes.values.map {
                                    val a = f.mana(it) / f.cost * 50
                                    "${if (a >= 10) "BGCOLOR(#FDB):" else ""}RIGHT:${a.f3}"
                                },
                                listOf(
                                        "RIGHT:${(f.manaSet.sum / f.cost * 50).f3}"
                                ),
                                ErgTypes.values.map {
                                    val a = f.erg(it)
                                    "${if (a >= 10) "BGCOLOR(#FDB):" else ""}RIGHT:${a.f3}"
                                }
                        ).flatten().joinToString("|")
                    }|\n"
                }
        )

        return EnumActionResult.SUCCESS
    }
}
