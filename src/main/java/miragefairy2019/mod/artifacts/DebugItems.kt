package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.erg
import miragefairy2019.lib.mana
import miragefairy2019.lib.sum
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.green
import miragefairy2019.libkt.hex
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.toRgb
import miragefairy2019.libkt.yellow
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairy.ColorSet
import miragefairy2019.mod.fairy.FairyTypes
import miragefairy2019.mod.oreseed.ApiOreSeedDrop
import miragefairy2019.mod.oreseed.EnumOreSeedType
import miragefairy2019.mod.oreseed.EnumVariantOreSeed
import miragefairy2019.mod.oreseed.OreSeedDropEnvironment
import miragefairy2019.mod.playeraura.ApiPlayerAura
import miragefairy2019.mod.skill.ApiSkill
import mirrg.kotlin.formatAs
import mirrg.kotlin.join
import mirrg.kotlin.log4j.hydrogen.getLogger
import mirrg.kotlin.toLowerCamelCase
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.oredict.OreDictionary
import java.io.File

val debugItemsModule = module {

    fun r(itemCreator: () -> Item, name: String, english: String, japanese: String) {
        val unlocalizedName = "debug_$name".toLowerCamelCase()
        item({ itemCreator() }, "debug_$name") {
            setUnlocalizedName(unlocalizedName)
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation(model = ResourceLocation("book"))
        }
        onMakeLang { enJa("item.$unlocalizedName.name", "Debug: $english", "デバッグ：$japanese") }
    }

    r({ ItemDebugFairyList() }, "fairy_list", "Fairy List", "妖精一覧")
    r({ ItemDebugOreNameList() }, "ore_name_list", "Ore Name List", "鉱石辞書名一覧")
    r({ ItemDebugSkillResetUnlock() }, "skill_reset_unlock", "Skill Reset Unlock", "スキルリセット解禁")
    r({ ItemDebugPlayerAuraReset() }, "player_aura_reset", "Player Aura Reset", "プレイヤーオーラリセット")
    r({ ItemDebugGainFairyMasterExp() }, "gain_fairy_master_exp", "Gain Fairy Master Exp", "妖精経験値入手")
    r({ ItemDebugOreSeedStatistics() }, "ore_seed_statistics", "Ore Seed Statistics", "鉱石分布")
    r({ ItemDebugOreSeedDropRate() }, "ore_seed_drop_rate", "Ore Seed Drop Rate", "鉱石生成確率表示")
    r({ ItemDebugMirageFlowerGrowthRateList() }, "mirage_flower_growth_rate_list", "Mirage Flower Growth Rate List", "ミラージュフラワー地面補正一覧")
    r({ ItemDebugMirageFlowerGrowthRate() }, "mirage_flower_growth_rate", "Mirage Flower Growth Rate", "ミラージュフラワー成長速度表示")
    r({ ItemDebugShowData() }, "show_data", "Show Data", "データ表示")

}

private val Double.f0 get() = this formatAs "%.0f"
private val Double.f3 get() = this formatAs "%.3f"

private fun writeAction(player: EntityPlayer, fileName: String, text: String) {
    val file = File("debug").resolve(fileName)
    player.sendStatusMessage(textComponent { "Saved to "() + file() }, false)
    file.parentFile.mkdirs()
    file.writeText(text)
}

open class ItemDebug : Item() {
    init {
        maxStackSize = 1
    }
}

class ItemDebugFairyList : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (!world.isRemote) return EnumActionResult.SUCCESS

        fun getLang(lang: String) = ItemDebugFairyList::class.java.getResource("/assets/miragefairy2019/lang/$lang.lang")!!.readText().split("""\r\n?|\n""".toRegex())
            .filter { it.isNotBlank() }
            .map { it.split("=", limit = 2) }
            .filter { it.size == 2 }
            .associate { Pair(it[0], it[1]) }

        val enUs = getLang("en_us")
        val jaJp = getLang("ja_jp")

        writeAction(player, "fairyList.txt", FairyTypes.instance.variants.joinToString("") { (id, bundle) ->
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
                    Mana.values().map {
                        "RIGHT:${fairyTypeRank1.mana(it).f3}"
                    },
                    listOf(
                        "RIGHT:${fairyTypeRank1.manaSet.sum.f3}"
                    ),
                    Mana.values().map {
                        val a1 = fairyTypeRank1.mana(it) / fairyTypeRank1.cost * 50
                        val a2 = fairyTypeRank2.mana(it) / fairyTypeRank2.cost * 50
                        "${if (a1 >= 10) "BGCOLOR(#FDD):" else if (a2 >= 10) "BGCOLOR(#DDF):" else ""}RIGHT:${a1.f3}"
                    },
                    listOf(
                        "RIGHT:${(fairyTypeRank1.manaSet.sum / fairyTypeRank1.cost * 50).f3}"
                    ),
                    Erg.values().map {
                        val a1 = fairyTypeRank1.erg(it)
                        val a2 = fairyTypeRank2.erg(it)
                        "${if (a1 >= 10) "BGCOLOR(#FDD):" else if (a2 >= 10) "BGCOLOR(#DDF):" else ""}RIGHT:${a1.f3}"
                    }
                ).flatten().joinToString("|")
            }|\n"
        })

        return EnumActionResult.SUCCESS
    }
}

class ItemDebugOreNameList : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (!world.isRemote) return EnumActionResult.SUCCESS
        writeAction(player, "oreNameList.txt", OreDictionary.getOreNames()
            .sorted()
            .filter { OreDictionary.getOres(it).isNotEmpty() }
            .joinToString("") { "$it\n" })
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugSkillResetUnlock : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
        skillContainer.variables.setLastMasteryResetTime(null)
        if (player is EntityPlayerMP) skillContainer.send(player)
        player.sendStatusMessage(textComponent { "スキルポイント初期化が可能になりました"() }, true) // TODO translate
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugPlayerAuraReset : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        if (player !is EntityPlayerMP) return EnumActionResult.SUCCESS
        val playerAuraHandler = ApiPlayerAura.playerAuraManager.getServerPlayerAuraHandler(player)
        playerAuraHandler.onReset()
        playerAuraHandler.send()
        player.sendStatusMessage(textComponent { "プレイヤーオーラをリセットしました"() }, true) // TODO translate
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugGainFairyMasterExp : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        if (player !is EntityPlayerMP) return EnumActionResult.SUCCESS
        val skillContainer = ApiSkill.skillManager.getServerSkillContainer(player)
        ItemAstronomicalObservationBook.gainExp(player, if (player.isSneaking) -100 else 100)
        skillContainer.send(player)
        return EnumActionResult.SUCCESS
    }
}

class ItemDebugOreSeedStatistics : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS
        val map = mutableMapOf<IBlockState, Int>()

        fun processChunk(chunkX: Int, chunkZ: Int) {
            val baseBlockPos = BlockPos(16 * chunkX, 0, 16 * chunkZ)
            (0 until 16).forEach { xi ->
                (0 until 256).forEach { yi ->
                    (0 until 16).forEach { zi ->
                        val blockState = world.getBlockState(baseBlockPos.add(xi, yi, zi))
                        when (blockState.block) {
                            OreSeed.blockOreSeed(),
                            OreSeed.blockOreSeedNether(),
                            OreSeed.blockOreSeedEnd()
                            -> map.compute(blockState) { _, value -> (value ?: 0) + 1 }
                        }
                    }
                }
            }
        }

        val chunkX = Math.floorDiv(blockPos.x, 16)
        val chunkZ = Math.floorDiv(blockPos.z, 16)

        (-4..4).forEach { x ->
            (-4..4).forEach { z ->
                processChunk(chunkX + x, chunkZ + z)
            }
        }

        // 鉱石生成確率表示
        player.sendStatusMessage(textComponent { "===== Ore Seed | Chunk: (${chunkX - 4}, ${chunkZ - 4}) .. (${chunkX + 4}, ${chunkZ + 4}) ====="() }, false)
        map.entries.sortedBy { it.key.block.getMetaFromState(it.key) }.sortedBy { Block.getIdFromBlock(it.key.block) }.forEach {
            player.sendStatusMessage(textComponent { "${it.key}: ${it.value}"() }, false)
        }
        player.sendStatusMessage(textComponent { "===================="() }, false)

        return EnumActionResult.SUCCESS
    }
}

class ItemDebugOreSeedDropRate : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS

        val type = when {
            BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.NETHER) -> EnumOreSeedType.NETHERRACK
            BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.END) -> EnumOreSeedType.END_STONE
            else -> EnumOreSeedType.STONE
        }

        // 鉱石生成確率表示
        player.sendStatusMessage(textComponent { "===== Ore List ($type) ====="() }, false)
        EnumVariantOreSeed.values().forEach { variant ->
            player.sendStatusMessage(textComponent { "----- ${variant.name} -----"() }, false)
            ApiOreSeedDrop.oreSeedDropRegistry.getDropList(OreSeedDropEnvironment(type, variant.shape, world, blockPos)).forEach {
                player.sendStatusMessage(textComponent { it.weight.f3() + ": "() + it.item().block.getItem(world, blockPos, it.item()).displayName() }, false)
            }
        }
        player.sendStatusMessage(textComponent { "===================="() }, false)

        return EnumActionResult.SUCCESS
    }
}

class ItemDebugMirageFlowerGrowthRateList : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS

        player.sendStatusMessage(textComponent { "===== Mirage Flower Grow Rate Table ====="() }, false)
        FairyTypes.instance.variants.map { it.bundle.main.type }.map { Pair(it, getGrowthRateInFloor(it)) }.filter { it.second > 1 }.sortedBy { it.second }.forEach {
            player.sendStatusMessage(textComponent { ((it.second * 100) formatAs "%7.2f%%  ")() + it.first.displayName() }, false)
        }
        player.sendStatusMessage(textComponent { "===================="() }, false)

        return EnumActionResult.SUCCESS
    }
}

class ItemDebugMirageFlowerGrowthRate : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS

        val airBlockPos = if (world.getBlockState(blockPos).isFullBlock) blockPos.up() else blockPos
        player.sendStatusMessage(textComponent { "===== Mirage Flower Grow Rate ====="() }, false)
        player.sendStatusMessage(textComponent { "Pos: ${airBlockPos.x} ${airBlockPos.y} ${airBlockPos.z}"() }, false)
        player.sendStatusMessage(textComponent { "Block: ${world.getBlockState(airBlockPos)}"() }, false)
        player.sendStatusMessage(textComponent { "Floor: ${world.getBlockState(airBlockPos.down())}"() }, false)
        val result = calculateGrowthRate(world, airBlockPos)
        player.sendStatusMessage(textComponent { "Growth Rate: "() + (result.growthRate * 100).f3() + "%"() }, false)
        result.forEach { (key, value) ->
            player.sendStatusMessage(textComponent { "  $key: "() + (value * 100).f3() + "%"() }, false)
        }
        player.sendStatusMessage(textComponent { "===================="() }, false)

        return EnumActionResult.SUCCESS
    }
}

class ItemDebugShowData : ItemDebug() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        if (world.isRemote) return EnumActionResult.SUCCESS

        val list = mutableListOf<ITextComponent>()

        list.add(textComponent { "===== Block Info ====="() })

        list.add(textComponent { "Position: "() + "(${blockPos.x}, ${blockPos.y}, ${blockPos.z})"().aqua })

        val blockState = world.getBlockState(blockPos).getActualState(world, blockPos)
        list.add(textComponent { "Block State: "() + "$blockState"().yellow })

        val block = blockState.block
        list.add(textComponent { "Block: "() + "${block.registryName}"().green + " as "() + block.javaClass.simpleName().blue })

        val tileEntity = world.getTileEntity(blockPos)
        if (tileEntity != null) {
            list.add(textComponent { "Tile Entity: "() + "${TileEntity.getKey(tileEntity.javaClass)}"().green + " as "() + tileEntity.javaClass.simpleName().blue })
            tileEntity.serializeNBT().toString().chunked(80).forEach { string ->
                list.add(textComponent { string().yellow })
            }
        }

        list.add(textComponent { "===================="() })


        list.forEach { message ->
            player.sendStatusMessage(message, false)
        }
        getLogger().info(list.map { it.unformattedText }.join("\n"))

        return EnumActionResult.SUCCESS
    }
}
