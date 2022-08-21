package miragefairy2019.mod.fairybox

import miragefairy2019.lib.TileEntityIgnoreBlockState
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.translateToLocal
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ITickable
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random
import kotlin.math.floor
import kotlin.math.log

class BlockFairyBoxBase(private val tier: Int, private val tileEntityProvider: () -> TileEntityFairyBoxBase) : BlockContainer(Material.WOOD) {
    init {

        // meta
        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.NORTH)

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(2.0f)
        setHarvestLevel("axe", 0)

    }


    // Variant

    override fun getMetaFromState(state: IBlockState) = when (state.getValue(FACING)) {
        EnumFacing.NORTH -> 0
        EnumFacing.SOUTH -> 1
        EnumFacing.WEST -> 2
        EnumFacing.EAST -> 3
        else -> 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState = when (meta) {
        0 -> defaultState.withProperty(FACING, EnumFacing.NORTH)
        1 -> defaultState.withProperty(FACING, EnumFacing.SOUTH)
        2 -> defaultState.withProperty(FACING, EnumFacing.WEST)
        3 -> defaultState.withProperty(FACING, EnumFacing.EAST)
        else -> defaultState
    }

    override fun createBlockState() = BlockStateContainer(this, FACING)

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))

    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)))

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.horizontalFacing.opposite), 2)
    }

    fun getFacing(blockState: IBlockState): EnumFacing = blockState.getValue(FACING)


    // Tile Entity
    override fun createNewTileEntity(worldIn: World, meta: Int) = tileEntityProvider()


    // Graphics
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL


    // View

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, tooltipFlag: ITooltipFlag) {
        super.addInformation(itemStack, world, tooltip, tooltipFlag)

        if (canTranslate("$unlocalizedName.poem")) { // ポエム
            val string = translateToLocal("$unlocalizedName.poem")
            if (string.isNotBlank()) tooltip += formattedText { string() }
        }

        tooltip += formattedText { "Tier $tier"().aqua } // tier // TRANSLATE

    }


    // Action

    fun getTileEntity(world: World, blockPos: BlockPos) = world.getTileEntity(blockPos)?.castOrNull<TileEntityFairyBoxBase>()

    // 右クリック
    override fun onBlockActivated(world: World, blockPos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tileEntity = getTileEntity(world, blockPos) ?: return super.onBlockActivated(world, blockPos, state, playerIn, hand, facing, hitX, hitY, hitZ)
        return tileEntity.getExecutor().onBlockActivated(playerIn, hand, facing, hitX, hitY, hitZ)
    }

    // 破壊時ドロップ
    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        val tileEntity = world.getTileEntity(blockPos)
        if (tileEntity is TileEntityFairyBoxBase) {
            val itemStacks = tileEntity.getDropItemStacks()
            itemStacks.forEach { InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), it) }
            world.updateComparatorOutputLevel(blockPos, this)
        }
        super.breakBlock(world, blockPos, blockState)
    }


    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
    }
}

abstract class TileEntityFairyBoxBase : TileEntityIgnoreBlockState(), ITickable {

    // ブロック

    fun getBlockState(): IBlockState = world.getBlockState(pos)
    fun getBlock() = getBlockState().block as? BlockFairyBoxBase
    fun getFacing() = getBlock()?.getFacing(getBlockState()) ?: EnumFacing.SOUTH


    // 特性

    open fun getDropItemStacks() = listOf<ItemStack>()


    // 行動

    private var tick = -1
    override fun update() {
        if (world.isRemote) return // サーバーワールドのみ

        // 平均して1分に1回行動する
        val interval = 20 * 60
        if (tick < 0) tick = randomSkipTicks(world.rand, 1 / interval.toDouble())
        if (tick != 0) {
            tick--
            return
        } else {
            tick = randomSkipTicks(world.rand, 1 / interval.toDouble())
        }

        getExecutor().onUpdateTick()
    }

    open fun getExecutor() = object : IFairyBoxExecutor {}


    // データ

    override fun getUpdatePacket() = SPacketUpdateTileEntity(pos, 9, updateTag)

    override fun getUpdateTag(): NBTTagCompound = writeToNBT(NBTTagCompound())

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
        super.onDataPacket(net, pkt)
    }

    fun sendUpdatePacket() = world.playerEntities.forEach { if (it is EntityPlayerMP) it.connection.sendPacket(updatePacket) }

}

interface IFairyBoxExecutor {
    fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = false
    fun onUpdateTick() = Unit
}

class FailureFairyBoxExecutor(val message: ITextComponent) : IFairyBoxExecutor {
    override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        player.sendStatusMessage(message, true)
        return true
    }
}

/** @return 0以上の値 */
fun randomSkipTicks(random: Random, rate: Double) = floor(log(random.nextDouble(), 1 - rate)).toInt()
