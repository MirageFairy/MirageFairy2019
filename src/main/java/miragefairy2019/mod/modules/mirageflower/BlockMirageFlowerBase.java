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
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class BlockMirageFlowerBase extends BlockBush implements IGrowable {

    public BlockMirageFlowerBase(Material material) {
        super(material);
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
