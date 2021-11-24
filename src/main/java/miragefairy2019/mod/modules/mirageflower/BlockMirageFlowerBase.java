package miragefairy2019.mod.modules.mirageflower;

import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.api.pickable.IPickable;
import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import miragefairy2019.mod.modules.materialsfairy.ModuleMaterialsFairy;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class BlockMirageFlowerBase extends BlockBush implements IGrowable {

    public BlockMirageFlowerBase(Material material) {
        super(material);
    }

    // 判定

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        int age = getAge(state);
        if (age == 0) return BlockMirageFlower.Companion.getAABB_STAGE0();
        if (age == 1) return BlockMirageFlower.Companion.getAABB_STAGE1();
        if (age == 2) return BlockMirageFlower.Companion.getAABB_STAGE2();
        if (age == 3) return BlockMirageFlower.Companion.getAABB_STAGE3();
        return BlockMirageFlower.Companion.getAABB_STAGE3();
    }

    // 動作

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.isFullBlock() || state.getBlock() == Blocks.FARMLAND;
    }

    public int getAge(IBlockState state) {
        return state.getValue(BlockMirageFlower.Companion.getAGE());
    }

    public boolean isMaxAge(IBlockState state) {
        return getAge(state) == 3;
    }

    protected void grow(World worldIn, BlockPos pos, IBlockState state, Random rand, double rate) {
        int t = UtilsMath.randomInt(rand, rate);
        for (int i = 0; i < t; i++) {
            if (!isMaxAge(state)) {
                worldIn.setBlockState(pos, getDefaultState().withProperty(BlockMirageFlower.Companion.getAGE(), getAge(state) + 1), 2);
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

        grow(worldIn, pos, state, rand, BlockMirageFlowerKt.getGrowRate(worldIn, pos));
    }

    /**
     * 骨粉をやると低確率で成長する。
     */
    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        grow(worldIn, pos, state, rand, BlockMirageFlowerKt.getGrowRate(worldIn, pos));
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
                world.setBlockState(blockPos, getDefaultState().withProperty(BlockMirageFlower.Companion.getAGE(), 1), 2);

                return true;
            }
        };
    }

}
