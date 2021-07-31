package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.modkt.api.oreseeddrop.EnumOreSeedType;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleOreSeed {

    public static BlockOreSeed blockOreSeed;
    public static BlockOreSeed blockOreSeedNether;
    public static BlockOreSeed blockOreSeedEnd;

    public static void init(EventRegistryMod erMod) {
        erMod.registerBlock.register(b -> {

            // 鉱石の種
            blockOreSeed = new BlockOreSeed(EnumOreSeedType.STONE);
            blockOreSeed.setRegistryName(ModMirageFairy2019.MODID, "ore_seed");
            blockOreSeed.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockOreSeed);

            // 鉱石の種：ネザー
            blockOreSeedNether = new BlockOreSeed(EnumOreSeedType.NETHERRACK);
            blockOreSeedNether.setRegistryName(ModMirageFairy2019.MODID, "ore_seed_nether");
            blockOreSeedNether.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockOreSeedNether);

            // 鉱石の種：エンド
            blockOreSeedEnd = new BlockOreSeed(EnumOreSeedType.END_STONE);
            blockOreSeedEnd.setRegistryName(ModMirageFairy2019.MODID, "ore_seed_end");
            blockOreSeedEnd.setCreativeTab(ApiMain.creativeTab());
            ForgeRegistries.BLOCKS.register(blockOreSeedEnd);

        });
        erMod.hookDecorator.register(() -> {

            // 地形生成
            MinecraftForge.ORE_GEN_BUS.register(new Object() {
                private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(blockOreSeed, blockState -> {
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
                private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(blockOreSeedNether, blockState -> {
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
                private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed(blockOreSeedEnd, blockState -> {
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
