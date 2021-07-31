package miragefairy2019.mod.api.fairycrystal;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public interface IRightClickDrop {

    public IDrop getDrop();

    public default boolean testUseItem(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    public default boolean testWorld(World world, BlockPos pos) {
        return false;
    }

    public default boolean testBlock(Block block) {
        return false;
    }

    public default boolean testBlockState(World world, BlockPos blockPos, IBlockState blockState) {
        return false;
    }

    public default boolean testItem(Item item) {
        return false;
    }

    public default boolean testItemStack(ItemStack itemStack) {
        return false;
    }

    public default boolean testBiome(Biome biome) {
        return false;
    }

    public default boolean testBiomeType(BiomeDictionary.Type biomeType) {
        return false;
    }

    public default boolean testClassEntity(Class<? extends Entity> classEntity) {
        return false;
    }

    public default boolean testEntity(Entity entity) {
        return false;
    }

}
