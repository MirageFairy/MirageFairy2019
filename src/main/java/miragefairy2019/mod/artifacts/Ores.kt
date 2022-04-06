package miragefairy2019.mod.artifacts

import miragefairy2019.libkt.BlockMulti
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.IBlockVariant
import miragefairy2019.libkt.ItemBlockMulti
import miragefairy2019.libkt.block
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.getItemStack
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.IStringSerializable
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random

object Ores {

    lateinit var blockOre1: () -> BlockOre<EnumVariantOre1>
    lateinit var itemBlockOre1: () -> ItemBlockOre<EnumVariantOre1>

    lateinit var blockOre2: () -> BlockOre<EnumVariantOre2>
    lateinit var itemBlockOre2: () -> ItemBlockOre<EnumVariantOre2>

    val module = module {

        // 鉱石ブロック1
        blockOre1 = block({ BlockOre(EnumVariantOre1.variantList) }, "ore1") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf(
                        "miragefairy2019:apatite_ore",
                        "miragefairy2019:fluorite_ore",
                        "miragefairy2019:sulfur_ore",
                        "miragefairy2019:cinnabar_ore",
                        "miragefairy2019:moonstone_ore",
                        "miragefairy2019:magnetite_ore",
                        "miragefairy2019:pyrope_ore",
                        "miragefairy2019:smithsonite_ore",
                        "miragefairy2019:netherrack_apatite_ore",
                        "miragefairy2019:netherrack_fluorite_ore",
                        "miragefairy2019:netherrack_sulfur_ore",
                        "miragefairy2019:netherrack_cinnabar_ore",
                        "miragefairy2019:netherrack_moonstone_ore",
                        "miragefairy2019:netherrack_magnetite_ore",
                        "miragefairy2019:nephrite_ore",
                        "miragefairy2019:topaz_ore"
                    ).mapIndexed { i, model -> "variant=$i" to DataBlockState(model = model) }.toMap()
                )
            }
        }
        itemBlockOre1 = item({ ItemBlockOre(blockOre1()) }, "ore1") {
            onRegisterItem {
                blockOre1().variantList.blockVariants.forEach {
                    item.setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                }
            }
            // TODO register ore name
        }
        onMakeLang {
            enJa("tile.oreApatite.name", "Apatite Ore", "燐灰石鉱石")
            enJa("tile.oreFluorite.name", "Fluorite Ore", "蛍石鉱石")
            enJa("tile.oreSulfur.name", "Sulfur Ore", "硫黄鉱石")
            enJa("tile.oreCinnabar.name", "Cinnabar Ore", "辰砂鉱石")
            enJa("tile.oreMoonstone.name", "Moonstone Ore", "月長石鉱石")
            enJa("tile.oreMagnetite.name", "Magnetite Ore", "磁鉄鉱鉱石")
            enJa("tile.orePyrope.name", "Pyrope Ore", "パイロープ鉱石")
            enJa("tile.oreSmithsonite.name", "Smithsonite Ore", "スミソナイト鉱石")
            enJa("tile.oreNephrite.name", "Nephrite Ore", "ネフライト鉱石")
            enJa("tile.oreTopaz.name", "Topaz Ore", "トパーズ鉱石")
        }

        // 鉱石ブロック2
        blockOre2 = block({ BlockOre(EnumVariantOre2.variantList) }, "ore2") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf(
                        "miragefairy2019:tourmaline_ore",
                        "miragefairy2019:heliolite_ore",
                        "miragefairy2019:end_stone_labradorite_ore",
                        "miragefairy2019:pyrite_ore",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone"
                    ).mapIndexed { i, model -> "variant=$i" to DataBlockState(model = model) }.toMap()
                )
            }
        }
        itemBlockOre2 = item({ ItemBlockOre(blockOre2()) }, "ore2") {
            onRegisterItem {
                blockOre2().variantList.blockVariants.forEach {
                    item.setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                }
            }
            // TODO register ore name
        }
        onMakeLang {
            enJa("tile.oreTourmaline.name", "Tourmaline Ore", "トルマリン鉱石")
            enJa("tile.oreHeliolite.name", "Heliolite Ore", "ヘリオライト鉱石")
            enJa("tile.oreLabradorite.name", "Labradorite Ore", "ラブラドライト鉱石")
            enJa("tile.orePyrite.name", "Pyrite Ore", "パイライト鉱石")
        }

    }
}


class BlockOre<V : IBlockVariantOre>(variantList: BlockVariantList<V>) : BlockMulti<V>(Material.ROCK, variantList) {
    init {

        // style
        soundType = SoundType.STONE

        // 挙動
        setHardness(3.0f)
        setResistance(5.0f)
        variantList.blockVariants.forEach { setHarvestLevel(it.harvestTool, it.harvestLevel, getState(it)) }

    }


    override fun getBlockHardness(blockState: IBlockState, worldIn: World, pos: BlockPos) = getVariant(blockState).hardness
    override fun getExplosionResistance(world: World, pos: BlockPos, exploder: Entity?, explosion: Explosion) = getVariant(world.getBlockState(pos)).resistance


    override fun getDrops(drops: NonNullList<ItemStack>, world: IBlockAccess, blockPos: BlockPos, blockState: IBlockState, fortune: Int) {
        val random = if (world is World) world.rand else RANDOM
        drops += getVariant(blockState).getDrops(random, this, getMetaFromState(blockState), fortune)
    }

    override fun canSilkHarvest(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer) = true
    override fun getExpDrop(state: IBlockState, world: IBlockAccess, pos: BlockPos, fortune: Int): Int {
        val random = if (world is World) world.rand else Random()
        return getVariant(state).getExpDrop(random, fortune)
    }


    @SideOnly(Side.CLIENT)
    override fun getBlockLayer() = BlockRenderLayer.CUTOUT_MIPPED
}


interface IBlockVariantOre : IBlockVariant {
    val harvestTool: String get() = "pickaxe"
    val harvestLevel: Int
    fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> = listOf(block.createItemStack(metadata = metadata))

    /**
     * 非シルクタッチでの破壊時に得られる経験値の量です。
     * 破壊時に鉱石ブロックがそのまま得られる場合、原則として0を返します。
     *
     * バニラでの設定は以下の通りです。
     *
     * ```
     * coal   : [0, 2)
     * diamond: [3, 7)
     * emerald: [3, 7)
     * lapis  : [2, 5)
     * quartz : [2, 5)
     * ```
     */
    fun getExpDrop(random: Random, fortune: Int): Int = 0

    val hardness: Float
    val resistance: Float
}

private class GemProvider(val itemStackSupplier: () -> ItemStack?, val amount: Double, val amountPerFortune: Double, val expMin: Int, val expMax: Int)

enum class EnumVariantOre1(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    override val hardness: Float,
    override val resistance: Float,
    override val harvestLevel: Int,
    private val gemProvider: GemProvider
) : IStringSerializable, IBlockVariantOre {
    APATITE_ORE(0, "apatite_ore", "oreApatite", 3f, 5f, 1, GemProvider({ getItemStack("gemApatite") }, 1.0, 1.5, 1, 3)),
    FLUORITE_ORE(1, "fluorite_ore", "oreFluorite", 3f, 5f, 2, GemProvider({ getItemStack("gemFluorite") }, 1.0, 1.0, 15, 30)),
    SULFUR_ORE(2, "sulfur_ore", "oreSulfur", 3f, 5f, 1, GemProvider({ getItemStack("gemSulfur") }, 1.0, 1.5, 1, 3)),
    CINNABAR_ORE(3, "cinnabar_ore", "oreCinnabar", 3f, 5f, 2, GemProvider({ getItemStack("gemCinnabar") }, 1.0, 1.0, 1, 3)),
    MOONSTONE_ORE(4, "moonstone_ore", "oreMoonstone", 3f, 5f, 2, GemProvider({ getItemStack("gemMoonstone") }, 1.0, 0.5, 20, 40)),
    MAGNETITE_ORE(5, "magnetite_ore", "oreMagnetite", 3f, 5f, 1, GemProvider({ getItemStack("gemMagnetite") }, 1.0, 2.0, 1, 2)),

    PYROPE_ORE(6, "pyrope_ore", "orePyrope", 3f, 5f, 2, GemProvider({ getItemStack("gemPyrope") }, 1.0, 0.5, 1, 5)),
    SMITHSONITE_ORE(7, "smithsonite_ore", "oreSmithsonite", 3f, 5f, 1, GemProvider({ getItemStack("gemSmithsonite") }, 1.0, 1.0, 1, 3)),

    NETHERRACK_APATITE_ORE(8, "netherrack_apatite_ore", "oreApatite", 0.4f, 0.4f, 1, GemProvider({ getItemStack("gemApatite") }, 1.0, 1.5, 1, 3)),
    NETHERRACK_FLUORITE_ORE(9, "netherrack_fluorite_ore", "oreFluorite", 0.4f, 0.4f, 2, GemProvider({ getItemStack("gemFluorite") }, 1.0, 1.0, 15, 30)),
    NETHERRACK_SULFUR_ORE(10, "netherrack_sulfur_ore", "oreSulfur", 0.4f, 0.4f, 1, GemProvider({ getItemStack("gemSulfur") }, 1.0, 1.5, 1, 3)),
    NETHERRACK_CINNABAR_ORE(11, "netherrack_cinnabar_ore", "oreCinnabar", 0.4f, 0.4f, 2, GemProvider({ getItemStack("gemCinnabar") }, 1.0, 1.0, 1, 3)),
    NETHERRACK_MOONSTONE_ORE(12, "netherrack_moonstone_ore", "oreMoonstone", 0.4f, 0.4f, 2, GemProvider({ getItemStack("gemMoonstone") }, 1.0, 0.5, 20, 40)),
    NETHERRACK_MAGNETITE_ORE(13, "netherrack_magnetite_ore", "oreMagnetite", 0.4f, 0.4f, 1, GemProvider({ getItemStack("gemMagnetite") }, 1.0, 2.0, 1, 2)),

    NEPHRITE_ORE(14, "nephrite_ore", "oreNephrite", 3f, 5f, 1, GemProvider({ getItemStack("gemNephrite") }, 1.0, 2.0, 1, 3)),
    TOPAZ_ORE(15, "topaz_ore", "oreTopaz", 3f, 5f, 2, GemProvider({ getItemStack("gemTopaz") }, 1.0, 0.5, 1, 5)),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName

    override fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> {
        return (0 until random.randomInt(gemProvider.amount + random.nextDouble() * gemProvider.amountPerFortune * fortune)).mapNotNull {
            gemProvider.itemStackSupplier()?.copy()
        }
    }

    override fun getExpDrop(random: Random, fortune: Int) = MathHelper.getInt(random, gemProvider.expMin, gemProvider.expMax)

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}

enum class EnumVariantOre2(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    override val hardness: Float,
    override val resistance: Float,
    override val harvestLevel: Int,
    private val gemProvider: GemProvider
) : IStringSerializable, IBlockVariantOre {
    TOURMALINE_ORE(0, "tourmaline_ore", "oreTourmaline", 3f, 5f, 2, GemProvider({ getItemStack("gemTourmaline") }, 1.0, 0.5, 1, 5)),
    HELIOLITE_ORE(1, "heliolite_ore", "oreHeliolite", 3f, 5f, 2, GemProvider({ getItemStack("gemHeliolite") }, 1.0, 0.5, 10, 20)),
    END_STONE_LABRADORITE_ORE(2, "end_stone_labradorite_ore", "oreLabradorite", 3f, 5f, 2, GemProvider({ getItemStack("gemLabradorite") }, 1.0, 0.5, 15, 30)),
    PYRITE_ORE(3, "pyrite_ore", "orePyrite", 3f, 5f, 1, GemProvider({ getItemStack("gemPyrite") }, 1.0, 1.5, 1, 3)),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName

    override fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> {
        return (0 until random.randomInt(gemProvider.amount + random.nextDouble() * gemProvider.amountPerFortune * fortune)).mapNotNull {
            gemProvider.itemStackSupplier()?.copy()
        }
    }

    override fun getExpDrop(random: Random, fortune: Int) = MathHelper.getInt(random, gemProvider.expMin, gemProvider.expMax)

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}


class ItemBlockOre<V : IBlockVariantOre>(block: BlockOre<V>) : ItemBlockMulti<BlockOre<V>, V>(block)
