package miragefairy2019.mod3.artifacts.oreseed;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.Random;

public class BlockOreSeed extends Block {

    private EnumOreSeedType type;

    public BlockOreSeed(EnumOreSeedType type) {
        super(Material.ROCK);
        this.type = type;

        // meta
        setDefaultState(blockState.getBaseState()
            .withProperty(VARIANT, EnumVariantOreSeed.TINY));

        // style
        setSoundType(SoundType.STONE);

        // 挙動
        setHardness(1.5f);
        setResistance(10.0f);
        setTickRandomly(true);
        setHarvestLevel("pickaxe", 0);

    }

    //

    public static final PropertyEnum<EnumVariantOreSeed> VARIANT = PropertyEnum.create("variant", EnumVariantOreSeed.class);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    public IBlockState getState(EnumVariantOreSeed variant) {
        return getDefaultState().withProperty(VARIANT, variant);
    }

    public EnumVariantOreSeed getVariant(IBlockState blockState) {
        return blockState.getValue(VARIANT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, EnumVariantOreSeed.Companion.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        return blockState.getValue(VARIANT).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> itemStacks) {
        for (EnumVariantOreSeed variant : EnumVariantOreSeed.values()) {
            itemStacks.add(new ItemStack(this, 1, variant.getMetadata()));
        }
    }

    //

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    //

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) update(world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) update(worldIn, pos);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        super.onBlockClicked(worldIn, pos, playerIn);
        if (!worldIn.isRemote) update(worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) update(worldIn, pos);
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    //

    protected void update(World world, BlockPos pos) {
        if (shouldMutate(world, pos)) {
            mutate(world, pos);
        }
    }

    protected boolean shouldMutate(IBlockAccess world, BlockPos pos) {
        if (!world.getBlockState(pos.up()).isSideSolid(world, pos.up(), EnumFacing.DOWN)) return true;
        if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP)) return true;
        if (!world.getBlockState(pos.west()).isSideSolid(world, pos.west(), EnumFacing.EAST)) return true;
        if (!world.getBlockState(pos.east()).isSideSolid(world, pos.east(), EnumFacing.WEST)) return true;
        if (!world.getBlockState(pos.north()).isSideSolid(world, pos.north(), EnumFacing.SOUTH)) return true;
        if (!world.getBlockState(pos.south()).isSideSolid(world, pos.south(), EnumFacing.NORTH)) return true;
        return false;
    }

    protected void mutate(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Random random = new Random(pos.getX() * 15946848L + pos.getY() * 29135678L + pos.getZ() * 65726816L);
        IBlockState blockStateAfter = Optional.ofNullable(ApiOreSeedDrop.oreSeedDropRegistry.drop(
            new OreSeedDropEnvironment(type,
                getVariant(state).getShape(),
                world,
                pos),
            random)).orElseGet(() -> type.getBlockState());

        Deque<BlockPos> poses = new ArrayDeque<>();
        poses.addLast(pos);
        int t = 4096;
        while (!poses.isEmpty()) {
            BlockPos pos2 = poses.removeFirst();

            world.setBlockState(pos2, blockStateAfter, 2);
            if (world.getBlockState(pos2.up()).equals(state)) poses.addLast(pos2.up());
            if (world.getBlockState(pos2.down()).equals(state)) poses.addLast(pos2.down());
            if (world.getBlockState(pos2.west()).equals(state)) poses.addLast(pos2.west());
            if (world.getBlockState(pos2.east()).equals(state)) poses.addLast(pos2.east());
            if (world.getBlockState(pos2.north()).equals(state)) poses.addLast(pos2.north());
            if (world.getBlockState(pos2.south()).equals(state)) poses.addLast(pos2.south());

            t--;
            if (t <= 0) break;
        }
    }

}
