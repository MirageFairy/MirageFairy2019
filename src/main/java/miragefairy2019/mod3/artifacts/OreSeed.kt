package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.block
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.mod3.artifacts.oreseed.ApiOreSeedDrop
import miragefairy2019.mod3.artifacts.oreseed.BlockOreSeed
import miragefairy2019.mod3.artifacts.oreseed.EnumOreSeedType
import miragefairy2019.mod3.artifacts.oreseed.EnumVariantOreSeed
import miragefairy2019.mod3.artifacts.oreseed.LoaderOreSeedDrop
import miragefairy2019.mod3.artifacts.oreseed.OreSeedDropRegistry
import miragefairy2019.mod3.artifacts.oreseed.WorldGenCompoundOreSeed
import miragefairy2019.mod.Main
import net.minecraft.block.BlockStone
import net.minecraft.init.Blocks
import net.minecraft.util.math.ChunkPos
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.DecorateBiomeEvent
import net.minecraftforge.event.terraingen.OreGenEvent
import net.minecraftforge.event.terraingen.PopulateChunkEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object OreSeed {
    lateinit var blockOreSeed: () -> BlockOreSeed
    lateinit var blockOreSeedNether: () -> BlockOreSeed
    lateinit var blockOreSeedEnd: () -> BlockOreSeed
    val module = module {

        // 鉱石の種

        // 地上
        blockOreSeed = block({ BlockOreSeed(EnumOreSeedType.STONE) }, "ore_seed") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates { DataBlockStates(variants = EnumVariantOreSeed.values().associate { "variant=$it" to DataBlockState("minecraft:stone") }) }
        }

        // ネザー
        blockOreSeedNether = block({ BlockOreSeed(EnumOreSeedType.NETHERRACK) }, "ore_seed_nether") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates { DataBlockStates(variants = EnumVariantOreSeed.values().associate { "variant=$it" to DataBlockState("minecraft:netherrack") }) }
        }

        // エンド
        blockOreSeedEnd = block({ BlockOreSeed(EnumOreSeedType.END_STONE) }, "ore_seed_end") {
            setCreativeTab { Main.creativeTab }
            makeBlockStates { DataBlockStates(variants = EnumVariantOreSeed.values().associate { "variant=$it" to DataBlockState("minecraft:end_stone") }) }
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
            LoaderOreSeedDrop.loadOreSeedDrop()
        }

    }
}
