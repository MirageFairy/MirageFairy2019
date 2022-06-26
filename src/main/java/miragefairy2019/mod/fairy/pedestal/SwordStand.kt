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
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.resourcemaker.normal
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.mod.placeditem.PlacedItem
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

val swordStandModule = module {
    val blockStoneSwordStand = block({ BlockSwordStand() }, "stone_sword_stand") {
        setUnlocalizedName("stoneSwordStand")
        setCreativeTab { Main.creativeTab }
        makeBlockStates { normal }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to "minecraft:blocks/stone",
                    "main" to "minecraft:blocks/stone"
                ),
                elements = listOf(
                    DataElement(
                        from = DataPoint(4.0, 0.0, 4.0),
                        to = DataPoint(12.0, 4.0, 12.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#main"),
                            up = DataFace(texture = "#main"),
                            north = DataFace(texture = "#main"),
                            south = DataFace(texture = "#main"),
                            west = DataFace(texture = "#main"),
                            east = DataFace(texture = "#main")
                        )
                    ),
                    DataElement(
                        from = DataPoint(6.0, 4.0, 6.0),
                        to = DataPoint(10.0, 6.0, 10.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#main"),
                            up = DataFace(texture = "#main"),
                            north = DataFace(texture = "#main"),
                            south = DataFace(texture = "#main"),
                            west = DataFace(texture = "#main"),
                            east = DataFace(texture = "#main")
                        )
                    )
                )
            )
        }
    }
    item({ ItemBlock(blockStoneSwordStand()) }, "stone_sword_stand") {
        setCustomModelResourceLocation()
        makeItemModel { block }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    " W ",
                    " # ",
                    "###"
                ),
                key = mapOf(
                    "#" to DataOreIngredient(ore = "stone"),
                    "W" to WandType.CRAFTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:stone_sword_stand")
            )
        }
    }
    onMakeLang { enJa("tile.stoneSwordStand.name", "Stone Sword Stand", "ガルガーノの岩") }
    tileEntity("stone_sword_stand", TileEntitySwordStand::class.java)
    tileEntityRenderer(TileEntitySwordStand::class.java) { TileEntityRendererPedestal() }
}

class BlockSwordStand : BlockPedestal<TileEntitySwordStand>(Material.CIRCUITS, { it as? TileEntitySwordStand }) {
    init {
        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(0.8f)
        setHarvestLevel("pickaxe", -1)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntitySwordStand()


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { (TextComponentKeybind(PlacedItem.keyBindingPlaceItem.keyDescription)() + "キーでアイテムを展示"()).red } // TODO translate
        tooltip += formattedText { ("右クリックで展示物を回転"()).red } // TODO translate
        tooltip += formattedText { ("Shift+右クリックで展示方法を変更"()).red } // TODO translate
    }


    // 当たり判定

    private val boundingBox = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 8 / 16.0, 14 / 16.0)
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = boundingBox
    private val collisionBoundingBox = AxisAlignedBB(4 / 16.0, 0 / 16.0, 4 / 16.0, 12 / 16.0, 6 / 16.0, 12 / 16.0)
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = collisionBoundingBox

}

class TileEntitySwordStand : TileEntityPedestal() {
    var rotation = 0.0
    var reversed = 0

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        nbt.setDouble("rotation", rotation)
        nbt.setInteger("reversed", reversed)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        rotation = nbt.getDouble("rotation")
        reversed = nbt.getInteger("reversed")
    }

    override fun transform(transformProxy: ITransformProxy) {
        transformProxy.translate(0.5, 9.0 / 16.0, 0.5)
        transformProxy.rotate((-rotation).toFloat(), 0f, 1f, 0f)
        when (reversed) {
            0 -> transformProxy.rotate(-45.0f + 180.0f, 0f, 0f, 1f)
            1 -> transformProxy.rotate(-45.0f + 0.0f, 0f, 0f, 1f)
            2 -> transformProxy.rotate(-45.0f + 90.0f, 0f, 0f, 1f)
            3 -> transformProxy.rotate(-45.0f + 270.0f, 0f, 0f, 1f)
            4 -> transformProxy.rotate(90.0f, 0f, 0f, 1f)
            5 -> transformProxy.rotate(0.0f, 0f, 0f, 1f)
            6 -> transformProxy.rotate(180.0f, 0f, 0f, 1f)
            7 -> transformProxy.rotate(270.0f, 0f, 0f, 1f)
            else -> Unit
        }
    }


    override fun onAdjust(player: EntityPlayer, placeExchanger: IPlaceExchanger): Boolean {
        if (super.onAdjust(player, placeExchanger)) return true

        // 角度の調整
        if (player.isSneaking) {
            reversed = (reversed + 1) % 8
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
        reversed = 0
    }
}
