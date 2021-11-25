package miragefairy2019.mod3.mirageflower;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropRegistry;
import miragefairy2019.mod.common.pickable.PickableRegistry;
import miragefairy2019.mod.lib.BiomeDecoratorFlowers;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.WorldGenBush;
import miragefairy2019.mod3.main.api.ApiMain;
import miragefairy2019.mod3.mirageflower.api.ApiMirageFlower;
import mirrg.boron.util.UtilsLambda;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModuleMirageFlower {

    public static void init(EventRegistryMod erMod) {
        initFairyLog(erMod);
        initMirageFlower(erMod);
    }

    //

    private static BlockFairyLog blockFairyLog;
    private static ItemBlock itemBlockFairyLog;

    private static void initFairyLog(EventRegistryMod erMod) {

        // レジストリ
        erMod.initRegistry.register(() -> {
            ApiMirageFlower.fairyLogDropRegistry = new FairyLogDropRegistry();
        });

        // ブロック
        erMod.registerBlock.register(b -> {
            blockFairyLog = new BlockFairyLog();
            blockFairyLog.setRegistryName(ModMirageFairy2019.MODID, "fairy_log");
            blockFairyLog.setUnlocalizedName("fairyLog");
            blockFairyLog.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockFairyLog);
            ApiMirageFlower.blockFairyLog = blockFairyLog;
        });

        // アイテム
        erMod.registerItem.register(b -> {
            itemBlockFairyLog = new ItemBlock(blockFairyLog);
            itemBlockFairyLog.setRegistryName(ModMirageFairy2019.MODID, "fairy_log");
            itemBlockFairyLog.setUnlocalizedName("fairyLog");
            itemBlockFairyLog.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemBlockFairyLog);
            ApiMirageFlower.itemBlockFairyLog = itemBlockFairyLog;
            if (ApiMain.side.isClient()) {
                ModelLoader.setCustomModelResourceLocation(itemBlockFairyLog, 0, new ModelResourceLocation(itemBlockFairyLog.getRegistryName(), "facing=north,variant=oak"));
            }
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
                        IBlockState blockStateFairyLog = blockFairyLog.getState(facing);
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

    private static BlockMirageFlower blockMirageFlower;
    private static ItemMirageFlowerSeeds<BlockMirageFlower> itemMirageFlowerSeeds;

    private static void initMirageFlower(EventRegistryMod erMod) {

        // レジストリ
        erMod.initRegistry.register(() -> {
            ApiMirageFlower.pickableRegistry = new PickableRegistry();
        });

        // ブロック
        erMod.registerBlock.register(b -> {
            blockMirageFlower = new BlockMirageFlower();
            blockMirageFlower.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower");
            blockMirageFlower.setUnlocalizedName("mirageFlower");
            blockMirageFlower.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.BLOCKS.register(blockMirageFlower);
            ApiMirageFlower.blockMirageFlower = blockMirageFlower;
            ApiMirageFlower.pickableRegistry.register(blockMirageFlower, blockMirageFlower.getPickable());
        });

        // 種アイテム
        erMod.registerItem.register(b -> {
            itemMirageFlowerSeeds = new ItemMirageFlowerSeeds<>(blockMirageFlower);
            itemMirageFlowerSeeds.setRegistryName(ModMirageFairy2019.MODID, "mirage_flower_seeds");
            itemMirageFlowerSeeds.setUnlocalizedName("mirageFlowerSeeds");
            itemMirageFlowerSeeds.setCreativeTab(ApiMain.creativeTab);
            ForgeRegistries.ITEMS.register(itemMirageFlowerSeeds);
            ApiMirageFlower.itemMirageFlowerSeeds = itemMirageFlowerSeeds;
            if (ApiMain.side.isClient()) {
                ModelLoader.setCustomModelResourceLocation(itemMirageFlowerSeeds, 0, new ModelResourceLocation(itemMirageFlowerSeeds.getRegistryName(), null));
            }
        });

        // 雑草が種をドロップ
        erMod.addRecipe.register(() -> {
            MinecraftForge.addGrassSeed(new ItemStack(ApiMirageFlower.itemMirageFlowerSeeds), 1);
        });

        // 地形生成
        erMod.hookDecorator.register(() -> {
            List<BiomeDecoratorFlowers> biomeDecorators = new ArrayList<>();
            {
                biomeDecorators.add(new BiomeDecoratorFlowers(
                        UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), wg -> {
                            wg.blockCountMin = 1;
                            wg.blockCountMax = 3;
                        }),
                        0.01));

                biomeDecorators.add(new BiomeDecoratorFlowers(
                        UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), wg -> {
                            wg.blockCountMin = 1;
                            wg.blockCountMax = 10;
                        }),
                        0.1) {
                    @Override
                    protected boolean canGenerate(Biome biome) {
                        return super.canGenerate(biome) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN);
                    }
                });

                biomeDecorators.add(new BiomeDecoratorFlowers(
                        UtilsLambda.get(new WorldGenBush(blockMirageFlower, blockMirageFlower.getState(3)), wg -> {
                            wg.blockCountMin = 1;
                            wg.blockCountMax = 10;
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
