package miragefairy2019.mod.fairy.pedestal

import miragefairy2019.api.IPlaceExchanger
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.modinitializer.tileEntityRenderer
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.mod.placeditem.PlacedItem
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentKeybind
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

val itemFrameModule = module {

    // ブロック登録
    val blockItemFrame = block({ BlockItemFrame() }, "item_frame") {
        setUnlocalizedName("itemFrame")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataBlockStates(
                variants = mapOf(
                    "facing=down" to DataBlockState(model = "miragefairy2019:item_frame"),
                    "facing=south" to DataBlockState(model = "miragefairy2019:item_frame", x = 90),
                    "facing=west" to DataBlockState(model = "miragefairy2019:item_frame", x = 90, y = 90),
                    "facing=north" to DataBlockState(model = "miragefairy2019:item_frame", x = 90, y = 180),
                    "facing=east" to DataBlockState(model = "miragefairy2019:item_frame", x = 90, y = 270),
                    "facing=up" to DataBlockState(model = "miragefairy2019:item_frame", x = 180)
                )
            )
        }
        makeBlockModel {
            val frameFaces = DataFaces(
                down = DataFace(texture = "#wood"),
                up = DataFace(texture = "#wood"),
                north = DataFace(texture = "#wood"),
                south = DataFace(texture = "#wood"),
                west = DataFace(texture = "#wood"),
                east = DataFace(texture = "#wood")
            )
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "minecraft:blocks/planks_oak",
                    "wood" to "minecraft:blocks/planks_oak",
                    "back" to "minecraft:blocks/itemframe_background"
                ),
                elements = listOf(
                    DataElement(
                        from = DataPoint(2.0, 0.0, 2.0),
                        to = DataPoint(14.0, 0.5, 14.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#wood"),
                            up = DataFace(texture = "#back"),
                            north = DataFace(texture = "#wood"),
                            south = DataFace(texture = "#wood"),
                            west = DataFace(texture = "#wood"),
                            east = DataFace(texture = "#wood")
                        )
                    ),
                    DataElement(
                        from = DataPoint(2.0, 0.0, 2.0),
                        to = DataPoint(3.0, 1.0, 14.0),
                        faces = frameFaces
                    ),
                    DataElement(
                        from = DataPoint(2.0, 0.0, 2.0),
                        to = DataPoint(14.0, 1.0, 3.0),
                        faces = frameFaces
                    ),
                    DataElement(
                        from = DataPoint(13.0, 0.0, 2.0),
                        to = DataPoint(14.0, 1.0, 14.0),
                        faces = frameFaces
                    ),
                    DataElement(
                        from = DataPoint(2.0, 0.0, 13.0),
                        to = DataPoint(14.0, 1.0, 14.0),
                        faces = frameFaces
                    )
                )
            )
        }
    }

    // アイテム登録
    item({ ItemBlock(blockItemFrame()) }, "item_frame") {
        setCustomModelResourceLocation(variant = "facing=down")
        makeItemModel { block }
        makeRecipe {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:item_frame"),
                    WandType.CRAFTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:item_frame")
            )
        }
    }

    // 翻訳生成
    onMakeLang { enJa("tile.itemFrame.name", "Item Frame Block", "額縁ブロック") }

    // タイルエンティティ登録
    tileEntity("item_frame", TileEntityItemFrame::class.java)
    tileEntityRenderer(TileEntityItemFrame::class.java) { TileEntityRendererPedestal() }

}

class BlockItemFrame : BlockPedestal<TileEntityItemFrame>(Material.WOOD, { it as? TileEntityItemFrame }) {
    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java)
    }

    init {
        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(0.8f)
        setHarvestLevel("axe", -1)

        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.DOWN)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityItemFrame()


    fun getBlockState(facing: EnumFacing): IBlockState = defaultState.withProperty(FACING, facing)
    fun getFacing(blockState: IBlockState): EnumFacing = blockState.getValue(FACING)
    fun getFacing(metadata: Int): EnumFacing = EnumFacing.VALUES[metadata]
    override fun createBlockState() = BlockStateContainer(this, FACING)
    override fun getStateFromMeta(metadata: Int) = getBlockState(getFacing(metadata))
    override fun getMetaFromState(blockState: IBlockState) = getFacing(blockState).index
    override fun withRotation(blockState: IBlockState, rotation: Rotation) = getBlockState(rotation.rotate(getFacing(blockState)))
    override fun withMirror(blockState: IBlockState, mirror: Mirror) = getBlockState(mirror.mirror(getFacing(blockState)))
    override fun getStateForPlacement(world: World, blockPos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, metadata: Int, placer: EntityLivingBase, hand: EnumHand) = getBlockState(facing.opposite)


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { (TextComponentKeybind(PlacedItem.keyBindingPlaceItem.keyDescription)() + "キーでアイテムを展示"()).red } // TODO translate
        tooltip += formattedText { ("右クリックで展示物を回転"()).red } // TODO translate
    }


    // 当たり判定

    override fun getBoundingBox(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos) = when (getFacing(blockState)) {
        EnumFacing.DOWN -> AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 1 / 16.0, 14 / 16.0)
        EnumFacing.UP -> AxisAlignedBB(2 / 16.0, 15 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
        EnumFacing.NORTH -> AxisAlignedBB(2 / 16.0, 2 / 16.0, 0 / 16.0, 14 / 16.0, 14 / 16.0, 1 / 16.0)
        EnumFacing.SOUTH -> AxisAlignedBB(2 / 16.0, 2 / 16.0, 15 / 16.0, 14 / 16.0, 14 / 16.0, 16 / 16.0)
        EnumFacing.WEST -> AxisAlignedBB(0 / 16.0, 2 / 16.0, 2 / 16.0, 1 / 16.0, 14 / 16.0, 14 / 16.0)
        EnumFacing.EAST -> AxisAlignedBB(15 / 16.0, 2 / 16.0, 2 / 16.0, 16 / 16.0, 14 / 16.0, 14 / 16.0)
    }

    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = NULL_AABB

}

class TileEntityItemFrame : TileEntityPedestal() {
    var rotation = 0.0

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        nbt.setDouble("rotation", rotation)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        rotation = nbt.getDouble("rotation")
    }

    override fun transform(transformProxy: ITransformProxy) {

        // 額縁の回転
        val blockState = world.getBlockState(pos)
        val facing = blockState.block.castOrNull<BlockItemFrame>()?.getFacing(blockState) ?: EnumFacing.DOWN
        transformProxy.translate(0.5, 0.5, 0.5)
        when (facing) {
            EnumFacing.DOWN -> Unit
            EnumFacing.UP -> {
                transformProxy.rotate(180.0f, 1.0f, 0.0f, 0.0f)
            }
            EnumFacing.NORTH -> {
                transformProxy.rotate(90.0f, 1.0f, 0.0f, 0.0f)
            }
            EnumFacing.EAST -> {
                transformProxy.rotate(-90.0f, 0.0f, 1.0f, 0.0f)
                transformProxy.rotate(90.0f, 1.0f, 0.0f, 0.0f)
            }
            EnumFacing.SOUTH -> {
                transformProxy.rotate(-180.0f, 0.0f, 1.0f, 0.0f)
                transformProxy.rotate(90.0f, 1.0f, 0.0f, 0.0f)
            }
            EnumFacing.WEST -> {
                transformProxy.rotate(-270.0f, 0.0f, 1.0f, 0.0f)
                transformProxy.rotate(90.0f, 1.0f, 0.0f, 0.0f)
            }
        }
        transformProxy.translate(-0.5, -0.5, -0.5)

        transformProxy.translate(0.5, 0.75 / 16.0 + 1 / 64.0, 0.5) // 位置を額縁の上に移動

        transformProxy.rotate(-rotation.toFloat(), 0.0f, 1.0f, 0.0f) // 展示物の回転角度の適用

        // アイテムの角度を設定
        transformProxy.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
        transformProxy.rotate(180.0f, 0.0f, 1.0f, 0.0f)

    }


    override fun onAdjust(player: EntityPlayer, placeExchanger: IPlaceExchanger): Boolean {
        if (super.onAdjust(player, placeExchanger)) return true

        if (player.isSneaking) {
            rotation = (rotation - 45.0 + 360.0) % 360.0
        } else {
            rotation = (rotation + 45.0) % 360.0
        }

        // TODO 設置撤去SEも額縁と同じに
        // TODO すべての展示台に回転SEを付ける
        world.playSound(null, pos, SoundEvents.ENTITY_ITEMFRAME_ROTATE_ITEM, SoundCategory.PLAYERS, 1.0f, 1.0f) // SE

        return true
    }

    override fun onDeploy(player: EntityPlayer) {
        rotation = 0.0
    }
}
