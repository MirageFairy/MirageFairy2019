package miragefairy2019.mod.beanstalk

import miragefairy2019.common.ResourceName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setStateMapper
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataSelector
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.get
import miragefairy2019.libkt.union
import miragefairy2019.mod.Main
import mirrg.kotlin.gson.hydrogen.jsonElement
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMapperBase
import net.minecraft.entity.Entity
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

lateinit var blockBeanstalkPipe: () -> BlockBeanstalkPipe
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
                    if (block.isInvalid(blockState)) return model("beanstalk_pipe_invalid", "facing=${block.getFacing(blockState)}")
                    if (block.isStraight(blockState)) return model("beanstalk_pipe_straight", "facing=${block.getFacing(blockState)}")
                    if (block.isEnd(blockState)) return model("beanstalk_pipe_end", "facing=${block.getFacing(blockState)}")
                    return model("beanstalk_pipe", getPropertyString(blockState.properties))
                }
            }
        }
    }

    // ブロックステート生成
    makeBlockStates("beanstalk_pipe_straight") {
        DataModelBlockDefinition(
            variants = Facing.values().associate { facing ->
                "facing=$facing" to DataSingleVariantList(DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_straight"), x = facing.x, y = facing.y))
            }
        )
    }
    makeBlockStates("beanstalk_pipe_end") {
        DataModelBlockDefinition(
            variants = Facing.values().associate { facing ->
                "facing=$facing" to DataSingleVariantList(DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_end"), x = facing.x, y = facing.y))
            }
        )
    }
    makeBlockStates("beanstalk_pipe") {
        DataModelBlockDefinition(
            multipart = listOf(
                DataSelector(
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_elbow"))
                ),
                DataSelector(
                    `when` = mapOf("facing" to "down".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.DOWN.x, y = Facing.DOWN.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "up".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.UP.x, y = Facing.UP.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "north".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.NORTH.x, y = Facing.NORTH.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "south".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.SOUTH.x, y = Facing.SOUTH.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "west".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.WEST.x, y = Facing.WEST.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "east".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down"), x = Facing.EAST.x, y = Facing.EAST.y)
                ),
                DataSelector(
                    `when` = mapOf("down" to true.jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.DOWN.opposite.x, y = Facing.DOWN.opposite.y)
                ),
                DataSelector(
                    `when` = mapOf("up" to true.jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.UP.opposite.x, y = Facing.UP.opposite.y)
                ),
                DataSelector(
                    `when` = mapOf("north" to true.jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.NORTH.opposite.x, y = Facing.NORTH.opposite.y)
                ),
                DataSelector(
                    `when` = mapOf("south" to true.jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.SOUTH.opposite.x, y = Facing.SOUTH.opposite.y)
                ),
                DataSelector(
                    `when` = mapOf("west" to true.jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.WEST.opposite.x, y = Facing.WEST.opposite.y)
                ),
                DataSelector(
                    `when` = mapOf("east" to true.jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_up"), x = Facing.EAST.opposite.x, y = Facing.EAST.opposite.y)
                )
            )
        )
    }
    makeBlockStates("beanstalk_pipe_invalid") {
        DataModelBlockDefinition(
            multipart = listOf(
                DataSelector(
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_elbow"))
                ),
                DataSelector(
                    `when` = mapOf("facing" to "down".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down_invalid"), x = Facing.DOWN.x, y = Facing.DOWN.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "up".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down_invalid"), x = Facing.UP.x, y = Facing.UP.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "north".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down_invalid"), x = Facing.NORTH.x, y = Facing.NORTH.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "south".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down_invalid"), x = Facing.SOUTH.x, y = Facing.SOUTH.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "west".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down_invalid"), x = Facing.WEST.x, y = Facing.WEST.y)
                ),
                DataSelector(
                    `when` = mapOf("facing" to "east".jsonElement),
                    apply = DataVariant(model = ResourceName("miragefairy2019", "beanstalk_pipe_down_invalid"), x = Facing.EAST.x, y = Facing.EAST.y)
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
    makeBlockModel("beanstalk_pipe_down_invalid") {
        DataModel(
            parent = "block/block",
            ambientOcclusion = false,
            textures = mapOf(
                "particle" to "miragefairy2019:blocks/beanstalk",
                "side" to "miragefairy2019:blocks/beanstalk_invalid",
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
    lang("tile.beanstalkPipe.name", "Beanstalk Pipe", "豆の木パイプ")

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

    override fun getActualState(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos): IBlockState {
        fun a(facing: EnumFacing): Boolean {
            val neighborBlockState = world.getBlockState(blockPos.offset(facing))
            val neighborBlock = neighborBlockState.block as? IBeanstalkBlock ?: return false
            return neighborBlock.getFacing(neighborBlockState, world, blockPos) == facing.opposite
        }
        return blockState
            .withProperty(DOWN, a(EnumFacing.DOWN))
            .withProperty(UP, a(EnumFacing.UP))
            .withProperty(NORTH, a(EnumFacing.NORTH))
            .withProperty(SOUTH, a(EnumFacing.SOUTH))
            .withProperty(WEST, a(EnumFacing.WEST))
            .withProperty(EAST, a(EnumFacing.EAST))
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

    fun isInvalid(blockState: IBlockState) = when (getFacing(blockState)) {
        EnumFacing.DOWN -> blockState[DOWN]
        EnumFacing.UP -> blockState[UP]
        EnumFacing.NORTH -> blockState[NORTH]
        EnumFacing.SOUTH -> blockState[SOUTH]
        EnumFacing.WEST -> blockState[WEST]
        EnumFacing.EAST -> blockState[EAST]
    }


    // Box

    private val collisionBoundingBoxElbow = AxisAlignedBB(5 / 16.0, 5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxDown = AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxUp = AxisAlignedBB(5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0, 16 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxNorth = AxisAlignedBB(5 / 16.0, 5 / 16.0, 0 / 16.0, 11 / 16.0, 11 / 16.0, 5 / 16.0)
    private val collisionBoundingBoxSouth = AxisAlignedBB(5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0, 11 / 16.0, 16 / 16.0)
    private val collisionBoundingBoxWest = AxisAlignedBB(0 / 16.0, 5 / 16.0, 5 / 16.0, 5 / 16.0, 11 / 16.0, 11 / 16.0)
    private val collisionBoundingBoxEast = AxisAlignedBB(11 / 16.0, 5 / 16.0, 5 / 16.0, 16 / 16.0, 11 / 16.0, 11 / 16.0)
    private fun getBoxes(actualBlockState: IBlockState): List<AxisAlignedBB> {
        val list = mutableListOf<AxisAlignedBB>()
        list += collisionBoundingBoxElbow
        val facing = getFacing(actualBlockState)
        if (facing == EnumFacing.DOWN || actualBlockState[DOWN]) list += collisionBoundingBoxDown
        if (facing == EnumFacing.UP || actualBlockState[UP]) list += collisionBoundingBoxUp
        if (facing == EnumFacing.NORTH || actualBlockState[NORTH]) list += collisionBoundingBoxNorth
        if (facing == EnumFacing.SOUTH || actualBlockState[SOUTH]) list += collisionBoundingBoxSouth
        if (facing == EnumFacing.WEST || actualBlockState[WEST]) list += collisionBoundingBoxWest
        if (facing == EnumFacing.EAST || actualBlockState[EAST]) list += collisionBoundingBoxEast
        return list
    }

    override fun getBoundingBox(blockState: IBlockState, world: IBlockAccess, blockPos: BlockPos): AxisAlignedBB {
        if (world !is World) return FULL_BLOCK_AABB
        return getBoxes(blockState.getActualState(world, blockPos)).union() ?: FULL_BLOCK_AABB
    }

    override fun addCollisionBoxToList(blockState: IBlockState, world: World, blockPos: BlockPos, entityBox: AxisAlignedBB, collidingBoxes: MutableList<AxisAlignedBB>, entity: Entity?, isActualState: Boolean) {
        val actualBlockState = if (isActualState) blockState else blockState.getActualState(world, blockPos)
        getBoxes(actualBlockState).forEach {
            addCollisionBoxToList(blockPos, entityBox, collidingBoxes, it)
        }
    }

    override fun collisionRayTrace(blockState: IBlockState, world: World, blockPos: BlockPos, start: Vec3d, end: Vec3d): RayTraceResult? {
        val collisionBoxes = getBoxes(blockState.getActualState(world, blockPos))
        val rayTraceResultAndDistanceSqList = collisionBoxes.mapNotNull {
            val rayTraceResult = rayTrace(blockPos, start, end, it) ?: return@mapNotNull null
            Pair(rayTraceResult, rayTraceResult.hitVec.squareDistanceTo(start))
        }
        val (rayTraceResult, _) = rayTraceResultAndDistanceSqList.minBy { it.second } ?: return null
        return rayTraceResult
    }

}
