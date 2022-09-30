package miragefairy2019.mod.artifacts

import miragefairy2019.lib.EnumFireSpreadSpeed
import miragefairy2019.lib.EnumFlammability
import miragefairy2019.lib.NeighborhoodType
import miragefairy2019.lib.TooLargeBehaviour
import miragefairy2019.lib.UnloadedPositionBehaviour
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.treeSearch
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.get
import miragefairy2019.libkt.with
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairyweapon.breakBlock
import mirrg.kotlin.hydrogen.toUnitOrNull
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.block.BlockLog
import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

lateinit var blockFairyWoodLog: () -> BlockFairyWoodLog
lateinit var itemBlockFairyWoodLog: () -> ItemBlockFairyWoodLog

val fairyWoodLogModule = module {

    // ブロック
    blockFairyWoodLog = block({ BlockFairyWoodLog() }, "fairy_wood_log") {
        setUnlocalizedName("fairyWoodLog")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataModelBlockDefinition(
                variants = listOf("y" to Pair(null, null), "z" to Pair(90, null), "x" to Pair(90, 90)).associate { axis ->
                    "axis=${axis.first}" to DataSingleVariantList(DataVariant("miragefairy2019:fairy_wood_log", x = axis.second.first, y = axis.second.second))
                }
            )
        }
        makeBlockModel {
            DataModel(
                parent = "block/cube_column",
                textures = mapOf(
                    "end" to "miragefairy2019:blocks/fairy_wood_log_top",
                    "side" to "miragefairy2019:blocks/fairy_wood_log"
                )
            )
        }
    }

    // アイテム
    itemBlockFairyWoodLog = item({ ItemBlockFairyWoodLog(blockFairyWoodLog()) }, "fairy_wood_log") {
        setUnlocalizedName("fairyWoodLog")
        addOreName("logFairyWood")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation(variant = "axis=y")
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "ooo",
                    "oLo",
                    "ooo"
                ),
                key = mapOf(
                    "L" to DataOreIngredient(ore = "logWood"),
                    "o" to DataOreIngredient(ore = "container1000MirageFlowerOil")
                ),
                result = DataResult(item = "miragefairy2019:fairy_wood_log")
            )
        }
        makeRecipe("fairy_wood_log_from_mirage_fairy_varnish") {
            DataShapedRecipe(
                pattern = listOf(
                    " o ",
                    "oLo",
                    " o "
                ),
                key = mapOf(
                    "L" to DataOreIngredient(ore = "logWood"),
                    "o" to DataOreIngredient(ore = "container250MirageFairyVarnish")
                ),
                result = DataResult(item = "miragefairy2019:fairy_wood_log")
            )
        }
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.fairyWoodLog.name", "Fairy Wood Log", "妖精の原木")
    }

}

class ItemBlockFairyWoodLog(block: BlockFairyWoodLog) : ItemBlock(block) {
    private fun isLog(world: World, blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLog -> true
        blockFairyLog() -> true
        else -> false
    }

    @SideOnly(Side.CLIENT)
    override fun canPlaceBlockOnSide(world: World, blockPos: BlockPos, facing: EnumFacing, player: EntityPlayer, itemStack: ItemStack): Boolean {
        return if (isLog(world, blockPos)) {
            true
        } else {
            super.canPlaceBlockOnSide(world, blockPos, facing, player, itemStack)
        }
    }

    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStack = player.getHeldItem(hand)

        // 木の探索
        val logBlockPosList = treeSearch(
            world,
            listOf(blockPos),
            mutableSetOf(),
            maxDistance = 32,
            maxSize = 2000,
            includeZero = true,
            neighborhoodType = NeighborhoodType.VERTEX,
            tooLargeBehaviour = TooLargeBehaviour.IGNORE,
            unloadedPositionBehaviour = UnloadedPositionBehaviour.LOAD
        ) { it, _ -> isLog(world, it).toUnitOrNull() }
        if (logBlockPosList.isEmpty()) return super.onItemUse(player, world, blockPos, hand, facing, hitX, hitY, hitZ)

        // 対象ブロック
        val targetBlockPos = logBlockPosList.last().blockPos
        val targetBlockState = world.getBlockState(targetBlockPos)

        // 軸方向取得
        val axis = when {
            BlockRotatedPillar.AXIS in targetBlockState.properties -> targetBlockState[BlockRotatedPillar.AXIS]
            BlockLog.LOG_AXIS in targetBlockState.properties -> EnumFacing.Axis.valueOf(targetBlockState[BlockLog.LOG_AXIS].name)
            else -> EnumFacing.Axis.Y
        }

        // チェック
        if (itemStack.isEmpty) return EnumActionResult.FAIL
        if (!player.canPlayerEdit(targetBlockPos, facing, itemStack)) return EnumActionResult.FAIL

        // 破壊
        if (!breakBlock(world, player, itemStack, targetBlockPos, facing = facing, collection = true)) return EnumActionResult.FAIL

        // チェック
        if (!world.mayPlace(block, targetBlockPos, false, facing, player)) return EnumActionResult.FAIL

        // 設置
        if (!world.setBlockState(targetBlockPos, block.defaultState.with(BlockRotatedPillar.AXIS, axis), 11)) return EnumActionResult.FAIL

        // 設置後処理
        val newBlockState = world.getBlockState(targetBlockPos)
        if (newBlockState.block === block) {
            setTileEntityNBT(world, player, targetBlockPos, itemStack)
            //block.onBlockPlacedBy(world, targetBlockPos, newBlockState, player, itemStack)
            if (player is EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger(player, targetBlockPos, itemStack)
        }

        // エフェクト
        val soundType = newBlockState.block.getSoundType(newBlockState, world, targetBlockPos, player)
        world.playSound(player, targetBlockPos, soundType.placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0f) / 2.0f, soundType.getPitch() * 0.8f)

        // 消費
        itemStack.shrink(1)
        //player.cooldownTracker.setCooldown(this, 5)

        return EnumActionResult.SUCCESS
    }
}

class BlockFairyWoodLog : BlockRotatedPillar(Material.WOOD) {
    init {

        // style
        soundType = SoundType.WOOD

        // 挙動
        setHardness(2.0f)
        setHarvestLevel("axe", 0)

    }

    override fun getFlammability(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = EnumFlammability.VERY_SLOW.value
    override fun getFireSpreadSpeed(world: IBlockAccess, pos: BlockPos, face: EnumFacing) = EnumFireSpreadSpeed.VERY_SLOW.value

    override fun canSustainLeaves(state: IBlockState, world: IBlockAccess, pos: BlockPos) = true
}
