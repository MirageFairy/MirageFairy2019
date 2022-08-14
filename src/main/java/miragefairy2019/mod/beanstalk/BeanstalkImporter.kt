package miragefairy2019.mod.beanstalk

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

lateinit var blockBeanstalkImporter: () -> BlockBeanstalkImporter
lateinit var itemBlockBeanstalkImporter: () -> ItemBlock

val beanstalkImporterModule = module {

    // ブロック
    blockBeanstalkImporter = block({ BlockBeanstalkImporter() }, "beanstalk_importer") {
        setUnlocalizedName("beanstalkImporter")
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
                    "flower" to "minecraft:blocks/wool_colored_orange" // TODO
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
                    element(DataPoint(7.0, 12.0, 7.0), DataPoint(9.0, 13.0, 9.0), "#flower"),
                    element(DataPoint(6.0, 13.0, 6.0), DataPoint(10.0, 14.0, 10.0), "#flower"),
                    element(DataPoint(5.0, 14.0, 5.0), DataPoint(11.0, 15.0, 11.0), "#flower"),
                    element(DataPoint(4.0, 15.0, 4.0), DataPoint(12.0, 16.0, 12.0), "#flower")
                )
            )
        }
    }

    // アイテム
    itemBlockBeanstalkImporter = item({ ItemBlock(blockBeanstalkImporter()) }, "beanstalk_importer") {
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
                    "1" to DataOreIngredient(ore = "mirageFairy2019SphereShoot")
                ),
                result = DataResult(item = "miragefairy2019:beanstalk_importer")
            )
        }
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.beanstalkImporter.name", "Beanstalk Importer", "豆の木インポーター")
    }

    // タイルエンティティ
    tileEntity("beanstalk_importer", TileEntityBeanstalkImporter::class.java)

}

class BlockBeanstalkImporter : BlockBeanstalkFlower<TileEntityBeanstalkImporter>() {
    override fun validateTileEntity(tileEntity: TileEntity) = tileEntity as? TileEntityBeanstalkImporter
    override fun createNewTileEntity() = TileEntityBeanstalkImporter()
}

class TileEntityBeanstalkImporter : TileEntityBeanstalkFlower() {
    override fun onUpdateTick() {
        val rootBlockPos = getRootBlockPos(world, pos) ?: return
        val encounterBlockPos = getEncounterBlockPos() ?: return

        // TODO
        println("$rootBlockPos ${world.getBlockState(rootBlockPos)} ${world.getTileEntity(rootBlockPos)?.javaClass}")
        println("$pos ${world.getBlockState(pos)} ${world.getTileEntity(pos)?.javaClass}")
        println("$encounterBlockPos ${world.getBlockState(encounterBlockPos)} ${world.getTileEntity(encounterBlockPos)?.javaClass}")
    }
}
