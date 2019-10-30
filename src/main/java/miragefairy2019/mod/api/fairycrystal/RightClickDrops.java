package miragefairy2019.mod.api.fairycrystal;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import mirrg.boron.util.suppliterator.ISuppliterator;
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
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.oredict.OreIngredient;

public class RightClickDrops
{

	public static IRightClickDrop fixed(IDrop drop)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testUseItem(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
			{
				return true;
			}
		};
	}

	public static IRightClickDrop world(IDrop drop, BiPredicate<World, BlockPos> predicate)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testWorld(World world, BlockPos pos)
			{
				return predicate.test(world, pos);
			}
		};
	}

	public static IRightClickDrop blocks(IDrop drop, Block... blocks)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testBlock(Block block)
			{
				for (Block block2 : blocks) {
					if (block.equals(block2)) return true;
				}
				return false;
			}
		};
	}

	public static IRightClickDrop blockStates(IDrop drop, IBlockState... blockStates)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testBlockState(World world, BlockPos blockPos, IBlockState blockState)
			{
				for (IBlockState blockState2 : blockStates) {
					if (blockState.equals(blockState2)) return true;
				}
				return false;
			}
		};
	}

	public static IRightClickDrop items(IDrop drop, Item... items)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testItem(Item item)
			{
				for (Item item2 : items) {
					if (item.equals(item2)) return true;
				}
				return false;
			}
		};
	}

	public static IRightClickDrop ores(IDrop drop, String... ores)
	{
		List<Predicate<ItemStack>> ingredients = ISuppliterator.ofObjArray(ores)
			.<Predicate<ItemStack>> map(o -> new OreIngredient(o))
			.toList();
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testItemStack(ItemStack itemStack)
			{
				for (Predicate<ItemStack> ingredient : ingredients) {
					if (ingredient.test(itemStack)) return true;
				}
				return false;
			}
		};
	}

	@SafeVarargs
	public static IRightClickDrop ingredients(IDrop drop, Predicate<ItemStack>... ingredients)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testItemStack(ItemStack itemStack)
			{
				for (Predicate<ItemStack> ingredient : ingredients) {
					if (ingredient.test(itemStack)) return true;
				}
				return false;
			}
		};
	}

	public static IRightClickDrop biomeTypes(IDrop drop, BiomeDictionary.Type... biomeTypes)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testBiomeType(Type biomeType)
			{
				for (BiomeDictionary.Type biomeType2 : biomeTypes) {
					if (biomeType.equals(biomeType2)) return true;
				}
				return false;
			}
		};
	}

	public static IRightClickDrop biomes(IDrop drop, Biome... biomes)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testBiome(Biome biome)
			{
				for (Biome biome2 : biomes) {
					if (biome.equals(biome2)) return true;
				}
				return false;
			}
		};
	}

	@SafeVarargs
	public static IRightClickDrop classEntities(IDrop drop, Class<? extends Entity>... classEntities)
	{
		return new IRightClickDrop() {
			@Override
			public IDrop getDrop()
			{
				return drop;
			}

			@Override
			public boolean testClassEntity(Class<? extends Entity> classEntity)
			{
				for (Class<? extends Entity> classEntity2 : classEntities) {
					if (classEntity.isAssignableFrom(classEntity2)) return true;
				}
				return false;
			}
		};
	}

}
