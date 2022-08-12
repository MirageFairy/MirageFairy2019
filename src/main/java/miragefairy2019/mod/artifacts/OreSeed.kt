package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.resourcemaker.DataModelBlockDefinition
import miragefairy2019.lib.resourcemaker.DataSingleVariantList
import miragefairy2019.lib.resourcemaker.DataVariant
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.mod.Main
import miragefairy2019.mod.material.EnumVariantOre1
import miragefairy2019.mod.material.EnumVariantOre2
import miragefairy2019.mod.material.Ores
import miragefairy2019.mod.oreseed.ApiOreSeedDrop
import miragefairy2019.mod.oreseed.BlockOreSeed
import miragefairy2019.mod.oreseed.Elements
import miragefairy2019.mod.oreseed.EnumOreSeedShape
import miragefairy2019.mod.oreseed.EnumOreSeedType
import miragefairy2019.mod.oreseed.EnumVariantOreSeed
import miragefairy2019.mod.oreseed.IOreSeedDropRequirement
import miragefairy2019.mod.oreseed.OreSeedDropRegistry
import miragefairy2019.mod.oreseed.OreSeedDropRegistryScope
import miragefairy2019.mod.oreseed.OreSeedDropRequirements
import miragefairy2019.mod.oreseed.Vein
import miragefairy2019.mod.oreseed.WorldGenCompoundOreSeed
import miragefairy2019.mod.oreseed.invoke
import miragefairy2019.mod.oreseed.register
import net.minecraft.block.Block
import net.minecraft.block.BlockStone
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.math.ChunkPos
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.DecorateBiomeEvent
import net.minecraftforge.event.terraingen.OreGenEvent
import net.minecraftforge.event.terraingen.PopulateChunkEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

object OreSeed {
    lateinit var blockOreSeed: () -> BlockOreSeed
    lateinit var blockOreSeedNether: () -> BlockOreSeed
    lateinit var blockOreSeedEnd: () -> BlockOreSeed
    val oreSeedModule = module {

        // 鉱石の種

        // 地上
        blockOreSeed = block({ BlockOreSeed(EnumOreSeedType.STONE) }, "ore_seed") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataModelBlockDefinition(variants = EnumVariantOreSeed.values().associate { "variant=$it" to DataSingleVariantList(DataVariant("minecraft:stone")) })
            }
        }

        // ネザー
        blockOreSeedNether = block({ BlockOreSeed(EnumOreSeedType.NETHERRACK) }, "ore_seed_nether") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataModelBlockDefinition(variants = EnumVariantOreSeed.values().associate { "variant=$it" to DataSingleVariantList(DataVariant("minecraft:netherrack")) })
            }
        }

        // エンド
        blockOreSeedEnd = block({ BlockOreSeed(EnumOreSeedType.END_STONE) }, "ore_seed_end") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates(resourceName.path) {
                DataModelBlockDefinition(variants = EnumVariantOreSeed.values().associate { "variant=$it" to DataSingleVariantList(DataVariant("minecraft:end_stone")) })
            }
        }


        // 地形生成
        onHookDecorator {

            // 地上
            MinecraftForge.ORE_GEN_BUS.register(object : Any() {
                val worldGenCompound: WorldGenCompoundOreSeed = WorldGenCompoundOreSeed(blockOreSeed()) { it.block == Blocks.STONE && it.getValue(BlockStone.VARIANT).isNatural }

                @SubscribeEvent
                fun accept(event: OreGenEvent.Post) {
                    if (event.world.provider.isSurfaceWorld) {
                        worldGenCompound.accept(event.world, event.rand, event.pos)
                    }
                }
            })

            // ネザー
            MinecraftForge.EVENT_BUS.register(object : Any() {
                val worldGenCompound: WorldGenCompoundOreSeed = WorldGenCompoundOreSeed(blockOreSeedNether()) { it.block == Blocks.NETHERRACK }

                @SubscribeEvent
                fun accept(event: DecorateBiomeEvent.Post) {
                    if (BiomeDictionary.hasType(event.world.getBiome(event.chunkPos.getBlock(8, 0, 8)), BiomeDictionary.Type.NETHER)) {
                        worldGenCompound.accept(event.world, event.rand, event.chunkPos.getBlock(0, 0, 0))
                    }
                }
            })

            // エンド
            MinecraftForge.EVENT_BUS.register(object : Any() {
                val worldGenCompound: WorldGenCompoundOreSeed = WorldGenCompoundOreSeed(blockOreSeedEnd()) { it.block == Blocks.END_STONE }

                @SubscribeEvent
                fun accept(event: PopulateChunkEvent.Post) {
                    if (BiomeDictionary.hasType(event.world.getBiome(ChunkPos(event.chunkX, event.chunkZ).getBlock(8, 0, 8)), BiomeDictionary.Type.END)) {
                        worldGenCompound.accept(event.world, event.rand, ChunkPos(event.chunkX, event.chunkZ).getBlock(0, 0, 0))
                    }
                }
            })

        }


        // 鉱石の種ドロップ
        onInstantiation {
            ApiOreSeedDrop.oreSeedDropRegistry = OreSeedDropRegistry()

            ApiOreSeedDrop.oreSeedDropRegistry {

                /*
                 * 暗視
                 *   /effect @p minecraft:night_vision 99999 1
                 * 垂直　Z自分
                 *   /fill ~-120 10 ~ ~120 80 ~ minecraft:air
                 * 水平　Y自分
                 *   /fill ~-90 ~ ~-90 ~90 ~ ~90 minecraft:air
                 * 水平　Y自分　幅2マス　狭い
                 *   /fill ~-60 ~ ~-60 ~60 ~1 ~60 minecraft:air
                 * 水平　Y45
                 *   /fill ~-90 45 ~-90 ~90 45 ~90 minecraft:air
                 * 水平　Y11
                 *   /fill ~-90 11 ~-90 ~90 11 ~90 minecraft:air
                 */

                fun ore1(variant: EnumVariantOre1) = Pair({ Ores.blockOre1().getState(variant.blockVariant) }, { ItemStack(Ores.blockOre1(), 1, variant.blockVariant.metadata) })
                fun ore2(variant: EnumVariantOre2) = Pair({ Ores.blockOre2().getState(variant.blockVariant) }, { ItemStack(Ores.blockOre2(), 1, variant.blockVariant.metadata) })
                fun block(block: Block, meta: Int = 0) = Pair({ block.defaultState }, { ItemStack(block, 1, meta) })

                fun OreSeedDropRegistryScope.TypedOreSeedDropRegistryScope.vein(shape: EnumOreSeedShape, weight: Double, output: Pair<() -> IBlockState, () -> ItemStack>, vararg generationConditions: IOreSeedDropRequirement) {
                    registry.register(type, EnumOreSeedShape.POINT, weight / 2, output, *generationConditions)
                    registry.register(type, EnumOreSeedShape.TINY, weight / 2, output, *generationConditions)
                    registry.register(type, EnumOreSeedShape.LAPIS, weight / 2, output, *generationConditions)
                    registry.register(type, EnumOreSeedShape.DIAMOND, weight / 2, output, *generationConditions)
                    registry.register(type, EnumOreSeedShape.IRON, weight / 2, output, *generationConditions)
                    registry.register(type, EnumOreSeedShape.MEDIUM, weight / 2, output, *generationConditions)
                    registry.register(type, shape, weight, output, *generationConditions)
                }

                //

                // まばら天然石
                EnumOreSeedType.STONE {
                    register(EnumOreSeedShape.POINT, 0.05, ore1(EnumVariantOre1.APATITE_ORE))
                    register(EnumOreSeedShape.POINT, 0.05, ore1(EnumVariantOre1.FLUORITE_ORE))
                    register(EnumOreSeedShape.POINT, 0.05, ore1(EnumVariantOre1.SULFUR_ORE), OreSeedDropRequirements.maxY(15))
                    register(EnumOreSeedShape.POINT, 0.05, ore1(EnumVariantOre1.CINNABAR_ORE), OreSeedDropRequirements.maxY(15))
                    register(EnumOreSeedShape.POINT, 0.05, ore1(EnumVariantOre1.MAGNETITE_ORE))
                    register(EnumOreSeedShape.POINT, 0.05, ore1(EnumVariantOre1.MOONSTONE_ORE), OreSeedDropRequirements.minY(40), OreSeedDropRequirements.maxY(50))
                    register(EnumOreSeedShape.LAPIS, 0.10, ore2(EnumVariantOre2.PYRITE_ORE), OreSeedDropRequirements.since(OffsetDateTime.of(LocalDateTime.of(2022, 4, 1, 0, 0, 0), ZoneOffset.ofHours(9))))
                }

                // 鉱脈天然石
                EnumOreSeedType.STONE {
                    vein(EnumOreSeedShape.LARGE, 1.00, ore1(EnumVariantOre1.APATITE_ORE), Vein(97063327, 32, 8, 0.02, Elements.FLUORINE, Elements.CALCIUM, Elements.PHOSPHORUS))
                    vein(EnumOreSeedShape.PYRAMID, 1.00, ore1(EnumVariantOre1.FLUORITE_ORE), Vein(63503821, 32, 8, 0.02, Elements.FLUORINE, Elements.CALCIUM))
                    vein(EnumOreSeedShape.STAR, 1.00, ore1(EnumVariantOre1.SULFUR_ORE), Vein(34153177, 32, 8, 0.02, Elements.SULFUR))
                    vein(EnumOreSeedShape.POINT, 1.00, ore1(EnumVariantOre1.CINNABAR_ORE), Vein(27826567, 32, 8, 0.02, Elements.SULFUR, Elements.MERCURY))
                    vein(EnumOreSeedShape.COAL, 1.00, ore1(EnumVariantOre1.MAGNETITE_ORE), Vein(16287001, 64, 16, 0.02, Elements.FERRUM))
                    vein(EnumOreSeedShape.TINY, 1.00, ore1(EnumVariantOre1.MOONSTONE_ORE), Vein(78750461, 16, 4, 0.02, Elements.KALIUM, Elements.ALUMINIUM))
                }

                // 鉱脈宝石
                EnumOreSeedType.STONE {
                    vein(EnumOreSeedShape.POINT, 0.50, ore1(EnumVariantOre1.PYROPE_ORE), Vein(39250117, 16, 4, 0.01, Elements.MAGNESIUM, Elements.ALUMINIUM))
                    vein(EnumOreSeedShape.LARGE, 0.75, ore1(EnumVariantOre1.SMITHSONITE_ORE), Vein(32379601, 32, 8, 0.01, Elements.ZINC, Elements.CARBON))
                    vein(EnumOreSeedShape.MEDIUM, 0.75, ore1(EnumVariantOre1.NEPHRITE_ORE), Vein(50393467, 64, 16, 0.01, Elements.CALCIUM, Elements.MAGNESIUM, Elements.FERRUM))
                    vein(EnumOreSeedShape.HORIZONTAL, 0.50, ore1(EnumVariantOre1.TOPAZ_ORE), Vein(58068649, 16, 4, 0.01, Elements.ALUMINIUM, Elements.FLUORINE))
                    vein(EnumOreSeedShape.HORIZONTAL, 0.50, ore2(EnumVariantOre2.TOURMALINE_ORE), Vein(25988519, 16, 4, 0.01, Elements.NATRIUM, Elements.LITHIUM, Elements.ALUMINIUM, Elements.BORON))
                    vein(EnumOreSeedShape.TINY, 0.50, ore2(EnumVariantOre2.HELIOLITE_ORE), Vein(85462735, 16, 4, 0.01, Elements.CALCIUM, Elements.ALUMINIUM))
                    vein(EnumOreSeedShape.POINT, 0.50, block(Blocks.EMERALD_ORE), Vein(54693454, 16, 4, 0.02, Elements.BERYLLIUM, Elements.ALUMINIUM))
                    vein(EnumOreSeedShape.LAPIS, 0.50, block(Blocks.LAPIS_ORE), Vein(60410682, 32, 8, 0.005, Elements.NATRIUM, Elements.ALUMINIUM))
                    vein(EnumOreSeedShape.DIAMOND, 0.50, block(Blocks.DIAMOND_ORE), Vein(20741887, 8, 2, 0.003, Elements.CARBON))
                    vein(EnumOreSeedShape.COAL, 0.50, block(Blocks.COAL_ORE), Vein(25197329, 64, 16, 0.003, Elements.CARBON))
                    vein(EnumOreSeedShape.DIAMOND, 0.50, block(Blocks.REDSTONE_ORE), Vein(95298700, 64, 16, 0.003, Elements.FERRUM, Elements.CUPRUM, Elements.ALUMINIUM, Elements.MERCURY))
                    vein(EnumOreSeedShape.IRON, 0.50, block(Blocks.IRON_ORE), Vein(34443884, 64, 16, 0.003, Elements.FERRUM))
                    vein(EnumOreSeedShape.IRON, 0.50, block(Blocks.GOLD_ORE), Vein(93307749, 16, 4, 0.003, Elements.AURUM))
                }
                EnumOreSeedType.END_STONE {
                    vein(EnumOreSeedShape.TINY, 0.50, ore2(EnumVariantOre2.END_STONE_LABRADORITE_ORE), Vein(61584972, 16, 256, 0.1, Elements.CALCIUM, Elements.ALUMINIUM, Elements.NATRIUM))
                }

                // ネザー鉱石
                EnumOreSeedType.NETHERRACK {
                    register(EnumOreSeedShape.LARGE, 0.10, ore1(EnumVariantOre1.NETHERRACK_APATITE_ORE), OreSeedDropRequirements.minY(90))
                    register(EnumOreSeedShape.PYRAMID, 0.10, ore1(EnumVariantOre1.NETHERRACK_FLUORITE_ORE), OreSeedDropRequirements.minY(90))
                    register(EnumOreSeedShape.TINY, 0.30, ore1(EnumVariantOre1.NETHERRACK_SULFUR_ORE), OreSeedDropRequirements.minY(20), OreSeedDropRequirements.maxY(40))
                    register(EnumOreSeedShape.IRON, 0.10, ore1(EnumVariantOre1.NETHERRACK_CINNABAR_ORE))
                    register(EnumOreSeedShape.COAL, 0.10, ore1(EnumVariantOre1.NETHERRACK_MAGNETITE_ORE))
                    register(EnumOreSeedShape.TINY, 0.10, ore1(EnumVariantOre1.NETHERRACK_MOONSTONE_ORE), OreSeedDropRequirements.maxY(32))
                }

            }

        }

    }
}
