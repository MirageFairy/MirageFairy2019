package miragefairy2019.mod.lib.multi;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMulti<V extends IBlockVariant> extends Block {

    public final IBlockVariantList<V> variantList;

    public BlockMulti(Material material, IBlockVariantList<V> variantList) {
        super(material);
        this.variantList = variantList;

        // meta
        setDefaultState(blockState.getBaseState()
                .withProperty(VARIANT, variantList.getDefaultMetadata()));

    }

    //

    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    public IBlockState getState(V variant) {
        return getDefaultState().withProperty(VARIANT, variant.getMetadata());
    }

    @Override
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState().withProperty(VARIANT, metadata);
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        return blockState.getValue(VARIANT);
    }

    public V getVariant(IBlockState state) {
        return getVariant(getMetaFromState(state));
    }

    public V getVariant(int metadata) {
        return variantList.byMetadata(metadata);
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> itemStacks) {
        for (V variant : variantList.getBlockVariants()) {
            itemStacks.add(new ItemStack(this, 1, variant.getMetadata()));
        }
    }

    //

    protected ItemStack getItem(IBlockAccess world, BlockPos pos, IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return getItem((IBlockAccess) world, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(getItem(world, pos, state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

}
