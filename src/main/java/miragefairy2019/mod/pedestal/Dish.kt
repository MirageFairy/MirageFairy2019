package miragefairy2019.mod.pedestal

import miragefairy2019.api.IPlaceExchanger
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.modinitializer.tileEntityRenderer
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataUv
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.resourcemaker.normal
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.mod.placeditem.keyBindingPlaceItem
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentKeybind
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.round

val dishModule = module {
    val blockDish = block({ BlockDish() }, "dish") {
        setUnlocalizedName("dish")
        setCreativeTab { Main.creativeTab }
        makeBlockStates { normal }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "minecraft:blocks/quartz_block_top",
                    "top" to "minecraft:blocks/bone_block_top",
                    "main" to "minecraft:blocks/quartz_block_top"
                ),
                elements = listOf(
                    DataElement(
                        from = DataPoint(4.0, 1.0, 4.0),
                        to = DataPoint(12.0, 1.5, 12.0),
                        faces = DataFaces(
                            down = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            up = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#top"),
                            north = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            south = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            west = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            east = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main")
                        )
                    ),
                    DataElement(
                        from = DataPoint(6.0, 0.0, 6.0),
                        to = DataPoint(10.0, 1.0, 10.0),
                        faces = DataFaces(
                            down = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            up = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            north = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            south = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            west = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main"),
                            east = DataFace(uv = DataUv(0.0, 0.0, 16.0, 16.0), texture = "#main")
                        )
                    )
                )
            )
        }
    }
    item({ ItemBlock(blockDish()) }, "dish") {
        setCustomModelResourceLocation()
        makeItemModel { block }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    " p ",
                    "qqq"
                ),
                key = mapOf(
                    "q" to DataOreIngredient(ore = "gemQuartz"),
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:dish")
            )
        }
    }
    lang("tile.dish.name", "Dish", "皿")
    tileEntity("dish", TileEntityDish::class.java)
    tileEntityRenderer(TileEntityDish::class.java) { TileEntityRendererPedestal() }
}

class BlockDish : BlockPlacedPedestal<TileEntityDish>(Material.CIRCUITS, { it as? TileEntityDish }) {
    init {
        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(0.8f)
        setHarvestLevel("pickaxe", -1)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityDish()


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { (TextComponentKeybind(keyBindingPlaceItem.keyDescription)() + "キーでアイテムを展示"()).red } // TRANSLATE
        tooltip += formattedText { ("右クリックで展示物を回転"()).red } // TRANSLATE
        tooltip += formattedText { ("Shift+右クリックで展示方法を変更"()).red } // TRANSLATE
    }


    // 当たり判定

    private val boundingBox = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 2 / 16.0, 14 / 16.0)
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = boundingBox
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = NULL_AABB

}

class TileEntityDish : TileEntityPedestal() {
    var rotation = 0.0
    var standing = false

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        nbt.setDouble("rotation", rotation)
        nbt.setBoolean("standing", standing)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        rotation = nbt.getDouble("rotation")
        standing = nbt.getBoolean("standing")
    }

    override fun transform(transformProxy: ITransformProxy) {
        transformProxy.translate(0.5, 1.5 / 16.0 + 1 / 64.0, 0.5)
        transformProxy.rotate((-rotation).toFloat(), 0f, 1f, 0f)
        if (standing) {
            transformProxy.translate(0.0, 0.25, 0.0)
        } else {
            transformProxy.rotate(90f, 1f, 0f, 0f)
        }
    }


    override fun onAdjust(player: EntityPlayer, placeExchanger: IPlaceExchanger): Boolean {
        if (super.onAdjust(player, placeExchanger)) return true

        // 角度の調整
        if (player.isSneaking) {
            standing = !standing
        } else {
            rotation += 45.0
            if (rotation >= 360) {
                rotation -= 360.0
            }
        }

        return true
    }

    override fun onDeploy(player: EntityPlayer) {
        rotation = round(player.rotationYawHead.toDouble() / 45) * 45 // 角度調整
        standing = false
    }
}
