package miragefairy2019.mod.modules.mirageflower;

import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.api.fairy.registry.ApiFairyRegistry;
import miragefairy2019.mod.api.pickable.IPickable;
import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import miragefairy2019.mod.modules.materialsfairy.ModuleMaterialsFairy;
import miragefairy2019.mod.modules.ore.ModuleOre;
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Optional;
import java.util.Random;

public class BlockMirageFlowerBase extends BlockBush implements IGrowable {

    public static ITextComponent getGrowRateTableMessage() {
        ITextComponent textComponent = new TextComponentString("");
        textComponent.appendText("===== Mirage Flower Grow Rate Table =====\n");
        ApiFairyRegistry.getFairyRegistry().getFairies()
                .map(r -> Tuple.of(r.getFairyType(), BlockMirageFlowerKt.getGrowRateInFloor(r.getFairyType())))
                .filter(t -> t.y > 1)
                .sortedDouble(Tuple::getY)
                .forEach(t -> {
                    textComponent.appendText(String.format("%7.2f%%  ", t.y * 100));
                    textComponent.appendSibling(t.x.getDisplayName());
                    textComponent.appendText("\n");
                });
        textComponent.appendText("====================");
        return textComponent;
    }

    public static ITextComponent getGrowRateMessage(World world, BlockPos pos) {
        ITextComponent textComponent = new TextComponentString("");
        textComponent.appendText("===== Mirage Flower Grow Rate =====\n");
        textComponent.appendText(String.format("Pos: %d %d %d\n", pos.getX(), pos.getY(), pos.getZ()));
        textComponent.appendText(String.format("Block: %s\n", world.getBlockState(pos)));
        textComponent.appendText(String.format("Floor: %s\n", world.getBlockState(pos.down())));
        textComponent.appendText(String.format("%.2f%%\n", getGrowRate(world, pos) * 100));
        textComponent.appendText("====================");
        return textComponent;
    }

    //

    public BlockMirageFlowerBase() {
        super(Material.PLANTS); // Solidでないマテリアルでなければ耕土の上に置けない

        // meta
        setDefaultState(blockState.getBaseState()
                .withProperty(AGE, 0));

        // style
        setSoundType(SoundType.GLASS);

    }

    // state

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);

    @Override
    public int getMetaFromState(IBlockState state) {
        return UtilsMath.trim(state.getValue(AGE), 0, 3);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AGE, UtilsMath.trim(meta, 0, 3));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    public IBlockState getState(int age) {
        return getDefaultState().withProperty(AGE, age);
    }

    // 判定

    public static final AxisAlignedBB AABB_STAGE0 = new AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0);
    public static final AxisAlignedBB AABB_STAGE1 = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0);
    public static final AxisAlignedBB AABB_STAGE2 = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0);
    public static final AxisAlignedBB AABB_STAGE3 = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int age = getAge(state);
        if (age == 0) return AABB_STAGE0;
        if (age == 1) return AABB_STAGE1;
        if (age == 2) return AABB_STAGE2;
        if (age == 3) return AABB_STAGE3;
        return AABB_STAGE3;
    }

    // 動作

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.isFullBlock() || state.getBlock() == Blocks.FARMLAND;
    }

    public int getAge(IBlockState state) {
        return state.getValue(AGE);
    }

    public boolean isMaxAge(IBlockState state) {
        return getAge(state) == 3;
    }

    protected void grow(World worldIn, BlockPos pos, IBlockState state, Random rand, double rate) {
        int t = UtilsMath.randomInt(rand, rate);
        for (int i = 0; i < t; i++) {
            if (!isMaxAge(state)) {
                worldIn.setBlockState(pos, getDefaultState().withProperty(AGE, getAge(state) + 1), 2);
            }
        }
    }

    /**
     * UpdateTickごとにAgeが1ずつ最大3まで増える。
     */
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (!worldIn.isAreaLoaded(pos, 1)) return;

        grow(worldIn, pos, state, rand, getGrowRate(worldIn, pos));
    }

    @SuppressWarnings("deprecation")
    public static double getGrowRate(World world, BlockPos blockPos) {
        double rate = 0.04;

        // 人工光が当たっているなら加点
        if (world.getLightFor(EnumSkyBlock.BLOCK, blockPos) >= 13) rate *= 1.2;
        else if (world.getLightFor(EnumSkyBlock.BLOCK, blockPos) >= 9) rate *= 1.1;

        // 太陽光が当たっているなら加点
        if (world.getLightFor(EnumSkyBlock.SKY, blockPos) >= 15) rate *= 1.1;
        else if (world.getLightFor(EnumSkyBlock.SKY, blockPos) >= 9) rate *= 1.05;

        // 空が見えるなら加点
        if (world.canSeeSky(blockPos)) rate *= 1.1;

        // 地面加点
        {
            double bonus = 0.5;

            if (world.getBlockState(blockPos.down()).getBlock() == Blocks.GRASS) bonus = Math.max(bonus, 1);

            if (world.getBlockState(blockPos.down()).getBlock() == Blocks.DIRT) bonus = Math.max(bonus, 1.1);

            if (world.getBlockState(blockPos.down()).getBlock() == Blocks.FARMLAND) {
                bonus = Math.max(bonus, 1.2);

                // 耕土が湿っているなら加点
                if (world.getBlockState(blockPos.down()).getValue(BlockFarmland.MOISTURE) > 0) bonus = Math.max(bonus, 1.3);

            }

            // 妖精による判定
            {
                IBlockState blockState = world.getBlockState(blockPos.down());
                ItemStack itemStack = blockState.getBlock().getItem(world, blockPos, blockState);

                Double value = ApiFairyRegistry.getFairyRelationRegistry().fairySelector()
                        .add(blockState)
                        .add(itemStack)
                        .select()
                        .mapIfPresent(n -> ApiFairyRegistry.getFairyRegistry().getFairy(n))
                        .map(r -> BlockMirageFlowerKt.getGrowRateInFloor(r.getFairyType()))
                        .max(Double::compare)
                        .orElse(null);
                if (value != null) {
                    bonus = Math.max(bonus, value);
                }
            }

            if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.APATITE_BLOCK)) bonus = Math.max(bonus, 1.5);
            if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.FLUORITE_BLOCK)) bonus = Math.max(bonus, 2);
            if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.SULFUR_BLOCK)) bonus = Math.max(bonus, 1.5);
            if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.CINNABAR_BLOCK)) bonus = Math.max(bonus, 2);
            if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MOONSTONE_BLOCK)) bonus = Math.max(bonus, 3);
            if (world.getBlockState(blockPos.down()) == ModuleOre.blockMaterials1.getState(EnumVariantMaterials1.MAGNETITE_BLOCK)) bonus = Math.max(bonus, 1.2);

            rate *= bonus;
        }

        // バイオーム加点
        {
            double bonus = 1;

            if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.PLAINS)) bonus = Math.max(bonus, 1.1);
            if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.SWAMP)) bonus = Math.max(bonus, 1.1);
            if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.MOUNTAIN)) bonus = Math.max(bonus, 1.2);
            if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.JUNGLE)) bonus = Math.max(bonus, 1.2);
            if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.FOREST)) bonus = Math.max(bonus, 1.3);
            if (BiomeDictionary.hasType(world.getBiome(blockPos), BiomeDictionary.Type.MAGICAL)) bonus = Math.max(bonus, 1.3);

            rate *= bonus;
        }

        return rate;
    }

    /**
     * 骨粉をやると低確率で成長する。
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        grow(worldIn, pos, state, rand, getGrowRate(worldIn, pos));
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !isMaxAge(state);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return worldIn.rand.nextFloat() < 0.05;
    }

    // ドロップ

    /**
     * クリエイティブピックでの取得アイテム。
     */
    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(ApiMirageFlower.itemMirageFlowerSeeds);
    }

    /**
     * Ageが最大のとき、種を1個ドロップする。
     * 幸運Lv1につき種のドロップ数が1%増える。
     * 地面破壊ドロップでも適用される。
     */
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        getDrops(drops, world, pos, state, fortune, true);
    }

    private void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune, boolean isBreaking) {
        Random random = world instanceof World ? ((World) world).rand : new Random();

        // 種1個は確定でドロップ
        if (isBreaking) {
            drops.add(new ItemStack(ApiMirageFlower.itemMirageFlowerSeeds));
        }

        // サイズが2以上なら確定で茎をドロップ
        if (isBreaking) {
            if (getAge(state) >= 2) {
                int count = UtilsMath.randomInt(random, 1 + fortune * 0.2);
                for (int i = 0; i < count; i++) {
                    drops.add(ModuleMaterialsFairy.itemStackLeafMirageFlower.copy());
                }
            }
        }

        // 追加の種
        if (getAge(state) >= 3) {
            int count = UtilsMath.randomInt(random, fortune * 0.01);
            for (int i = 0; i < count; i++) {
                drops.add(new ItemStack(ApiMirageFlower.itemMirageFlowerSeeds));
            }
        }

        // クリスタル
        if (getAge(state) >= 3) {
            int count = UtilsMath.randomInt(random, 1 + fortune * 0.5);
            for (int i = 0; i < count; i++) {
                drops.add(ModuleFairyCrystal.variantFairyCrystal.createItemStack());
            }
        }

        // ミラジウム
        if (getAge(state) >= 3) {
            int count = UtilsMath.randomInt(random, 1 + fortune * 0.5);
            for (int i = 0; i < count; i++) {
                drops.add(UtilsMinecraft.getItemStack("dustTinyMiragium").copy());
            }
        }

    }

    /**
     * シルクタッチ無効。
     */
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    // 経験値ドロップ

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return getExpDrop(state, world, pos, fortune, true);
    }

    private int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune, boolean isBreaking) {
        if (isBreaking) {
            if (getAge(state) >= 3) return 2;
            if (getAge(state) >= 2) return 1;
            return 0;
        } else {
            if (getAge(state) >= 3) return 1;
            return 0;
        }
    }

    // Pickable関連

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = playerIn.getHeldItemMainhand();
        itemStack = itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy();
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack);

        return getPickable().tryPick(worldIn, pos, Optional.of(playerIn), fortune);
    }

    public IPickable getPickable() {
        return new IPickable() {
            @Override
            public Block getBlock() {
                return BlockMirageFlowerBase.this;
            }

            @Override
            public boolean isPickableAge(IBlockState blockState) {
                return getAge(blockState) == 3;
            }

            @Override
            public boolean tryPick(World world, BlockPos blockPos, Optional<EntityPlayer> oPlayer, int fortune) {
                IBlockState blockState = world.getBlockState(blockPos);

                // 最大サイズでないなら失敗
                if (!isPickableAge(blockState)) return false;

                // 収穫物計算
                NonNullList<ItemStack> drops = NonNullList.create();
                getDrops(drops, world, blockPos, blockState, fortune, false);

                // 収穫物生成
                for (ItemStack drop : drops) {
                    Block.spawnAsEntity(world, blockPos, drop);
                }

                // 経験値生成
                blockState.getBlock().dropXpOnBlockBreak(world, blockPos, getExpDrop(blockState, world, blockPos, fortune, false));

                // エフェクト
                world.playEvent(oPlayer.orElse(null), 2001, blockPos, Block.getStateId(blockState));

                // ブロックの置換
                world.setBlockState(blockPos, getDefaultState().withProperty(AGE, 1), 2);

                return true;
            }
        };
    }

}
