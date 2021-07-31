package miragefairy2019.mod.modules.placeditem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockPlacedItem extends BlockContainer {

    public BlockPlacedItem() {
        super(Material.CIRCUITS);

        // style
        setSoundType(SoundType.WOOD);

        // 挙動
        setHardness(0.5f);
        setHarvestLevel("shovel", 0);

    }

    //

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPlacedItem();
    }

    // ドロップ

    /**
     * クリエイティブピックでの取得アイテム。
     */
    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityPlacedItem) {
            return ((TileEntityPlacedItem) tileEntity).getItemStack();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {

    }

    /**
     * 破壊時ドロップ
     */
    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityPlacedItem) {
            InventoryHelper.spawnItemStack(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), ((TileEntityPlacedItem) tileEntity).getItemStack());
            world.updateComparatorOutputLevel(blockPos, this);
        }
        super.breakBlock(world, blockPos, blockState);
    }

    /**
     * シルクタッチ無効。
     */
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    //

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    //

    public static final AxisAlignedBB AABB = new AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 2 / 16.0, 14 / 16.0);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    //

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return canSustain(worldIn, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        checkForDrop(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        checkForDrop(worldIn, pos, state);
    }

    private void checkForDrop(World world, BlockPos blockPos, IBlockState blockState) {
        if (blockState.getBlock() != this) return;
        if (!canSustain(world, blockPos)) {
            if (world.getBlockState(blockPos).getBlock() == this) {
                dropBlockAsItem(world, blockPos, blockState, 0);
                world.setBlockToAir(blockPos);
            }
        }
    }

    private boolean canSustain(IBlockAccess world, BlockPos blockPos) {
        return world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), EnumFacing.UP);
    }

    //

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityPlacedItem) {
            if (playerIn.isSneaking()) {
                ((TileEntityPlacedItem) tileEntity).rotation = playerIn.rotationYawHead;
            } else {
                ((TileEntityPlacedItem) tileEntity).action();
            }
            tileEntity.markDirty();
            ((TileEntityPlacedItem) tileEntity).sendUpdatePacket();
        }

        return true;
    }
}
