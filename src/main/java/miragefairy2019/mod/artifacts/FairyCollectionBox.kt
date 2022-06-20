package miragefairy2019.mod.artifacts

import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.writeToNBT
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.block
import miragefairy2019.libkt.drawGuiBackground
import miragefairy2019.libkt.drawSlot
import miragefairy2019.libkt.drawStringRightAligned
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.rectangle
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.x
import miragefairy2019.libkt.y
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairy.fairyVariant
import miragefairy2019.mod.fairy.hasSameId
import miragefairy2019.mod.fairy.level
import miragefairy2019.resourcemaker.DataBlockModel
import miragefairy2019.resourcemaker.DataBlockState
import miragefairy2019.resourcemaker.DataBlockStates
import miragefairy2019.resourcemaker.makeBlockModel
import miragefairy2019.resourcemaker.makeBlockStates
import miragefairy2019.util.InventoryTileEntity
import miragefairy2019.util.SmartSlot
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import net.minecraft.block.BlockContainer
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.IStringSerializable
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object FairyCollectionBox {
    lateinit var blockFairyCollectionBox: () -> BlockFairyCollectionBox
    lateinit var itemFairyCollectionBox: () -> ItemBlock

    val module = module {
        blockFairyCollectionBox = block({ BlockFairyCollectionBox() }, "fairy_collection_box") {
            setUnlocalizedName("fairyCollectionBox")
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataBlockStates(
                    variants = listOf("middle", "bottom").flatMap { context ->
                        listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).map { facing ->
                            "context=$context,facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_building_$context", y = facing.second)
                        }
                    }.toMap()
                )
            }
        }
        itemFairyCollectionBox = item({ ItemBlock(blockFairyCollectionBox()) }, "fairy_collection_box") {
            setCustomModelResourceLocation(variant = "context=bottom,facing=north")
        }
        tileEntity("fairy_collection_box", TileEntityFairyCollectionBox::class.java)
        onInit {
            Main.registerGuiHandler(GuiId.fairyCollectionBox, object : ISimpleGuiHandler {
                override fun GuiHandlerContext.onServer() = (tileEntity as? TileEntityFairyCollectionBox)?.let { ContainerFairyCollectionBox(player.inventory, it.inventory) }
                override fun GuiHandlerContext.onClient() = (tileEntity as? TileEntityFairyCollectionBox)?.let { GuiFairyCollectionBox(ContainerFairyCollectionBox(player.inventory, it.inventory)) }
            }.guiHandler)
        }
        makeBlockModel("fairy_building_bottom") {
            DataBlockModel(
                parent = "block/block",
                textures = mapOf(
                    "particle" to "blocks/log_oak",
                    "top" to "miragefairy2019:blocks/fairy_building_top",
                    "side_background" to "miragefairy2019:blocks/fairy_building_side_background",
                    "side_light_1" to "miragefairy2019:blocks/fairy_building_side_light_1",
                    "side_light_2" to "miragefairy2019:blocks/fairy_building_side_light_2",
                    "front_background" to "miragefairy2019:blocks/fairy_building_front_background",
                    "front_light_1" to "miragefairy2019:blocks/fairy_building_front_light_1",
                    "front_light_2" to "miragefairy2019:blocks/fairy_building_front_light_2",
                    "log_top" to "blocks/log_oak_top",
                    "log_side" to "blocks/log_oak"
                ),
                elements = listOf(
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            14.jsonElement,
                            16.jsonElement,
                            14.jsonElement
                        ),
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#front_background".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            14.jsonElement,
                            16.jsonElement,
                            14.jsonElement
                        ),
                        "faces" to jsonObject(
                            "north" to jsonObject(
                                "texture" to "#front_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            14.jsonElement,
                            16.jsonElement,
                            14.jsonElement
                        ),
                        "faces" to jsonObject(
                            "north" to jsonObject(
                                "texture" to "#front_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            14.jsonElement
                        ),
                        "to" to jsonArray(
                            12.jsonElement,
                            2.jsonElement,
                            16.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            6.jsonElement,
                            0.jsonElement,
                            14.jsonElement
                        ),
                        "to" to jsonArray(
                            8.jsonElement,
                            6.jsonElement,
                            16.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            10.jsonElement,
                            0.jsonElement,
                            14.jsonElement
                        ),
                        "to" to jsonArray(
                            12.jsonElement,
                            10.jsonElement,
                            16.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            14.jsonElement,
                            0.jsonElement,
                            8.jsonElement
                        ),
                        "to" to jsonArray(
                            16.jsonElement,
                            2.jsonElement,
                            14.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            14.jsonElement,
                            0.jsonElement,
                            10.jsonElement
                        ),
                        "to" to jsonArray(
                            16.jsonElement,
                            8.jsonElement,
                            12.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            14.jsonElement,
                            0.jsonElement,
                            0.jsonElement
                        ),
                        "to" to jsonArray(
                            16.jsonElement,
                            2.jsonElement,
                            4.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            14.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            16.jsonElement,
                            4.jsonElement,
                            4.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            4.jsonElement,
                            0.jsonElement,
                            0.jsonElement
                        ),
                        "to" to jsonArray(
                            6.jsonElement,
                            4.jsonElement,
                            2.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            0.jsonElement,
                            0.jsonElement,
                            4.jsonElement
                        ),
                        "to" to jsonArray(
                            2.jsonElement,
                            4.jsonElement,
                            6.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            0.jsonElement,
                            0.jsonElement,
                            6.jsonElement
                        ),
                        "to" to jsonArray(
                            2.jsonElement,
                            12.jsonElement,
                            8.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            0.jsonElement,
                            0.jsonElement,
                            8.jsonElement
                        ),
                        "to" to jsonArray(
                            2.jsonElement,
                            6.jsonElement,
                            10.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            0.jsonElement,
                            0.jsonElement,
                            10.jsonElement
                        ),
                        "to" to jsonArray(
                            2.jsonElement,
                            2.jsonElement,
                            12.jsonElement
                        ),
                        "shade" to false.jsonElement,
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#log_top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#log_side".jsonElement
                            )
                        )
                    )
                )
            )
        }
        makeBlockModel("fairy_building_middle") {
            DataBlockModel(
                parent = "block/block",
                textures = mapOf(
                    "particle" to "blocks/log_oak",
                    "top" to "miragefairy2019:blocks/fairy_building_top",
                    "side_background" to "miragefairy2019:blocks/fairy_building_side_background",
                    "side_light_1" to "miragefairy2019:blocks/fairy_building_side_light_1",
                    "side_light_2" to "miragefairy2019:blocks/fairy_building_side_light_2"
                ),
                elements = listOf(
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            14.jsonElement,
                            16.jsonElement,
                            14.jsonElement
                        ),
                        "faces" to jsonObject(
                            "down" to jsonObject(
                                "texture" to "#top".jsonElement
                            ),
                            "up" to jsonObject(
                                "texture" to "#top".jsonElement
                            ),
                            "north" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#side_background".jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            14.jsonElement,
                            16.jsonElement,
                            14.jsonElement
                        ),
                        "faces" to jsonObject(
                            "north" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#side_light_1".jsonElement,
                                "tintindex" to 0.jsonElement
                            )
                        )
                    ),
                    jsonObject(
                        "from" to jsonArray(
                            2.jsonElement,
                            0.jsonElement,
                            2.jsonElement
                        ),
                        "to" to jsonArray(
                            14.jsonElement,
                            16.jsonElement,
                            14.jsonElement
                        ),
                        "faces" to jsonObject(
                            "north" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            ),
                            "south" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            ),
                            "west" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            ),
                            "east" to jsonObject(
                                "texture" to "#side_light_2".jsonElement,
                                "tintindex" to 1.jsonElement
                            )
                        )
                    )
                )
            )
        }
    }
}

class BlockFairyCollectionBox : BlockContainer(Material.WOOD) {
    init {

        // meta
        defaultState = blockState.baseState.withProperty(FACING, EnumFacing.NORTH).withProperty(CONTEXT, EnumFairyCollectionBoxContext.BOTTOM)

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(1.0f)
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

    override fun createBlockState() = BlockStateContainer(this, FACING, CONTEXT)

    override fun getActualState(blockState: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState = if (world.getBlockState(pos.down()).block is BlockFairyCollectionBox) {
        blockState.withProperty(CONTEXT, EnumFairyCollectionBoxContext.MIDDLE)
    } else {
        blockState
    }

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState = state.withProperty(FACING, rot.rotate(state.getValue(FACING)))
    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState = state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)))

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.horizontalFacing.opposite), 2)
    }


    // TileEntity
    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityFairyCollectionBox()


    // Graphics
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
    override fun getRenderType(state: IBlockState) = EnumBlockRenderType.MODEL
    override fun isOpaqueCube(state: IBlockState) = false
    override fun isFullCube(state: IBlockState) = false


    // Drop
    override fun breakBlock(world: World, blockPos: BlockPos, blockState: IBlockState) {
        val tileEntity = world.getTileEntity(blockPos)
        if (tileEntity is TileEntityFairyCollectionBox) {
            tileEntity.inventory.itemStacks.forEach { InventoryHelper.spawnItemStack(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), it) }
            world.updateComparatorOutputLevel(blockPos, this)
        }
        super.breakBlock(world, blockPos, blockState)
    }


    // Action
    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (worldIn.isRemote) return true
        playerIn.openGui(ModMirageFairy2019.instance, GuiId.fairyCollectionBox, worldIn, pos.x, pos.y, pos.z)
        return true
    }


    companion object {
        val FACING: PropertyEnum<EnumFacing> = PropertyEnum.create("facing", EnumFacing::class.java, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST)
        val CONTEXT: PropertyEnum<EnumFairyCollectionBoxContext> = PropertyEnum.create("context", EnumFairyCollectionBoxContext::class.java)

        enum class EnumFairyCollectionBoxContext : IStringSerializable {
            MIDDLE, BOTTOM, ;

            override fun getName(): String = name.toLowerCase()
            override fun toString(): String = name.toLowerCase()
        }
    }
}

class TileEntityFairyCollectionBox : TileEntity() {
    val inventory = InventoryFairyCollectionBox(this, "tile.fairyCollectionBox.name", false, 50)

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        inventory.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        inventory.writeToNBT(nbt)
        return nbt
    }
}

class InventoryFairyCollectionBox(tileEntity: TileEntityFairyCollectionBox, title: String, customName: Boolean, slotCount: Int) : InventoryTileEntity<TileEntityFairyCollectionBox>(tileEntity, title, customName, slotCount) {
    override fun getInventoryStackLimit() = 1
    override fun isItemValidForSlot(index: Int, itemStack: ItemStack): Boolean {
        val variant = itemStack.fairyVariant ?: return false // スタンダード妖精でないと受け付けない
        return itemStacks
            .filterIndexed { i, _ -> i != index } // 他スロットにおいて
            .all a@{ itemStack2 -> !hasSameId(variant, itemStack2.fairyVariant ?: return@a true) } // 同種の妖精があってはならない
    }
}

class ContainerFairyCollectionBox(val inventoryPlayer: IInventory, val inventoryTileEntity: IInventory) : Container() {
    init {
        var yi = 17
        repeat(5) { r -> repeat(10) { c -> addSlotToContainer(SmartSlot(inventoryTileEntity, 10 * r + c, 7 + 18 * c + 1, yi + 18 * r + 1)) } }
        yi += 18 * 5 + 13
        repeat(3) { r -> repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, 9 + 9 * r + c, 7 + 9 + 18 * c + 1, yi + 18 * r + 1)) } }
        yi += 18 * 3 + 4
        repeat(9) { c -> addSlotToContainer(Slot(inventoryPlayer, c, 7 + 9 + 18 * c + 1, yi + 1)) }
    }

    override fun canInteractWith(playerIn: EntityPlayer) = inventoryTileEntity.isUsableByPlayer(playerIn)

    val fairyMasterGrade get() = inventoryTileEntity.itemStacks.mapNotNull { itemStack -> itemStack.fairyVariant }.distinctBy { it.id }.sumBy { it.level }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        val slot = inventorySlots[index] ?: return ItemStack.EMPTY // スロットがnullなら終了
        if (!slot.hasStack) return ItemStack.EMPTY // スロットが空なら終了

        val itemStack = slot.stack
        val itemStackOriginal = itemStack.copy()

        // 移動処理
        // itemStackを改変する
        if (index < 50) { // タイルエンティティ→プレイヤー
            if (!mergeItemStack(itemStack, 50, 50 + 9 * 4, true)) return ItemStack.EMPTY
        } else { // プレイヤー→タイルエンティティ
            if (!mergeItemStack(itemStack, 0, 50, false)) return ItemStack.EMPTY
        }

        if (itemStack.isEmpty) { // スタックが丸ごと移動した
            slot.putStack(ItemStack.EMPTY)
        } else { // 部分的に残った
            slot.onSlotChanged()
        }

        if (itemStack.count == itemStackOriginal.count) return ItemStack.EMPTY // アイテムが何も移動していない場合は終了

        // スロットが改変を受けた場合にここを通過する

        slot.onTake(playerIn, itemStack)

        return itemStackOriginal
    }
}

@SideOnly(Side.CLIENT)
class GuiFairyCollectionBox(val container: ContainerFairyCollectionBox) : GuiContainer(container) {
    init {
        xSize = 7 + 18 * 10 + 7
        ySize = 17 + 18 * 5 + 13 + 18 * 3 + 4 + 18 + 7
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
        renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        rectangle.drawGuiBackground()

        var yi = 17
        repeat(5) { r -> repeat(10) { c -> drawSlot((x + 7 + 18 * c).toFloat(), (y + yi + 18 * r).toFloat()) } }
        yi += 18 * 5 + 13
        repeat(3) { r -> repeat(9) { c -> drawSlot((x + 7 + 9 + 18 * c).toFloat(), (y + yi + 18 * r).toFloat()) } }
        yi += 18 * 3 + 4
        repeat(9) { c -> drawSlot((x + 7 + 9 + 18 * c).toFloat(), (y + yi).toFloat()) }
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        var yi = 7
        fontRenderer.drawString(container.inventoryTileEntity.displayName.unformattedText, yi + 1, yi - 1, 0x404040)
        fontRenderer.drawStringRightAligned("グレード: ${container.fairyMasterGrade}", xSize - 7 - 1, yi - 1, 0x000088) // TODO translate Grade
        yi += 10 + 18 * 5 + 3
        fontRenderer.drawString(container.inventoryPlayer.displayName.unformattedText, 7 + 1, yi - 1, 0x404040)
    }
}
