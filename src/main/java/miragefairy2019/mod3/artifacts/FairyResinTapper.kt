package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.block
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.darkRed
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.tileEntity
import miragefairy2019.mod3.artifacts.treecompile.TreeCompileException
import miragefairy2019.mod3.main.api.ApiMain
import mirrg.boron.util.UtilsMath
import mirrg.kotlin.formatAs
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World

object FairyResinTapper {
    lateinit var blockFairyResinTapper: () -> BlockFairyResinTapper
    lateinit var itemBlockFairyResinTapper: () -> ItemBlock
    val module: Module = {
        blockFairyResinTapper = block({ BlockFairyResinTapper() }, "fairy_resin_tapper") {
            setUnlocalizedName("fairyResinTapper")
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                        "facing=${facing.first}" to DataBlockState(
                            "miragefairy2019:fairy_resin_tapper",
                            y = facing.second
                        )
                    }
                )
            }
        }
        itemBlockFairyResinTapper = item({ ItemBlock(blockFairyResinTapper()) }, "fairy_resin_tapper") {
            setCustomModelResourceLocation(variant = "facing=north")
        }
        tileEntity("fairy_resin_tapper", TileEntityFairyResinTapper::class.java)
    }
}

class BlockFairyResinTapper : BlockFairyBoxBase() {
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyResinTapper()
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        val tileEntity = worldIn.getTileEntity(pos) as? TileEntityFairyResinTapper ?: return false
        val executor = tileEntity.executor ?: return false
        return executor.onBlockActivated(playerIn, hand, facing, hitX, hitY, hitZ)
    }
}

class TileEntityFairyResinTapper : TileEntityFairyBoxBase() {
    override val executor: TileEntityExecutor?
        get() {
            val blockState = world.getBlockState(pos)
            val block = blockState.block as? BlockFairyResinTapper ?: return null
            val facing = block.getFacing(blockState)
            val blockPosOutput = pos.offset(facing)

            fun getErrorExecutor(textComponent: ITextComponent) = object : TileEntityExecutor() {
                override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                    player.sendStatusMessage(textComponent, true)
                    return true
                }
            }

            // 目の前にアイテムがある場合は行動しない（Lazy Chunk対策）
            if (world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPosOutput)).isNotEmpty()) return getErrorExecutor(textComponent { (!"制作物があふれています").darkRed }) // TODO translation

            // 排出面が塞がれている場合は行動しない
            if (world.getBlockState(blockPosOutput).isSideSolid(world, blockPosOutput, facing.opposite)) return getErrorExecutor(textComponent { (!"妖精が入れません").darkRed }) // TODO translation

            // 妖精の木のコンパイルに失敗した場合は抜ける
            val leaves = try {
                compileFairyTree(world, pos)
            } catch (e: TreeCompileException) {
                return getErrorExecutor(e.description)
            }

            // 成立
            return object : TileEntityExecutor() {
                override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                    val times = 10000

                    val auraCollectionSpeed = getAuraCollectionSpeed(world, leaves, times).coerceAtMost(120.0)
                    val baseCount = (auraCollectionSpeed / smallTreeAuraCollectionSpeed - 0.5).coerceAtLeast(0.0)

                    player.sendStatusMessage(textComponent { (!"オーラ吸収速度: ${auraCollectionSpeed formatAs "%.2f"} Folia, 生産速度: ${baseCount formatAs "%.2f"} 個/分") }, true) // TODO translation
                    return true
                }

                override fun onUpdateTick() {
                    val times = 10

                    val auraCollectionSpeed = getAuraCollectionSpeed(world, leaves, times).coerceAtMost(120.0)
                    val baseCount = (auraCollectionSpeed / smallTreeAuraCollectionSpeed - 0.5).coerceAtLeast(0.0)

                    val count = UtilsMath.randomInt(world.rand, baseCount)
                    if (count > 0) FairyMaterials.itemVariants.fairyWoodResin.createItemStack(count).drop(world, blockPosOutput, motionless = true)
                }
            }
        }
}
