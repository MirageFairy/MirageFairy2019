package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.DataItemModel
import miragefairy2019.libkt.DataOrIngredient
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.MakeItemVariantModelScope
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.generated
import miragefairy2019.libkt.getItemStack
import miragefairy2019.libkt.handheld
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.makeItemVariantModel
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.BlockMulti
import miragefairy2019.mod.lib.BlockVariantList
import miragefairy2019.mod.lib.IBlockVariant
import miragefairy2019.mod.lib.ItemBlockMulti
import miragefairy2019.mod.lib.ItemMultiMaterial
import miragefairy2019.mod.lib.ItemVariantMaterial
import miragefairy2019.mod.lib.setCustomModelResourceLocations
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.IStringSerializable
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.time.LocalDateTime
import java.util.Random

object CommonMaterials {
    lateinit var blockOre1: () -> BlockOre<EnumVariantOre1>
    lateinit var itemBlockOre1: () -> ItemBlockOre<EnumVariantOre1>

    lateinit var blockOre2: () -> BlockOre<EnumVariantOre2>
    lateinit var itemBlockOre2: () -> ItemBlockOre<EnumVariantOre2>

    lateinit var itemMaterials: () -> ItemSimpleMaterials

    lateinit var blockMaterials1: () -> BlockMaterials<EnumVariantMaterials1>
    lateinit var itemBlockMaterials1: () -> ItemBlockMaterials<EnumVariantMaterials1>
    val module = module {

        // 素材レシピ

        // Tier 2 金鉱石 -> 金
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "gold_ore_smelt_tier_2"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandBreaking"),
                    DataSimpleIngredient(item = "minecraft:gold_ore")
                ),
                result = DataResult(
                    item = "minecraft:gold_nugget",
                    count = 17
                )
            )
        )

        // Tier 4 金鉱石 -> 金
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "gold_ore_smelt_tier_4"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandFusion"),
                    DataOreIngredient(ore = "dustMiragium"),
                    DataSimpleIngredient(item = "minecraft:gold_ore")
                ),
                result = DataResult(
                    item = "minecraft:gold_ingot",
                    count = 3
                )
            )
        )

        // Tier 2 鉄鉱石 -> 鉄
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "iron_ore_smelt_tier_2"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandBreaking"),
                    DataSimpleIngredient(item = "minecraft:iron_ore")
                ),
                result = DataResult(
                    item = "minecraft:iron_nugget",
                    count = 17
                )
            )
        )

        // Tier 4 鉄鉱石 -> 鉄
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "iron_ore_smelt_tier_4"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandFusion"),
                    DataOreIngredient(ore = "dustMiragium"),
                    DataSimpleIngredient(item = "minecraft:iron_ore")
                ),
                result = DataResult(
                    item = "minecraft:iron_ingot",
                    count = 3
                )
            )
        )

        // Tier 2 磁鉄鉱の粉 -> 鉄
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "magnetite_smelt_tier_2"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    DataOrIngredient(
                        DataOreIngredient(ore = "dustCoal"),
                        DataOreIngredient(ore = "dustCharcoal")
                    ),
                    DataOreIngredient(ore = "dustMagnetite")
                ),
                result = DataResult(
                    item = "minecraft:iron_nugget",
                    count = 3
                )
            )
        )

        // Tier 4 磁鉄鉱の粉 -> 鉄
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "magnetite_smelt_tier_4"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandDistortion"),
                    DataOreIngredient(ore = "dustMagnetite"),
                    DataOreIngredient(ore = "dustMagnetite"),
                    DataOreIngredient(ore = "dustMagnetite"),
                    DataOreIngredient(ore = "dustMagnetite"),
                    DataOreIngredient(ore = "dustMagnetite"),
                    DataOreIngredient(ore = "dustMagnetite"),
                    DataOreIngredient(ore = "dustMagnetite")
                ),
                result = DataResult(
                    item = "minecraft:iron_ingot",
                    count = 3
                )
            )
        )

        onAddRecipe {
            GameRegistry.addSmelting(getItemStack("gemPyrite"), Items.IRON_NUGGET.createItemStack(count = 3), 0.7f)
        }


        // 鉱石ブロック1
        blockOre1 = block({ BlockOre(EnumVariantOre1.variantList) }, "ore1") {
            setCreativeTab { ApiMain.creativeTab }
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
            setCreativeTab { ApiMain.creativeTab }
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

        // アイテム状素材
        itemMaterials = item({ ItemSimpleMaterials() }, "materials") {
            setUnlocalizedName("materials")
            setCreativeTab { ApiMain.creativeTab }

            fun r(
                metadata: Int,
                registryName: String,
                unlocalizedName: String,
                oreName: String,
                modelSupplier: MakeItemVariantModelScope<ItemSimpleMaterials, ItemVariantSimpleMaterials>.() -> DataItemModel
            ) = itemVariant(registryName, { ItemVariantSimpleMaterials(it, unlocalizedName) }, metadata) {
                addOreName(oreName)
                makeItemVariantModel { modelSupplier() }
            }

            fun ItemVariantInitializer<ItemSimpleMaterials, ItemVariantSimpleMaterials>.fuel(burnTime: Int) = also { itemInitializer.modInitializer.onRegisterItem { itemVariant.burnTime = burnTime } }

            r(0, "apatite_gem", "gemApatite", "gemApatite", { generated })
            r(1, "fluorite_gem", "gemFluorite", "gemFluorite", { generated })
            r(2, "sulfur_gem", "gemSulfur", "gemSulfur", { generated })
            r(3, "miragium_dust", "dustMiragium", "dustMiragium", { generated })
            r(4, "miragium_tiny_dust", "dustTinyMiragium", "dustTinyMiragium", { generated })
            r(5, "miragium_ingot", "ingotMiragium", "ingotMiragium", { generated })
            r(6, "cinnabar_gem", "gemCinnabar", "gemCinnabar", { generated })
            r(7, "moonstone_gem", "gemMoonstone", "gemMoonstone", { generated })
            r(8, "magnetite_gem", "gemMagnetite", "gemMagnetite", { generated })
            r(9, "saltpeter_gem", "gemSaltpeter", "gemSaltpeter", { generated })
            r(10, "pyrope_gem", "gemPyrope", "gemPyrope", { generated })
            r(11, "smithsonite_gem", "gemSmithsonite", "gemSmithsonite", { generated })
            r(12, "miragium_rod", "rodMiragium", "rodMiragium", { handheld })
            r(13, "miragium_nugget", "nuggetMiragium", "nuggetMiragium", { generated })
            r(14, "nephrite_gem", "gemNephrite", "gemNephrite", { generated })
            r(15, "topaz_gem", "gemTopaz", "gemTopaz", { generated })
            r(16, "tourmaline_gem", "gemTourmaline", "gemTourmaline", { generated })
            r(17, "heliolite_gem", "gemHeliolite", "gemHeliolite", { generated })
            r(18, "labradorite_gem", "gemLabradorite", "gemLabradorite", { generated })
            r(19, "lilagium_ingot", "ingotLilagium", "ingotLilagium", { generated })
            r(20, "miragium_plate", "plateMiragium", "plateMiragium", { generated })
            r(21, "coal_dust", "dustCoal", "dustCoal", { generated }).fuel(1600)
            r(22, "charcoal_dust", "dustCharcoal", "dustCharcoal", { generated }).fuel(1600)
            r(23, "apatite_dust", "dustApatite", "dustApatite", { generated })
            r(24, "fluorite_dust", "dustFluorite", "dustFluorite", { generated })
            r(25, "sulfur_dust", "dustSulfur", "dustSulfur", { generated })
            r(26, "cinnabar_dust", "dustCinnabar", "dustCinnabar", { generated })
            r(27, "moonstone_dust", "dustMoonstone", "dustMoonstone", { generated })
            r(28, "magnetite_dust", "dustMagnetite", "dustMagnetite", { generated })
            r(29, "pyrite_gem", "gemPyrite", "gemPyrite", { generated }).apply {
                // TODO remove
                onRegisterItem {
                    if (LocalDateTime.now() < LocalDateTime.of(2022, 4, 1, 0, 0, 0)) {
                        itemVariant.canSeeOnCreativeTab = false
                    }
                }
            }

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }
        onMakeLang {
            enJa("item.gemApatite.name", "Apatite", "燐灰石")
            enJa("item.gemFluorite.name", "Fluorite", "蛍石")
            enJa("item.gemSulfur.name", "Sulfur", "硫黄")
            enJa("item.dustMiragium.name", "Miragium Dust", "ミラジウムの粉")
            enJa("item.dustTinyMiragium.name", "Tiny Pile of Miragium Dust", "ミラジウムの微粉")
            enJa("item.ingotMiragium.name", "Miragium Ingot", "ミラジウムインゴット")
            enJa("item.gemCinnabar.name", "Cinnabar", "辰砂")
            enJa("item.gemMoonstone.name", "Moonstone", "月長石")
            enJa("item.gemMagnetite.name", "Magnetite", "磁鉄鉱")
            enJa("item.gemSaltpeter.name", "Saltpeter", "硝石")
            enJa("item.gemPyrope.name", "Pyrope", "パイロープ")
            enJa("item.gemSmithsonite.name", "Smithsonite", "スミソナイト")
            enJa("item.rodMiragium.name", "Miragium Rod", "ミラジウムの棒")
            enJa("item.nuggetMiragium.name", "Miragium Nugget", "ミラジウムの塊")
            enJa("item.gemNephrite.name", "Nephrite", "ネフライト")
            enJa("item.gemTopaz.name", "Topaz", "トパーズ")
            enJa("item.gemTourmaline.name", "Tourmaline", "トルマリン")
            enJa("item.gemHeliolite.name", "Heliolite", "ヘリオライト")
            enJa("item.gemLabradorite.name", "Labradorite", "ラブラドライト")
            enJa("item.ingotLilagium.name", "Lilagium Ingot", "リラジウムインゴット")
            enJa("item.plateMiragium.name", "Miragium Plate", "ミラジウムの板")
            enJa("item.dustCoal.name", "Coal Dust", "石炭の粉")
            enJa("item.dustCharcoal.name", "Charcoal Dust", "木炭の粉")
            enJa("item.dustApatite.name", "Apatite Dust", "燐灰石の粉")
            enJa("item.dustFluorite.name", "Fluorite Dust", "蛍石の粉")
            enJa("item.dustSulfur.name", "Sulfur Dust", "硫黄の粉")
            enJa("item.dustCinnabar.name", "Cinnabar Dust", "辰砂の粉")
            enJa("item.dustMoonstone.name", "Moonstone Dust", "月長石の粉")
            enJa("item.dustMagnetite.name", "Magnetite Dust", "磁鉄鉱の粉")
            enJa("item.gemPyrite.name", "Pyrite", "パイライト")
        }

        // ブロック状素材
        blockMaterials1 = block({ BlockMaterials(EnumVariantMaterials1.variantList) }, "materials1") {
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf(
                        "miragefairy2019:apatite_block",
                        "miragefairy2019:fluorite_block",
                        "miragefairy2019:sulfur_block",
                        "miragefairy2019:cinnabar_block",
                        "miragefairy2019:moonstone_block",
                        "miragefairy2019:magnetite_block",
                        "miragefairy2019:pyrope_block",
                        "miragefairy2019:smithsonite_block",
                        "miragefairy2019:charcoal_block",
                        "miragefairy2019:mirage_flower_leaf_block",
                        "miragefairy2019:miragium_ingot_block",
                        "miragefairy2019:miragium_dust_block",
                        "miragefairy2019:nephrite_block",
                        "miragefairy2019:topaz_block",
                        "miragefairy2019:tourmaline_block",
                        "minecraft:stone"
                    ).mapIndexed { i, model -> "variant=$i" to DataBlockState(model = model) }.toMap()
                )
            }
        }
        itemBlockMaterials1 = item({ ItemBlockMaterials(blockMaterials1()) }, "materials1") {
            onRegisterItem {
                blockMaterials1().variantList.blockVariants.forEach {
                    item.setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                }
            }
            onCreateItemStack {
                blockMaterials1().variantList.blockVariants.forEach {
                    item.addOreName(it.oreName, it.metadata)
                }
            }
        }
        onMakeLang {
            enJa("tile.blockApatite.name", "Block of Apatite", "燐灰石ブロック")
            enJa("tile.blockFluorite.name", "Block of Fluorite", "蛍石ブロック")
            enJa("tile.blockSulfur.name", "Block of Sulfur", "硫黄ブロック")
            enJa("tile.blockCinnabar.name", "Block of Cinnabar", "辰砂ブロック")
            enJa("tile.blockMoonstone.name", "Block of Moonstone", "月長石ブロック")
            enJa("tile.blockMagnetite.name", "Block of Magnetite", "磁鉄鉱ブロック")
            enJa("tile.blockPyrope.name", "Block of Pyrope", "パイロープブロック")
            enJa("tile.blockSmithsonite.name", "Block of Smithsonite", "スミソナイトブロック")
            enJa("tile.blockCharcoal.name", "Block of Charcoal", "木炭ブロック")
            enJa("tile.blockLeafMirageFlower.name", "Block of Mirage Flower Leaf", "ミラージュフラワーの葉ブロック")
            enJa("tile.blockMiragium.name", "Block of Miragium", "ミラジウムブロック")
            enJa("tile.blockDustMiragium.name", "Block of Miragium Dust", "ミラジウムの粉ブロック")
            enJa("tile.blockNephrite.name", "Block of Nephrite", "ネフライトブロック")
            enJa("tile.blockTopaz.name", "Block of Topaz", "トパーズブロック")
            enJa("tile.blockTourmaline.name", "Block of Tourmaline", "トルマリンブロック")
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

    class GemProvider(val itemStackSupplier: () -> ItemStack, val amount: Double, val amountPerFortune: Double, val expMin: Int, val expMax: Int)

    override fun toString() = resourceName
    override fun getName() = resourceName

    override fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> {
        return (0 until random.randomInt(gemProvider.amount + random.nextDouble() * gemProvider.amountPerFortune * fortune)).map {
            gemProvider.itemStackSupplier().copy()
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
    private val gemProvider: GemProvider,
    override val canSeeOnCreativeTab: Boolean = true
) : IStringSerializable, IBlockVariantOre {
    TOURMALINE_ORE(0, "tourmaline_ore", "oreTourmaline", 3f, 5f, 2, GemProvider({ getItemStack("gemTourmaline") }, 1.0, 0.5, 1, 5)),
    HELIOLITE_ORE(1, "heliolite_ore", "oreHeliolite", 3f, 5f, 2, GemProvider({ getItemStack("gemHeliolite") }, 1.0, 0.5, 10, 20)),
    END_STONE_LABRADORITE_ORE(2, "end_stone_labradorite_ore", "oreLabradorite", 3f, 5f, 2, GemProvider({ getItemStack("gemLabradorite") }, 1.0, 0.5, 15, 30)),
    PYRITE_ORE(3, "pyrite_ore", "orePyrite", 3f, 5f, 1, GemProvider({ getItemStack("gemPyrite") }, 1.0, 1.5, 1, 3), Unit.run {
        // TODO remove
        if (LocalDateTime.now() < LocalDateTime.of(2022, 4, 1, 0, 0, 0)) {
            false
        } else {
            true
        }
    }),
    ;

    class GemProvider(val itemStackSupplier: () -> ItemStack, val amount: Double, val amountPerFortune: Double, val expMin: Int, val expMax: Int)

    override fun toString() = resourceName
    override fun getName() = resourceName

    override fun getDrops(random: Random, block: Block, metadata: Int, fortune: Int): List<ItemStack> {
        return (0 until random.randomInt(gemProvider.amount + random.nextDouble() * gemProvider.amountPerFortune * fortune)).map {
            gemProvider.itemStackSupplier().copy()
        }
    }

    override fun getExpDrop(random: Random, fortune: Int) = MathHelper.getInt(random, gemProvider.expMin, gemProvider.expMax)

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}

class ItemBlockOre<V : IBlockVariantOre>(block: BlockOre<V>) : ItemBlockMulti<BlockOre<V>, V>(block)

class ItemSimpleMaterials : ItemMultiMaterial<ItemVariantSimpleMaterials>() {
    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.burnTime ?: -1
}

class ItemVariantSimpleMaterials(registryName: String, unlocalizedName: String) : ItemVariantMaterial(registryName, unlocalizedName) {
    var burnTime: Int? = null
}

class BlockMaterials<V : IBlockVariantMaterials>(variantList: BlockVariantList<V>) : BlockMulti<V>(Material.IRON, variantList) {
    init {
        variantList.blockVariants.forEach { it -> setHarvestLevel(it.harvestTool, it.harvestLevel, getState(it)) }
    }


    // 一般
    override fun getMaterial(blockState: IBlockState) = getVariant(blockState).material
    override fun getBlockHardness(blockState: IBlockState, world: World, blockPos: BlockPos) = getVariant(blockState).blockHardness
    override fun getExplosionResistance(world: World, blockPos: BlockPos, exploder: Entity?, explosion: Explosion) = getVariant(world.getBlockState(blockPos)).blockHardness * 5
    override fun getSoundType(blockState: IBlockState, world: World, blockPos: BlockPos, entity: Entity?) = getVariant(blockState).soundType


    // ドロップ系
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = true


    // 落下判定

    override fun onBlockAdded(world: World, blockPos: BlockPos, blockState: IBlockState) {
        if (!getVariant(blockState).isFallable) return
        world.scheduleUpdate(blockPos, this, tickRate(world))
    }

    override fun neighborChanged(blockState: IBlockState, world: World, blockPos: BlockPos, block: Block, fromBlockPos: BlockPos) {
        if (!getVariant(blockState).isFallable) return
        world.scheduleUpdate(blockPos, this, tickRate(world))
    }

    override fun updateTick(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        if (world.isRemote) return
        if (!getVariant(blockState).isFallable) return
        checkFallable(world, blockPos)
    }

    private fun checkFallable(world: World, blockPos: BlockPos) {
        fun canFallThrough(blockPos: BlockPos) = world.isAirBlock(blockPos) || BlockFalling.canFallThrough(world.getBlockState(blockPos))

        val downBlockPos = blockPos.down()
        if (!(canFallThrough(downBlockPos) && blockPos.y >= 0)) return // 現在奈落よりも上にあり、かつ床が無い　でないなら落下しない

        if (!BlockFalling.fallInstantly && world.isAreaLoaded(blockPos.add(-32, -32, -32), blockPos.add(32, 32, 32))) {
            // 即時落下状態でなく、周辺が読み込まれているならば、落下ブロックエンティティになる

            if (!world.isRemote) {
                world.spawnEntity(
                    EntityFallingBlock(
                        world,
                        blockPos.x + 0.5,
                        blockPos.y.toDouble(),
                        blockPos.z + 0.5,
                        world.getBlockState(blockPos)
                    )
                )
            }
        } else {
            // 即時落下状態、もしくは周辺が部分的に読み込まれていないならば、即落下する

            val blockState = world.getBlockState(blockPos)
            world.setBlockToAir(blockPos)

            var groundBlockPos = downBlockPos
            while (canFallThrough(groundBlockPos) && groundBlockPos.y > 0) { // 地面かY=0になるまでループ
                groundBlockPos = groundBlockPos.down()
            }

            if (groundBlockPos.y > 0) { // Y=0の位置にある床には着地できない
                world.setBlockState(groundBlockPos.up(), blockState)
            }
        }
    }

    override fun tickRate(world: World) = 2

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(blockState: IBlockState, world: World, blockPos: BlockPos, random: Random) {
        if (!getVariant(blockState).isFallable) return
        if (random.nextInt(16) == 0) {
            val downBlockPos = blockPos.down()
            if (BlockFalling.canFallThrough(world.getBlockState(downBlockPos))) {
                val x = (downBlockPos.x + random.nextFloat()).toDouble()
                val y = downBlockPos.y - 0.05
                val z = (downBlockPos.z + random.nextFloat()).toDouble()
                world.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0, 0.0, 0.0, getStateId(blockState))
            }
        }
    }


    // ビーコン基礎ブロック判定
    override fun isBeaconBase(world: IBlockAccess, blockPos: BlockPos, beaconBlockPos: BlockPos) = getVariant(world.getBlockState(blockPos)).isBeaconBase
}

class HardnessClass(val blockHardness: Float, val harvestTool: String, val harvestLevel: Int) {
    companion object {
        val SOFT = HardnessClass(3.0f, "pickaxe", 0) // 硬度3程度、カルサイト級
        val HARD = HardnessClass(5.0f, "pickaxe", 0) // 硬度5程度、石級
        val VERY_HARD = HardnessClass(5.0f, "pickaxe", 1) // 硬度7程度、クリスタル級
        val SUPER_HARD = HardnessClass(5.0f, "pickaxe", 2) // 硬度9程度、ダイヤモンド級
    }
}

enum class EnumVariantMaterials1(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val oreName: String,
    val hardnessClass: HardnessClass,
    override val burnTime: Int,
    override val soundType: SoundType,
    override val isFallable: Boolean,
    override val material: Material,
    override val isBeaconBase: Boolean
) : IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", HardnessClass.SOFT, 0, SoundType.STONE, false, Material.IRON, true),
    CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", HardnessClass(5.0f, "pickaxe", 0), 20 * 10 * 8 * 9, SoundType.STONE, false, Material.ROCK, false),
    MIRAGE_FLOWER_LEAF_BLOCK(9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", HardnessClass(2.0f, "axe", 0), 0, SoundType.GLASS, false, Material.LEAVES, false),
    MIRAGIUM_INGOT_BLOCK(10, "miragium_ingot_block", "blockMiragium", "blockMiragium", HardnessClass(5.0f, "pickaxe", 1), 0, SoundType.METAL, false, Material.IRON, false),
    MIRAGIUM_DUST_BLOCK(11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", HardnessClass(0.5f, "shovel", 0), 0, SoundType.SNOW, true, Material.SAND, false),
    NEPHRITE_BLOCK(12, "nephrite_block", "blockNephrite", "blockNephrite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    TOPAZ_BLOCK(13, "topaz_block", "blockTopaz", "blockTopaz", HardnessClass.SUPER_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    TOURMALINE_BLOCK(14, "tourmaline_block", "blockTourmaline", "blockTourmaline", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName
    override val blockHardness = hardnessClass.blockHardness
    override val harvestTool = hardnessClass.harvestTool
    override val harvestLevel = hardnessClass.harvestLevel

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}

interface IBlockVariantMaterials : IBlockVariant {
    val blockHardness: Float
    val harvestTool: String
    val harvestLevel: Int
    val burnTime: Int
    val soundType: SoundType
    val isFallable: Boolean
    val material: Material
    val isBeaconBase: Boolean
}

class ItemBlockMaterials<V : IBlockVariantMaterials>(block: BlockMaterials<V>) : ItemBlockMulti<BlockMaterials<V>, V>(block) {
    override fun getItemBurnTime(itemStack: ItemStack) = block2.getVariant(itemStack.metadata).burnTime
}
