package miragefairy2019.mod.artifacts

import miragefairy2019.lib.EnumFireSpreadSpeed
import miragefairy2019.lib.EnumFlammability
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import net.minecraft.block.BlockRotatedPillar
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess

lateinit var blockFairyWoodLog: () -> BlockFairyWoodLog
lateinit var itemBlockFairyWoodLog: () -> ItemBlock

val fairyWoodLogModule = module {

    // ブロック
    blockFairyWoodLog = block({ BlockFairyWoodLog() }, "fairy_wood_log") {
        setUnlocalizedName("fairyWoodLog")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataBlockStates(
                variants = listOf("y" to Pair(null, null), "z" to Pair(90, null), "x" to Pair(90, 90)).associate { axis ->
                    "axis=${axis.first}" to DataBlockState("miragefairy2019:fairy_wood_log", x = axis.second.first, y = axis.second.second)
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
    itemBlockFairyWoodLog = item({ ItemBlock(blockFairyWoodLog()) }, "fairy_wood_log") {
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
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.fairyWoodLog.name", "Fairy Wood Log", "妖精の原木")
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
