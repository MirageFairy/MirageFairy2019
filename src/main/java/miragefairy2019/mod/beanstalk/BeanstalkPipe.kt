package miragefairy2019.mod.beanstalk

import miragefairy2019.common.ResourceName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setStateMapper
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPart
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.get
import miragefairy2019.mod.Main
import mirrg.kotlin.gson.hydrogen.jsonElement
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

lateinit var blockBeanstalkPipe: () -> BlockBeanstalk
lateinit var itemBlockBeanstalkPipe: () -> ItemBlock

val beanstalkPipeModule = module {

    // ブロック
    blockBeanstalkPipe = block({ BlockBeanstalkPipe() }, "beanstalk_pipe") {
        setUnlocalizedName("beanstalkPipe")
        setCreativeTab { Main.creativeTab }
        setStateMapper {
            object : StateMapperBase() {
                override fun getModelResourceLocation(blockState: IBlockState): ModelResourceLocation {
                    fun model(path: String, variant: String) = ModelResourceLocation(ResourceLocation("miragefairy2019", path), variant)
                    val block = blockState.block as? BlockBeanstalkPipe ?: return model("beanstalk_pipe_straight", "facing=down")
                    if (block.isStraight(blockState)) return model("beanstalk_pipe_straight", "facing=${block.getFacing(blockState)}")
                    if (block.isEnd(blockState)) return model("beanstalk_pipe_end", "facing=${block.getFacing(blockState)}")
                    return model("beanstalk_pipe", getPropertyString(blockState.properties))
                }
            }
        }
    }

    // ブロックステート生成
    makeBlockStates("beanstalk_pipe_straight") {
        DataBlockStates(
            variants = Facing.values().associate { facing ->
                "facing=$facing" to DataSingleVariantList(DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_straight"), x = facing.x, y = facing.y))
            }
        )
    }
    makeBlockStates("beanstalk_pipe_end") {
        DataBlockStates(
            variants = Facing.values().associate { facing ->
                "facing=$facing" to DataSingleVariantList(DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_end"), x = facing.x, y = facing.y))
            }
        )
    }
    makeBlockStates("beanstalk_pipe") {
        DataBlockStates(
            multipart = listOf(
                DataPart(
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_elbow"))
                ),
                DataPart(
                    `when` = mapOf("facing" to "down".jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.DOWN.x, y = Facing.DOWN.y)
                ),
                DataPart(
                    `when` = mapOf("facing" to "up".jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.UP.x, y = Facing.UP.y)
                ),
                DataPart(
                    `when` = mapOf("facing" to "north".jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.NORTH.x, y = Facing.NORTH.y)
                ),
                DataPart(
                    `when` = mapOf("facing" to "south".jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.SOUTH.x, y = Facing.SOUTH.y)
                ),
                DataPart(
                    `when` = mapOf("facing" to "west".jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.WEST.x, y = Facing.WEST.y)
                ),
                DataPart(
                    `when` = mapOf("facing" to "east".jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.EAST.x, y = Facing.EAST.y)
                ),
                DataPart(
                    `when` = mapOf("down" to true.jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.DOWN.opposite.x, y = Facing.DOWN.opposite.y)
                ),
                DataPart(
                    `when` = mapOf("up" to true.jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.UP.opposite.x, y = Facing.UP.opposite.y)
                ),
                DataPart(
                    `when` = mapOf("north" to true.jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.NORTH.opposite.x, y = Facing.NORTH.opposite.y)
                ),
                DataPart(
                    `when` = mapOf("south" to true.jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.SOUTH.opposite.x, y = Facing.SOUTH.opposite.y)
                ),
                DataPart(
                    `when` = mapOf("west" to true.jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.WEST.opposite.x, y = Facing.WEST.opposite.y)
                ),
                DataPart(
                    `when` = mapOf("east" to true.jsonElement),
                    apply = DataBlockState(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.EAST.opposite.x, y = Facing.EAST.opposite.y)
                )
            )
        )
    }

    // ブロックモデル生成
    makeBlockModel("beanstalk_pipe_straight") {
        DataModel(
            parent = "block/block",
            ambientOcclusion = false,
            textures = mapOf(
                "particle" to "miragefairy2019:blocks/beanstalk",
                "main" to "miragefairy2019:blocks/beanstalk",
                "top" to "miragefairy2019:blocks/beanstalk_top"
            ),
            elements = listOf(
                ud(DataPoint(0.0, 0.0, 0.0), DataPoint(16.0, 16.0, 16.0), "#top", "#top"),
                northDuplex(DataPoint(0.0, 0.0, 5.0), DataPoint(16.0, 16.0, 5.0), "#main"),
                southDuplex(DataPoint(0.0, 0.0, 11.0), DataPoint(16.0, 16.0, 11.0), "#main"),
                westDuplex(DataPoint(5.0, 0.0, 0.0), DataPoint(5.0, 16.0, 16.0), "#main"),
                eastDuplex(DataPoint(11.0, 0.0, 0.0), DataPoint(11.0, 16.0, 16.0), "#main")
            )
        )
    }
    makeBlockModel("beanstalk_pipe_end") {
        DataModel(
            parent = "block/block",
            ambientOcclusion = false,
            textures = mapOf(
                "particle" to "miragefairy2019:blocks/beanstalk",
                "main" to "miragefairy2019:blocks/beanstalk",
                "top" to "miragefairy2019:blocks/beanstalk_top",
                "end" to "miragefairy2019:blocks/beanstalk_end"
            ),
            elements = listOf(
                upDuplex(DataPoint(0.0, 11.0, 0.0), DataPoint(16.0, 11.0, 16.0), "#end"),
                DataElement(
                    from = DataPoint(0.0, 0.0, 0.0),
                    to = DataPoint(16.0, 0.0, 16.0),
                    faces = DataFaces(
                        down = DataFace(texture = "#top")
                    )
                ),
                northDuplex(DataPoint(0.0, 0.0, 5.0), DataPoint(16.0, 11.0, 5.0), "#main"),
                southDuplex(DataPoint(0.0, 0.0, 11.0), DataPoint(16.0, 11.0, 11.0), "#main"),
                westDuplex(DataPoint(5.0, 0.0, 0.0), DataPoint(5.0, 11.0, 16.0), "#main"),
                eastDuplex(DataPoint(11.0, 0.0, 0.0), DataPoint(11.0, 11.0, 16.0), "#main")
            )
        )
    }
    makeBlockModel("beanstalk_pipe_elbow") {
        DataModel(
            parent = "block/block",
            ambientOcclusion = false,
            textures = mapOf(
                "particle" to "miragefairy2019:blocks/beanstalk",
                "elbow" to "miragefairy2019:blocks/beanstalk_elbow"
            ),
            elements = listOf(
                element(DataPoint(5.0, 5.0, 5.0), DataPoint(11.0, 11.0, 11.0), "#elbow")
            )
        )
    }
    makeBlockModel("beanstalk_pipe_down") {
        DataModel(
            parent = "block/block",
            ambientOcclusion = false,
            textures = mapOf(
                "particle" to "miragefairy2019:blocks/beanstalk",
                "side" to "miragefairy2019:blocks/beanstalk",
                "top" to "miragefairy2019:blocks/beanstalk_top"
            ),
            elements = listOf(
                northDuplex(DataPoint(0.0, 0.0, 5.0), DataPoint(16.0, 5.0, 5.0), "#side"),
                southDuplex(DataPoint(0.0, 0.0, 11.0), DataPoint(16.0, 5.0, 11.0), "#side"),
                westDuplex(DataPoint(5.0, 0.0, 0.0), DataPoint(5.0, 5.0, 16.0), "#side"),
                eastDuplex(DataPoint(11.0, 0.0, 0.0), DataPoint(11.0, 5.0, 16.0), "#side"),
                DataElement(
                    from = DataPoint(5.0, 0.0, 5.0),
                    to = DataPoint(11.0, 0.0, 11.0),
                    faces = DataFaces(
                        down = DataFace(texture = "#top")
                    )
                )
            )
        )
    }
    makeBlockModel("beanstalk_pipe_up") {
        DataModel(
            parent = "block/block",
            ambientOcclusion = false,
            textures = mapOf(
                "particle" to "miragefairy2019:blocks/beanstalk",
                "side" to "miragefairy2019:blocks/beanstalk",
                "top" to "miragefairy2019:blocks/beanstalk_top"
            ),
            elements = listOf(
                northDuplex(DataPoint(0.0, 11.0, 5.0), DataPoint(16.0, 16.0, 5.0), "#side"),
                southDuplex(DataPoint(0.0, 11.0, 11.0), DataPoint(16.0, 16.0, 11.0), "#side"),
                westDuplex(DataPoint(5.0, 11.0, 0.0), DataPoint(5.0, 16.0, 16.0), "#side"),
                eastDuplex(DataPoint(11.0, 11.0, 0.0), DataPoint(11.0, 16.0, 16.0), "#side"),
                DataElement(
                    from = DataPoint(5.0, 16.0, 5.0),
                    to = DataPoint(11.0, 16.0, 11.0),
                    faces = DataFaces(
                        up = DataFace(texture = "#top")
                    )
                )
            )
        )
    }

    // アイテム
    itemBlockBeanstalkPipe = item({ ItemBlock(blockBeanstalkPipe()) }, "beanstalk_pipe") {
        setCustomModelResourceLocation()
        makeItemModel { DataModel(parent = "miragefairy2019:block/beanstalk_pipe_straight") }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    " v ",
                    "vmv",
                    " v "
                ),
                key = mapOf(
                    "v" to DataOreIngredient(ore = "vine"),
                    "m" to DataOreIngredient(ore = "mirageFairyMandrake")
                ),
                result = DataResult(item = "miragefairy2019:beanstalk_pipe")
            )
        }
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.beanstalkPipe.name", "Beanstalk Pipe", "豆の木パイプ")
    }

}

class BlockBeanstalkPipe : BlockBeanstalk() {
    companion object {
        val DOWN: PropertyBool = PropertyBool.create("down")
        val UP: PropertyBool = PropertyBool.create("up")
        val NORTH: PropertyBool = PropertyBool.create("north")
        val SOUTH: PropertyBool = PropertyBool.create("south")
        val WEST: PropertyBool = PropertyBool.create("west")
        val EAST: PropertyBool = PropertyBool.create("east")
    }


    // Metadata

    init {
        defaultState = defaultState
            .withProperty(DOWN, false)
            .withProperty(UP, false)
            .withProperty(NORTH, false)
            .withProperty(SOUTH, false)
            .withProperty(WEST, false)
            .withProperty(EAST, false)
    }

    override fun createBlockState() = BlockStateContainer(this, FACING, DOWN, UP, NORTH, SOUTH, WEST, EAST)

    override fun getActualState(blockState: IBlockState, world: IBlockAccess, pos: BlockPos): IBlockState {
        fun IBlockState.a(property: PropertyBool, facing: EnumFacing): IBlockState {
            val neighborBlockState = world.getBlockState(pos.offset(facing))
            val neighborBlock = neighborBlockState.block as? BlockBeanstalk ?: return this
            val canConnect = neighborBlock.getFacing(neighborBlockState) == facing.opposite
            return if (canConnect) this.withProperty(property, true) else this
        }
        return blockState
            .a(DOWN, EnumFacing.DOWN)
            .a(UP, EnumFacing.UP)
            .a(NORTH, EnumFacing.NORTH)
            .a(SOUTH, EnumFacing.SOUTH)
            .a(WEST, EnumFacing.WEST)
            .a(EAST, EnumFacing.EAST)
    }

    fun isStraight(blockState: IBlockState): Boolean {
        val down = blockState[DOWN]
        val up = blockState[UP]
        val north = blockState[NORTH]
        val south = blockState[SOUTH]
        val west = blockState[WEST]
        val east = blockState[EAST]
        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        return when (getFacing(blockState)) {
            EnumFacing.DOWN -> !down && up && !north && !south && !west && !east
            EnumFacing.UP -> down && !up && !north && !south && !west && !east
            EnumFacing.NORTH -> !down && !up && !north && south && !west && !east
            EnumFacing.SOUTH -> !down && !up && north && !south && !west && !east
            EnumFacing.WEST -> !down && !up && !north && !south && !west && east
            EnumFacing.EAST -> !down && !up && !north && !south && west && !east
        }
    }

    fun isEnd(blockState: IBlockState) = !blockState[DOWN] && !blockState[UP] && !blockState[NORTH] && !blockState[SOUTH] && !blockState[WEST] && !blockState[EAST]

}
