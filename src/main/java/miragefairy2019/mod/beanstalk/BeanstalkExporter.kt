package miragefairy2019.mod.beanstalk

import miragefairy2019.lib.getCapabilityIfHas
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.faced
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import net.minecraft.item.ItemBlock
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

lateinit var blockBeanstalkExporter: () -> BlockBeanstalkExporter
lateinit var itemBlockBeanstalkExporter: () -> ItemBlock

val beanstalkExporterModule = module {

    // ブロック
    blockBeanstalkExporter = block({ BlockBeanstalkExporter() }, "beanstalk_exporter") {
        setUnlocalizedName("beanstalkExporter")
        setCreativeTab { Main.creativeTab }
        makeBlockStates { faced }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/beanstalk",
                    "side" to "miragefairy2019:blocks/beanstalk",
                    "top" to "miragefairy2019:blocks/beanstalk_top",
                    "elbow" to "miragefairy2019:blocks/beanstalk_elbow",
                    "stem" to "miragefairy2019:blocks/beanstalk_stem",
                    "flower" to "miragefairy2019:blocks/beanstalk_flower_input"
                ),
                elements = listOf(
                    down(DataPoint(5.0, 0.0, 5.0), DataPoint(11.0, 0.0, 11.0), "#top"),
                    northDuplex(DataPoint(0.0, 0.0, 5.0), DataPoint(16.0, 5.0, 5.0), "#side"),
                    southDuplex(DataPoint(0.0, 0.0, 11.0), DataPoint(16.0, 5.0, 11.0), "#side"),
                    westDuplex(DataPoint(5.0, 0.0, 0.0), DataPoint(5.0, 5.0, 16.0), "#side"),
                    eastDuplex(DataPoint(11.0, 0.0, 0.0), DataPoint(11.0, 5.0, 16.0), "#side"),
                    element(DataPoint(5.0, 5.0, 5.0), DataPoint(11.0, 11.0, 11.0), "#elbow"),

                    // 花
                    element(DataPoint(7.0, 11.0, 7.0), DataPoint(9.0, 12.0, 9.0), "#stem"), // 茎
                    downDuplex(DataPoint(6.0, 11.95, 6.0), DataPoint(10.0, 11.95, 10.0), "#stem"), // がく
                    element(DataPoint(6.0, 12.0, 6.0), DataPoint(10.0, 13.0, 10.0), "#flower"),
                    element(DataPoint(5.0, 13.0, 5.0), DataPoint(11.0, 15.0, 11.0), "#flower"),
                    element(DataPoint(6.0, 15.0, 6.0), DataPoint(10.0, 16.0, 10.0), "#flower")
                )
            )
        }
    }

    // アイテム
    itemBlockBeanstalkExporter = item({ ItemBlock(blockBeanstalkExporter()) }, "beanstalk_exporter") {
        setCustomModelResourceLocation()
        makeItemModel { block }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "1",
                    "#"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:beanstalk_pipe"),
                    "1" to DataOreIngredient(ore = "mirageFairy2019SphereLevitate")
                ),
                result = DataResult(item = "miragefairy2019:beanstalk_exporter")
            )
        }
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.beanstalkExporter.name", "Beanstalk Exporter", "豆の木エクスポーター")
    }

    // タイルエンティティ
    tileEntity("beanstalk_exporter", TileEntityBeanstalkExporter::class.java)


}

class BlockBeanstalkExporter : BlockBeanstalkFlower<TileEntityBeanstalkExporter>() {
    override fun validateTileEntity(tileEntity: TileEntity) = tileEntity as? TileEntityBeanstalkExporter
    override fun createNewTileEntity() = TileEntityBeanstalkExporter()
}

class TileEntityBeanstalkExporter : TileEntityBeanstalkFlower() {
    override fun doAction() {
        val src = getRoot(world, pos) ?: return // 豆の木が異常
        val dest = getEncounterBlockPos() ?: return // 花が異常

        val srcItemHandler = world.getTileEntity(src.blockPos)?.getCapabilityIfHas(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, src.facing) as? IItemHandlerModifiable ?: return // 元がコンテナでない
        val destItemHandler = world.getTileEntity(dest.blockPos)?.getCapabilityIfHas(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dest.facing) as? IItemHandlerModifiable ?: return // 先がコンテナでない

        move(9, srcItemHandler, destItemHandler)

        // TODO エフェクト
        // TODO 選別機能
    }
}
