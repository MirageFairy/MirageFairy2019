package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleOreSeed {

    public static void init(EventRegistryMod erMod) {
        erMod.hookDecorator.register(() -> {

            // 地形生成
            MinecraftForge.ORE_GEN_BUS.register(new Object() {
                private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(OreSeed.blockOreSeed.invoke(), blockState -> {
                    if (blockState == null) return false;
                    if (blockState.getBlock() != Blocks.STONE) return false;
                    if (!blockState.getValue(BlockStone.VARIANT).isNatural()) return false;
                    return true;
                });

                @SubscribeEvent
                public void accept(OreGenEvent.Post event) {
                    if (event.getWorld().provider.isSurfaceWorld()) {
                        worldGenCompound.accept(event.getWorld(), event.getRand(), event.getPos());
                    }
                }
            });

            // 地形生成：ネザー
            MinecraftForge.EVENT_BUS.register(new Object() {
                private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(OreSeed.blockOreSeedNether.invoke(), blockState -> {
                    if (blockState == null) return false;
                    if (blockState.getBlock() != Blocks.NETHERRACK) return false;
                    return true;
                });

                @SubscribeEvent
                public void accept(DecorateBiomeEvent.Post event) {
                    if (BiomeDictionary.hasType(event.getWorld().getBiome(event.getChunkPos().getBlock(8, 0, 8)), BiomeDictionary.Type.NETHER)) {
                        worldGenCompound.accept(event.getWorld(), event.getRand(), event.getChunkPos().getBlock(0, 0, 0));
                    }
                }
            });

            // 地形生成：エンド
            MinecraftForge.EVENT_BUS.register(new Object() {
                private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(OreSeed.blockOreSeedEnd.invoke(), blockState -> {
                    if (blockState == null) return false;
                    if (blockState.getBlock() != Blocks.END_STONE) return false;
                    return true;
                });

                @SubscribeEvent
                public void accept(PopulateChunkEvent.Post event) {
                    if (BiomeDictionary.hasType(event.getWorld().getBiome(new ChunkPos(event.getChunkX(), event.getChunkZ()).getBlock(8, 0, 8)), BiomeDictionary.Type.END)) {
                        worldGenCompound.accept(event.getWorld(), event.getRand(), new ChunkPos(event.getChunkX(), event.getChunkZ()).getBlock(0, 0, 0));
                    }
                }
            });

        });
    }

}
