package miragefairy2019.mod.fairybox

import miragefairy2019.lib.PartiallyUnloadedTreeSearchException
import miragefairy2019.lib.TooLargeTreeSearchException
import miragefairy2019.lib.TreeSearchException
import miragefairy2019.lib.get
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.set
import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.material.FairyMaterialCard
import miragefairy2019.mod.material.createItemStack
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntityHopper
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB

lateinit var blockFairyResinTapper: () -> BlockFairyBoxBase
lateinit var itemBlockFairyResinTapper: () -> ItemBlock

val fairyResinTapperModule = module {

    // ブロック
    blockFairyResinTapper = block({ BlockFairyBoxBase(5) { TileEntityFairyResinTapper() } }, "fairy_resin_tapper") {
        setUnlocalizedName("fairyResinTapper")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataModelBlockDefinition(
                variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                    "facing=${facing.first}" to DataSingleVariantList(DataVariant("miragefairy2019:fairy_resin_tapper", y = facing.second))
                }
            )
        }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                elements = listOf(
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#end", cullface = "down"),
                            up = DataFace(texture = "#end", cullface = "up"),
                            north = DataFace(texture = "#side", cullface = "north"),
                            south = DataFace(texture = "#side", cullface = "south"),
                            west = DataFace(texture = "#side", cullface = "west"),
                            east = DataFace(texture = "#side", cullface = "east")
                        )
                    ),
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            north = DataFace(texture = "#entrance", cullface = "north"),
                            south = DataFace(texture = "#window", cullface = "south"),
                            west = DataFace(texture = "#window", cullface = "west"),
                            east = DataFace(texture = "#window", cullface = "east")
                        )
                    )
                ),
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/fairy_wood_log",
                    "end" to "miragefairy2019:blocks/fairy_wood_log_top",
                    "side" to "miragefairy2019:blocks/fairy_wood_log",
                    "entrance" to "miragefairy2019:blocks/fairy_resin_tapper_entrance",
                    "window" to "miragefairy2019:blocks/fairy_resin_tapper_window"
                )
            )
        }
    }

    // アイテム
    itemBlockFairyResinTapper = item({ ItemBlock(blockFairyResinTapper()) }, "fairy_resin_tapper") {
        setCustomModelResourceLocation(variant = "facing=north")
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "LsL",
                    "H#H",
                    "BcB"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:fairy_box"),
                    "L" to DataSimpleIngredient(item = "miragefairy2019:lilagium_scythe"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSlash"),
                    "H" to DataSimpleIngredient(item = "minecraft:hopper"),
                    "B" to DataSimpleIngredient(item = "minecraft:bowl"),
                    "c" to DataOreIngredient(ore = "mirageFairy2019SphereChemical")
                ),
                result = DataResult(item = "miragefairy2019:fairy_resin_tapper")
            )
        }
    }

    // タイルエンティティ登録
    tileEntity("fairy_resin_tapper", TileEntityFairyResinTapper::class.java)

    // 翻訳生成
    lang("tile.fairyResinTapper.name", "Fairy Resin Tapper", "樹液取り職人スプルーツァの家")
    lang("tile.fairyResinTapper.poem", "", "妖精だから、森に帰ります")

}

class TileEntityFairyResinTapper : TileEntityFairyBoxBase() {
    override fun getExecutor(): IFairyBoxExecutor {
        val facing = getFacing()
        val blockPosOutput = pos.offset(facing)

        // 目の前にアイテムがある場合は行動しない（Lazy Chunk対策）
        if (world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPosOutput)).isNotEmpty()) return FailureFairyBoxExecutor(textComponent { "制作物があふれています"().darkRed }) // TRANSLATE

        // 排出面が塞がれている場合は行動しない
        if (world.getBlockState(blockPosOutput).isSideSolid(world, blockPosOutput, facing.opposite)) return FailureFairyBoxExecutor(textComponent { "妖精が入れません"().darkRed }) // TRANSLATE

        // 妖精の木のコンパイルに失敗した場合は抜ける
        val leaves = try {
            compileFairyTree(world, pos)
        } catch (e: TreeCompileException) {
            return FailureFairyBoxExecutor(e.description)
        } catch (e: TreeSearchException) {
            return when (e) {
                is PartiallyUnloadedTreeSearchException -> FailureFairyBoxExecutor(textComponent { "構造物の一部がロード範囲外にあります"().darkRed }) // TRANSLATE
                is TooLargeTreeSearchException -> FailureFairyBoxExecutor(textComponent { "構造物が大きすぎます"().darkRed }) // TRANSLATE
            }
        }

        // 成立
        return object : IFairyBoxExecutor {
            override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                if (world.isRemote) return true
                val times = 10000

                val auraCollectionSpeed = getAuraCollectionSpeed(world, leaves, times) atMost 300.0
                val baseCount = auraCollectionSpeed / smallTreeAuraCollectionSpeed

                player.sendStatusMessage(textComponent { "オーラ吸収速度: ${auraCollectionSpeed formatAs "%.2f"} Folia, 生産速度: ${baseCount formatAs "%.2f"} 個/分"() }, true) // TRANSLATE
                return true
            }

            override fun onUpdateTick() {
                val times = 10

                val auraCollectionSpeed = getAuraCollectionSpeed(world, leaves, times) atMost 300.0
                val baseCount = auraCollectionSpeed / smallTreeAuraCollectionSpeed

                val count = world.rand.randomInt(baseCount)
                if (count > 0) {
                    val outputItemStack = FairyMaterialCard.FAIRY_WOOD_RESIN.createItemStack(count)

                    fun insert(): Boolean {
                        val hopperBlockPos = pos.down()
                        if (world.getBlockState(hopperBlockPos).block != Blocks.HOPPER) return false // ホッパーじゃない場合は失敗
                        val hopperTileEntity = world.getTileEntity(hopperBlockPos) as? TileEntityHopper ?: return false // ホッパーじゃない場合は失敗
                        if (!hopperTileEntity[0].isEmpty) return true // 最初のスロットが空でない場合は失敗
                        hopperTileEntity[0] = outputItemStack
                        return true
                    }

                    fun drop() {
                        outputItemStack.drop(world, blockPosOutput, motionless = true)
                    }

                    if (!insert()) drop()
                }
            }
        }
    }
}
