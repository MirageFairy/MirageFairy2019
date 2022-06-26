package miragefairy2019.mod.fairy.pedestal

import miragefairy2019.api.IMortarRecipe
import miragefairy2019.api.IPlaceExchanger
import miragefairy2019.api.MortarRecipeRegistry
import miragefairy2019.common.toOreName
import miragefairy2019.lib.MortarRecipe
import miragefairy2019.lib.getMortarRecipe
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
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.block
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.white
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.placeditem.PlacedItem
import mirrg.kotlin.hydrogen.castOrNull
import mirrg.kotlin.hydrogen.formatAs
import mirrg.kotlin.hydrogen.toUpperCamelCase
import mirrg.kotlin.hydrogen.unit
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.IStringSerializable
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentKeybind
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

enum class MortarVariant(
    val registryName: String,
    val unlocalizedName: String,
    val englishName: String,
    val japaneseName: String,
    val texture: String,
    val bodyOreName: String,
    val handleOreName: String,
    val tier: Int,
    val level: Int,
    val successRate: Double
) : IStringSerializable {
    STONE(
        "stone", "stone", "Stone", "石",
        "minecraft:blocks/stone_diorite_smooth", "stone", "mirageFairyCrystal",
        0, 1, 0.03
    ),
    IRON(
        "iron", "iron", "Iron", "鉄",
        "minecraft:blocks/anvil_base", "ingotIron", "rodMiragium",
        2, 2, 0.1
    ),
    QUARTZ(
        "quartz", "quartz", "Nether Quartz", "ネザークォーツ",
        "minecraft:blocks/quartz_block_bottom", "gemQuartz", "mirageFairy2019ManaRodQuartz",
        3, 3, 0.3
    ),
    DIAMOND(
        "diamond", "diamond", "Diamond", "ダイヤモンド",
        "minecraft:blocks/diamond_block", "gemDiamond", "rodMirageFairyPlastic",
        5, 4, 1.0
    ),
    ;

    companion object {
        operator fun get(metadata: Int) = values()[metadata]
    }

    val metadata get() = ordinal
    override fun getName() = registryName
    val blockRegistryName get() = "${registryName}_mortar"
    val blockUnlocalizedName get() = "${unlocalizedName}Mortar"
}


lateinit var blockMortar: () -> BlockMortar
lateinit var itemBlockMortar: () -> ItemBlockMortar

val mortarModule = module {

    // 全般
    run {

        // ブロック登録
        blockMortar = block({ BlockMortar() }, "mortar") {
            setUnlocalizedName("mortar")
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = MortarVariant.values().associate { variant ->
                        "variant=${variant.registryName}" to DataBlockState(model = "miragefairy2019:${variant.blockRegistryName}")
                    }
                )
            }
        }

        // アイテム登録
        itemBlockMortar = item({ ItemBlockMortar(blockMortar()) }, "mortar") {
            MortarVariant.values().forEach { variant ->
                setCustomModelResourceLocation(variant.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, variant.blockRegistryName))
            }
        }

        // タイルエンティティ登録
        tileEntity("mortar", TileEntityMortar::class.java)
        tileEntityRenderer(TileEntityMortar::class.java) { TileEntityRendererPedestal() }

    }

    // 種類ごと
    MortarVariant.values().forEach { variant ->

        // ブロックモデル生成
        makeBlockModel(variant.blockRegistryName) {
            val faces = DataFaces(
                down = DataFace(texture = "#main"),
                up = DataFace(texture = "#main"),
                north = DataFace(texture = "#main"),
                south = DataFace(texture = "#main"),
                west = DataFace(texture = "#main"),
                east = DataFace(texture = "#main")
            )
            DataModel(
                parent = "block/block",
                ambientOcclusion = false,
                textures = mapOf(
                    "particle" to variant.texture,
                    "main" to variant.texture
                ),
                elements = listOf(
                    DataElement( // 底面
                        from = DataPoint(2.0, 0.0, 2.0),
                        to = DataPoint(14.0, 2.0, 14.0),
                        faces = faces
                    ),
                    DataElement(
                        from = DataPoint(2.0, 0.0, 2.0),
                        to = DataPoint(4.0, 4.0, 14.0),
                        faces = faces
                    ),
                    DataElement(
                        from = DataPoint(2.0, 0.0, 2.0),
                        to = DataPoint(14.0, 4.0, 4.0),
                        faces = faces
                    ),
                    DataElement(
                        from = DataPoint(12.0, 0.0, 2.0),
                        to = DataPoint(14.0, 4.0, 14.0),
                        faces = faces
                    ),
                    DataElement(
                        from = DataPoint(2.0, 0.0, 12.0),
                        to = DataPoint(14.0, 4.0, 14.0),
                        faces = faces
                    )
                )
            )
        }

        // アイテムモデル生成
        makeItemModel(variant.blockRegistryName) { block }

        // レシピ生成
        makeRecipe(variant.blockRegistryName) {
            DataShapedRecipe(
                pattern = listOf(
                    " G ",
                    "#G#",
                    "###"
                ),
                key = mapOf(
                    "#" to DataOreIngredient(ore = variant.bodyOreName),
                    "G" to DataOreIngredient(ore = variant.handleOreName)
                ),
                result = DataResult(
                    item = "miragefairy2019:mortar",
                    data = variant.metadata
                )
            )
        }

        // 翻訳生成
        onMakeLang { enJa("tile.${variant.blockUnlocalizedName}.name", "${variant.englishName} Mortar", "${variant.japaneseName}の乳鉢") }

    }

    // TODO move -> recipe
    // 対応レシピ登録
    onAddRecipe {
        fun r(recipe: IMortarRecipe) = unit { MortarRecipeRegistry.mortarRecipes += recipe }
        fun r(level: Int, inputOreName: String, outputOreName: String) = r(MortarRecipe(level, inputOreName.oreIngredient) { outputOreName.toOreName().copyItemStack()!! })
        fun r(level: Int, materialName: String) = r(level, "gem${materialName.toUpperCamelCase()}", "dust${materialName.toUpperCamelCase()}")

        r(1, "coal")
        r(1, "charcoal")
        r(4, "diamond")
        r(2, "lapis")
        r(3, "emerald")
        r(2, "quartz")
        r(1, "enderpearl", "dustEnderPearl")

        r(1, "apatite")
        r(1, "fluorite")
        r(1, "sulfur")
        r(1, "cinnabar")
        r(2, "moonstone")
        r(1, "magnetite")

        r(1, "saltpeter")
        r(3, "pyrope")
        r(1, "smithsonite")
        r(2, "nephrite")
        r(4, "topaz")
        r(3, "tourmaline")
        r(2, "heliolite")
        r(2, "labradorite")
        r(3, "peridot")
        r(4, "ruby")
        r(4, "sapphire")

        r(2, "crystalCertusQuartz", "dustCertusQuartz")
        r(2, "crystalFluix", "dustFluix")

    }

}

class BlockMortar : BlockPedestal<TileEntityMortar>(Material.CIRCUITS, { it as? TileEntityMortar }) {
    companion object {
        val VARIANT: PropertyEnum<MortarVariant> = PropertyEnum.create("variant", MortarVariant::class.java)
    }

    init {
        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(0.8f)
        setHarvestLevel("pickaxe", -1)

        defaultState = blockState.baseState.withProperty(VARIANT, MortarVariant.STONE)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileEntityMortar()


    fun getBlockState(variant: MortarVariant): IBlockState = defaultState.withProperty(VARIANT, variant)
    fun getVariant(blockState: IBlockState): MortarVariant = blockState.getValue(VARIANT)
    fun getVariant(metadata: Int) = MortarVariant[metadata]
    override fun createBlockState() = BlockStateContainer(this, VARIANT)
    override fun getStateFromMeta(metadata: Int) = getBlockState(getVariant(metadata))
    override fun getMetaFromState(blockState: IBlockState) = getVariant(blockState).metadata
    override fun getSubBlocks(creativeTab: CreativeTabs, items: NonNullList<ItemStack>) {
        MortarVariant.values().forEach { variant ->
            items += createItemStack(metadata = variant.metadata)
        }
    }


    override fun damageDropped(blockState: IBlockState) = getMetaFromState(blockState)
    fun getItem(blockState: IBlockState) = createItemStack(metadata = damageDropped(blockState))
    override fun getItem(world: World, blockPos: BlockPos, blockState: IBlockState) = getItem(blockState)
    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, blockPos: BlockPos, blockState: IBlockState, fortune: Int) {
        drops += getItem(blockState)
    }


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip += formattedText { "Tier ${getVariant(itemStack.metadata).tier}"().aqua } // TODO translate
        tooltip += formattedText { (TextComponentKeybind(PlacedItem.keyBindingPlaceItem.keyDescription)() + "キーでアイテムを展示"()).red } // TODO translate
        tooltip += formattedText { ("右クリックで粉砕"()).red } // TODO translate
        tooltip += formattedText { ("成功率: "() + (getVariant(itemStack.metadata).successRate * 100 formatAs "%.0f%%")().white).blue } // TODO translate
    }


    // 当たり判定

    private val boundingBox = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 4 / 16.0, 14 / 16.0)
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos) = boundingBox
    private val collisionBoundingBox = AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 4 / 16.0, 14 / 16.0)
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos) = collisionBoundingBox

}

class ItemBlockMortar(private val block2: BlockMortar) : ItemBlock(block2) {
    init {
        maxDamage = 0
        setHasSubtypes(true)
    }

    override fun getMetadata(metadata: Int) = metadata
    override fun getUnlocalizedName(itemStack: ItemStack) = "tile.${block2.getVariant(itemStack.metadata).blockUnlocalizedName}"
}

class TileEntityMortar : TileEntityPedestal() {
    val variant: MortarVariant
        get() {
            val blockState = world.getBlockState(pos)
            return blockState.block.castOrNull<BlockMortar>()?.getVariant(blockState) ?: MortarVariant.STONE
        }

    var progress = 0

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        nbt.setInteger("progress", progress)
        return nbt
    }

    override fun readFromNBT(nbt: NBTTagCompound) {
        progress = nbt.getInteger("progress")
        super.readFromNBT(nbt)
    }

    override fun transform(transformProxy: ITransformProxy) {
        transformProxy.translate(0.5, 2.0 / 16.0 + 1 / 64.0, 0.5)
        transformProxy.rotate(90f, 1f, 0f, 0f)
    }

    override fun onAdjust(player: EntityPlayer, placeExchanger: IPlaceExchanger): Boolean {

        // 加工が完了している場合、アイテムを取り除きつつ状態をリセットする
        if (progress >= 1) {
            progress = 0
            if (itemStackOrNull != null) {
                placeExchanger.harvest(itemStackOrNull!!)
                itemStackOrNull = null
            }
            return true
        }

        // ※必ず加工が完了していない

        // レシピ判定
        val recipe = getMortarRecipe(variant.level, itemStackOrNull)

        // アイテムが加工対象である場合、アイテムを加工する
        if (recipe != null) {

            // 加工完了判定
            val finished = world.rand.nextDouble() < variant.successRate

            // パーティクル
            repeat(10) {
                world.castOrNull<WorldServer>()?.spawnParticle(
                    EnumParticleTypes.ITEM_CRACK,
                    pos.x + 0.5 + (world.rand.nextDouble() - 0.5) * 0.3,
                    pos.y + 0.2 + (world.rand.nextDouble() - 0.5) * 0.3,
                    pos.z + 0.5 + (world.rand.nextDouble() - 0.5) * 0.3,
                    0,
                    world.rand.nextGaussian() * 0.03,
                    0.1 + world.rand.nextGaussian() * 0.03,
                    world.rand.nextGaussian() * 0.03,
                    1.0,
                    Item.getIdFromItem(itemStackOrNull!!.item), itemStackOrNull!!.metadata
                )
            }

            // SE
            world.playSound(
                null,
                pos,
                if (finished) SoundEvents.BLOCK_STONE_BREAK else SoundEvents.BLOCK_STONE_PLACE,
                SoundCategory.PLAYERS,
                0.2f,
                ((Math.random() - Math.random()) * 1.4 + 2.0).toFloat()
            )

            // 加工完了した場合、状態を加工完了済みにしてアイテムをセット
            if (finished) {
                progress = 1
                itemStackOrNull = recipe.getOutput()
            }

            return true
        }

        // ※必ず加工が完了しておらず、アイテムが加工対象でない

        // アイテムがある場合、回収する
        if (itemStackOrNull != null) {
            placeExchanger.harvest(itemStackOrNull!!)
            itemStackOrNull = null
            return true
        }

        // ※必ず加工が完了しておらず、アイテムがない

        return super.onAdjust(player, placeExchanger)
    }
}
