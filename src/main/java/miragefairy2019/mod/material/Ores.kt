package miragefairy2019.mod.material

import miragefairy2019.common.toOreName
import miragefairy2019.libkt.BlockVariantList
import miragefairy2019.libkt.block
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.resourcemaker.DataBlockModel
import miragefairy2019.resourcemaker.DataBlockState
import miragefairy2019.resourcemaker.DataBlockStates
import miragefairy2019.resourcemaker.DataElement
import miragefairy2019.resourcemaker.DataPoint
import miragefairy2019.resourcemaker.block
import miragefairy2019.resourcemaker.makeBlockModel
import miragefairy2019.resourcemaker.makeBlockStates
import miragefairy2019.resourcemaker.makeItemModel
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import java.util.Random

object Ores {
    lateinit var blockOre1: () -> BlockOre<BlockVariantOre>
    lateinit var itemBlockOre1: () -> ItemBlockOre<BlockVariantOre>
    lateinit var blockOre2: () -> BlockOre<BlockVariantOre>
    lateinit var itemBlockOre2: () -> ItemBlockOre<BlockVariantOre>
    val module = module {

        // 鉱石ブロック1
        blockOre1 = block({ BlockOre(EnumVariantOre1.variantList) }, "ore1") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
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

        // 鉱石ブロック2
        blockOre2 = block({ BlockOre(EnumVariantOre2.variantList) }, "ore2") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
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

        // 翻訳の生成
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
            enJa("tile.oreTourmaline.name", "Tourmaline Ore", "トルマリン鉱石")
            enJa("tile.oreHeliolite.name", "Heliolite Ore", "ヘリオライト鉱石")
            enJa("tile.oreLabradorite.name", "Labradorite Ore", "ラブラドライト鉱石")
            enJa("tile.orePyrite.name", "Pyrite Ore", "パイライト鉱石")
        }

        // ブロックモデルの生成
        run {
            makeBlockModel("overlay_block") {
                DataBlockModel(
                    parent = "block/block",
                    elements = listOf(
                        DataElement(
                            from = DataPoint(0.0, 0.0, 0.0),
                            to = DataPoint(16.0, 16.0, 16.0),
                            faces = jsonObject(
                                "down" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#base".jsonElement,
                                    "tintindex" to 0.jsonElement,
                                    "cullface" to "down".jsonElement
                                ),
                                "up" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#base".jsonElement,
                                    "tintindex" to 0.jsonElement,
                                    "cullface" to "up".jsonElement
                                ),
                                "north" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#base".jsonElement,
                                    "tintindex" to 0.jsonElement,
                                    "cullface" to "north".jsonElement
                                ),
                                "south" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#base".jsonElement,
                                    "tintindex" to 0.jsonElement,
                                    "cullface" to "south".jsonElement
                                ),
                                "west" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#base".jsonElement,
                                    "tintindex" to 0.jsonElement,
                                    "cullface" to "west".jsonElement
                                ),
                                "east" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#base".jsonElement,
                                    "tintindex" to 0.jsonElement,
                                    "cullface" to "east".jsonElement
                                )
                            )
                        ),
                        DataElement(
                            from = DataPoint(0.0, 0.0, 0.0),
                            to = DataPoint(16.0, 16.0, 16.0),
                            faces = jsonObject(
                                "down" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#overlay".jsonElement,
                                    "tintindex" to 1.jsonElement,
                                    "cullface" to "down".jsonElement
                                ),
                                "up" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#overlay".jsonElement,
                                    "tintindex" to 1.jsonElement,
                                    "cullface" to "up".jsonElement
                                ),
                                "north" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#overlay".jsonElement,
                                    "tintindex" to 1.jsonElement,
                                    "cullface" to "north".jsonElement
                                ),
                                "south" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#overlay".jsonElement,
                                    "tintindex" to 1.jsonElement,
                                    "cullface" to "south".jsonElement
                                ),
                                "west" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#overlay".jsonElement,
                                    "tintindex" to 1.jsonElement,
                                    "cullface" to "west".jsonElement
                                ),
                                "east" to jsonObject(
                                    "uv" to jsonArray(
                                        0.jsonElement,
                                        0.jsonElement,
                                        16.jsonElement,
                                        16.jsonElement
                                    ),
                                    "texture" to "#overlay".jsonElement,
                                    "tintindex" to 1.jsonElement,
                                    "cullface" to "east".jsonElement
                                )
                            )
                        )
                    )
                )
            }
            fun makeBlockModel(name: String, base: String, overlay: String) = makeBlockModel(name) {
                DataBlockModel(
                    parent = "miragefairy2019:block/overlay_block",
                    textures = mapOf(
                        "particle" to base,
                        "base" to base,
                        "overlay" to overlay
                    )
                )
            }
            makeBlockModel("apatite_ore", "blocks/stone", "miragefairy2019:blocks/apatite_ore")
            makeBlockModel("fluorite_ore", "blocks/stone", "miragefairy2019:blocks/fluorite_ore")
            makeBlockModel("sulfur_ore", "blocks/stone", "miragefairy2019:blocks/sulfur_ore")
            makeBlockModel("cinnabar_ore", "blocks/stone", "miragefairy2019:blocks/cinnabar_ore")
            makeBlockModel("moonstone_ore", "blocks/stone", "miragefairy2019:blocks/moonstone_ore")
            makeBlockModel("magnetite_ore", "blocks/stone", "miragefairy2019:blocks/magnetite_ore")
            makeBlockModel("pyrope_ore", "blocks/stone", "miragefairy2019:blocks/pyrope_ore")
            makeBlockModel("smithsonite_ore", "blocks/stone", "miragefairy2019:blocks/smithsonite_ore")
            makeBlockModel("netherrack_apatite_ore", "blocks/netherrack", "miragefairy2019:blocks/apatite_ore")
            makeBlockModel("netherrack_fluorite_ore", "blocks/netherrack", "miragefairy2019:blocks/fluorite_ore")
            makeBlockModel("netherrack_sulfur_ore", "blocks/netherrack", "miragefairy2019:blocks/sulfur_ore")
            makeBlockModel("netherrack_cinnabar_ore", "blocks/netherrack", "miragefairy2019:blocks/cinnabar_ore")
            makeBlockModel("netherrack_magnetite_ore", "blocks/netherrack", "miragefairy2019:blocks/magnetite_ore")
            makeBlockModel("netherrack_moonstone_ore", "blocks/netherrack", "miragefairy2019:blocks/moonstone_ore")
            makeBlockModel("nephrite_ore", "blocks/stone", "miragefairy2019:blocks/nephrite_ore")
            makeBlockModel("topaz_ore", "blocks/stone", "miragefairy2019:blocks/topaz_ore")
            makeBlockModel("tourmaline_ore", "blocks/stone", "miragefairy2019:blocks/tourmaline_ore")
            makeBlockModel("heliolite_ore", "blocks/stone", "miragefairy2019:blocks/heliolite_ore")
            makeBlockModel("end_stone_labradorite_ore", "blocks/end_stone", "miragefairy2019:blocks/labradorite_ore")
            makeBlockModel("pyrite_ore", "blocks/stone", "miragefairy2019:blocks/pyrite_ore")
        }

        // アイテムモデルの生成
        run {
            makeItemModel("apatite_ore") { block }
            makeItemModel("fluorite_ore") { block }
            makeItemModel("sulfur_ore") { block }
            makeItemModel("cinnabar_ore") { block }
            makeItemModel("moonstone_ore") { block }
            makeItemModel("magnetite_ore") { block }
            makeItemModel("pyrope_ore") { block }
            makeItemModel("smithsonite_ore") { block }
            makeItemModel("netherrack_apatite_ore") { block }
            makeItemModel("netherrack_fluorite_ore") { block }
            makeItemModel("netherrack_sulfur_ore") { block }
            makeItemModel("netherrack_cinnabar_ore") { block }
            makeItemModel("netherrack_moonstone_ore") { block }
            makeItemModel("netherrack_magnetite_ore") { block }
            makeItemModel("nephrite_ore") { block }
            makeItemModel("topaz_ore") { block }
            makeItemModel("tourmaline_ore") { block }
            makeItemModel("heliolite_ore") { block }
            makeItemModel("end_stone_labradorite_ore") { block }
            makeItemModel("pyrite_ore") { block }
        }

    }
}

class BlockVariantOre(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    override val hardness: Float,
    override val resistance: Float,
    override val harvestLevel: Int,
    private val itemStackSupplier: () -> ItemStack?,
    private val amount: Double,
    private val amountPerFortune: Double,
    private val exp: IntRange
) : IBlockVariantOre {
    override fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> {
        return (0 until random.randomInt(amount + random.nextDouble() * amountPerFortune * fortune)).mapNotNull {
            itemStackSupplier()?.copy()
        }
    }

    override fun getExpDrop(random: Random, fortune: Int) = MathHelper.getInt(random, exp.first, exp.last)
}

enum class EnumVariantOre1(val blockVariant: BlockVariantOre) {
    APATITE_ORE(BlockVariantOre(0, "apatite_ore", "oreApatite", 3f, 5f, 1, { "gemApatite".toOreName().copyItemStack() }, 1.0, 1.5, 1..3)),
    FLUORITE_ORE(BlockVariantOre(1, "fluorite_ore", "oreFluorite", 3f, 5f, 2, { "gemFluorite".toOreName().copyItemStack() }, 1.0, 1.0, 15..30)),
    SULFUR_ORE(BlockVariantOre(2, "sulfur_ore", "oreSulfur", 3f, 5f, 1, { "gemSulfur".toOreName().copyItemStack() }, 1.0, 1.5, 1..3)),
    CINNABAR_ORE(BlockVariantOre(3, "cinnabar_ore", "oreCinnabar", 3f, 5f, 2, { "gemCinnabar".toOreName().copyItemStack() }, 1.0, 1.0, 1..3)),
    MOONSTONE_ORE(BlockVariantOre(4, "moonstone_ore", "oreMoonstone", 3f, 5f, 2, { "gemMoonstone".toOreName().copyItemStack() }, 1.0, 0.5, 20..40)),
    MAGNETITE_ORE(BlockVariantOre(5, "magnetite_ore", "oreMagnetite", 3f, 5f, 1, { "gemMagnetite".toOreName().copyItemStack() }, 1.0, 2.0, 1..2)),

    PYROPE_ORE(BlockVariantOre(6, "pyrope_ore", "orePyrope", 3f, 5f, 2, { "gemPyrope".toOreName().copyItemStack() }, 1.0, 0.5, 1..5)),
    SMITHSONITE_ORE(BlockVariantOre(7, "smithsonite_ore", "oreSmithsonite", 3f, 5f, 1, { "gemSmithsonite".toOreName().copyItemStack() }, 1.0, 1.0, 1..3)),

    NETHERRACK_APATITE_ORE(BlockVariantOre(8, "netherrack_apatite_ore", "oreApatite", 0.4f, 0.4f, 1, { "gemApatite".toOreName().copyItemStack() }, 1.0, 1.5, 1..3)),
    NETHERRACK_FLUORITE_ORE(BlockVariantOre(9, "netherrack_fluorite_ore", "oreFluorite", 0.4f, 0.4f, 2, { "gemFluorite".toOreName().copyItemStack() }, 1.0, 1.0, 15..30)),
    NETHERRACK_SULFUR_ORE(BlockVariantOre(10, "netherrack_sulfur_ore", "oreSulfur", 0.4f, 0.4f, 1, { "gemSulfur".toOreName().copyItemStack() }, 1.0, 1.5, 1..3)),
    NETHERRACK_CINNABAR_ORE(BlockVariantOre(11, "netherrack_cinnabar_ore", "oreCinnabar", 0.4f, 0.4f, 2, { "gemCinnabar".toOreName().copyItemStack() }, 1.0, 1.0, 1..3)),
    NETHERRACK_MOONSTONE_ORE(BlockVariantOre(12, "netherrack_moonstone_ore", "oreMoonstone", 0.4f, 0.4f, 2, { "gemMoonstone".toOreName().copyItemStack() }, 1.0, 0.5, 20..40)),
    NETHERRACK_MAGNETITE_ORE(BlockVariantOre(13, "netherrack_magnetite_ore", "oreMagnetite", 0.4f, 0.4f, 1, { "gemMagnetite".toOreName().copyItemStack() }, 1.0, 2.0, 1..2)),

    NEPHRITE_ORE(BlockVariantOre(14, "nephrite_ore", "oreNephrite", 3f, 5f, 1, { "gemNephrite".toOreName().copyItemStack() }, 1.0, 2.0, 1..3)),
    TOPAZ_ORE(BlockVariantOre(15, "topaz_ore", "oreTopaz", 3f, 5f, 2, { "gemTopaz".toOreName().copyItemStack() }, 1.0, 0.5, 1..5)),
    ;

    companion object {
        val variantList = BlockVariantList(values().map { it.blockVariant })
    }
}

enum class EnumVariantOre2(val blockVariant: BlockVariantOre) {
    TOURMALINE_ORE(BlockVariantOre(0, "tourmaline_ore", "oreTourmaline", 3f, 5f, 2, { "gemTourmaline".toOreName().copyItemStack() }, 1.0, 0.5, 1..5)),
    HELIOLITE_ORE(BlockVariantOre(1, "heliolite_ore", "oreHeliolite", 3f, 5f, 2, { "gemHeliolite".toOreName().copyItemStack() }, 1.0, 0.5, 10..20)),
    END_STONE_LABRADORITE_ORE(BlockVariantOre(2, "end_stone_labradorite_ore", "oreLabradorite", 3f, 5f, 2, { "gemLabradorite".toOreName().copyItemStack() }, 1.0, 0.5, 15..30)),
    PYRITE_ORE(BlockVariantOre(3, "pyrite_ore", "orePyrite", 3f, 5f, 1, { "gemPyrite".toOreName().copyItemStack() }, 1.0, 1.5, 1..3)),
    ;

    companion object {
        val variantList = BlockVariantList(values().map { it.blockVariant })
    }
}
