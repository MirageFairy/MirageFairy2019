package miragefairy2019.mod3.worldgen;

import miragefairy2019.mod.lib.BiomeDecoratorFlowers;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.WorldGenBush;
import miragefairy2019.mod3.fairylogdrop.FairyLogDropRegistry;
import miragefairy2019.mod3.worldgen.api.ApiWorldGen;
import mirrg.boron.util.UtilsLambda;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ModuleMirageFlower {

    public static void init(EventRegistryMod erMod) {
        initFairyLog(erMod);
        initMirageFlower(erMod);
    }

    //

    private static void initFairyLog(EventRegistryMod erMod) {

        // レジストリ
        erMod.initRegistry.register(() -> {
            ApiWorldGen.fairyLogDropRegistry = new FairyLogDropRegistry();
        });

        // 地形生成
        erMod.hookDecorator.register(() -> {
            MinecraftForge.EVENT_BUS.register(new Object() {
                @SubscribeEvent
                public void init(DecorateBiomeEvent.Post event) {
                    int count = UtilsMath.randomInt(event.getRand(), (event.getWorld().getHeight() / 16.0) * 50); // TODO
                    for (int i = 0; i < count; i++) {
                        EnumFacing facing = EnumFacing.HORIZONTALS[event.getRand().nextInt(4)];
                        BlockPos posLog = event.getChunkPos().getBlock(
                            event.getRand().nextInt(16) + 8,
                            event.getRand().nextInt(event.getWorld().getHeight()),
                            event.getRand().nextInt(16) + 8);
                        BlockPos posAir = posLog.offset(facing);
                        IBlockState blockStateLog = event.getWorld().getBlockState(posLog);
                        IBlockState blockStateAir = event.getWorld().getBlockState(posAir);
                        IBlockState blockStateFairyLog = FairyLog.blockFairyLog.invoke().getState(facing);
                        if (!blockStateAir.isSideSolid(event.getWorld(), posAir, facing.getOpposite())) {
                            if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK))) {
                                event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
                            } else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE))) {
                                event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
                            } else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH))) {
                                event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
                            } else if (blockStateLog.equals(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE))) {
                                event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
                            } else if (blockStateLog.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA))) {
                                event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
                            } else if (blockStateLog.equals(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK))) {
                                event.getWorld().setBlockState(posLog, blockStateFairyLog, 2);
                            }
                        }
                    }
                }
            });
        });

    }

    //

    private static void initMirageFlower(EventRegistryMod erMod) {

        // 雑草が種をドロップ
        erMod.addRecipe.register(() -> {
            MinecraftForge.addGrassSeed(new ItemStack(MirageFlower.itemMirageFlowerSeeds.invoke()), 1);
        });

        // 地形生成
        erMod.hookDecorator.register(() -> {
            List<BiomeDecoratorFlowers> biomeDecorators = new ArrayList<>();
            {
                biomeDecorators.add(new BiomeDecoratorFlowers(
                    UtilsLambda.get(new WorldGenBush(MirageFlower.blockMirageFlower.invoke(), MirageFlower.blockMirageFlower.invoke().getState(3)), wg -> {
                        wg.setBlockCountMin(1);
                        wg.setBlockCountMax(3);
                    }),
                    0.01));

                biomeDecorators.add(new BiomeDecoratorFlowers(
                    UtilsLambda.get(new WorldGenBush(MirageFlower.blockMirageFlower.invoke(), MirageFlower.blockMirageFlower.invoke().getState(3)), wg -> {
                        wg.setBlockCountMin(1);
                        wg.setBlockCountMax(10);
                    }),
                    0.1) {
                    @Override
                    protected boolean canGenerate(Biome biome) {
                        return super.canGenerate(biome) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN);
                    }
                });

                biomeDecorators.add(new BiomeDecoratorFlowers(
                    UtilsLambda.get(new WorldGenBush(MirageFlower.blockMirageFlower.invoke(), MirageFlower.blockMirageFlower.invoke().getState(3)), wg -> {
                        wg.setBlockCountMin(1);
                        wg.setBlockCountMax(10);
                    }),
                    0.5) {
                    @Override
                    protected boolean canGenerate(Biome biome) {
                        return super.canGenerate(biome) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST);
                    }
                });
            }
            MinecraftForge.EVENT_BUS.register(new Object() {
                @SubscribeEvent
                public void accept(DecorateBiomeEvent.Post event) {
                    for (BiomeDecoratorFlowers biomeDecorator : biomeDecorators) {
                        biomeDecorator.decorate(event);
                    }
                }
            });
        });

    }

}
