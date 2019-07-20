package miragefairy2019.mod.modules.ore;

import java.util.Random;

import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.lib.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOreSeed extends Block
{

	public BlockOreSeed()
	{
		super(Material.ROCK);

		// meta
		setDefaultState(blockState.getBaseState()
			.withProperty(VARIANT, EnumVariant.TINY));

		// style
		setSoundType(SoundType.STONE);

		// 挙動
		setHardness(1.5f);
		setResistance(10.0f);

	}

	//

	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", EnumVariant.class);

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, VARIANT);
	}

	public IBlockState getState(EnumVariant variant)
	{
		return getDefaultState().withProperty(VARIANT, variant);
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(VARIANT, EnumVariant.byMetadata(meta));
	}

	public int getMetaFromState(IBlockState blockState)
	{
		return blockState.getValue(VARIANT).metadata;
	}

	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> itemStacks)
	{
		for (EnumVariant variant : EnumVariant.values()) {
			itemStacks.add(new ItemStack(this, 1, variant.metadata));
		}
	}

	//

	@Override
	public void getDrops(NonNullList<ItemStack> p_getDrops_1_, IBlockAccess p_getDrops_2_, BlockPos p_getDrops_3_, IBlockState p_getDrops_4_, int p_getDrops_5_)
	{
		// TODO 自動生成されたメソッド・スタブ
		super.getDrops(p_getDrops_1_, p_getDrops_2_, p_getDrops_3_, p_getDrops_4_, p_getDrops_5_);
	}

	public Item getItemDropped(IBlockState p_getItemDropped_1_, Random p_getItemDropped_2_, int p_getItemDropped_3_)
	{
		return Item.getItemFromBlock(Blocks.STONE);
	}

	public int damageDropped(IBlockState blockState)
	{
		return 0;
	}



	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state)
	{
		return ApiMirageFlower.itemStackMirageFlowerSeeds.copy();
	}

	/**
	 * Ageが最大のとき、種を1個ドロップする。
	 * 幸運Lv1につき種のドロップ数が1%増える。
	 * 地面破壊ドロップでも適用される。
	 */
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		Random random = world instanceof World ? ((World) world).rand : new Random();

		// 種1個は確定でドロップ
		drops.add(ApiMirageFlower.itemStackMirageFlowerSeeds.copy());

		// 追加の種
		{
			int count = Utils.randomInt(isMaxAge(state) ? fortune * 0.01 : 0, random);
			for (int i = 0; i < count; i++) {
				drops.add(ApiMirageFlower.itemStackMirageFlowerSeeds.copy());
			}
		}

		// クリスタル
		{
			int count = Utils.randomInt(isMaxAge(state) ? 1 + fortune * 0.5 : 0, random);
			for (int i = 0; i < count; i++) {
				drops.add(ApiFairy.itemStackFairyCrystal.copy());
			}
		}

	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	//

	public static enum EnumVariant implements IStringSerializable
	{
		TINY(0, "tiny", "tiny"),
		LAPIS(1, "lapis", "lapis"),
		DIAMOND(2, "diamond", "diamond"),
		IRON(3, "iron", "iron"),
		MEDIUM(4, "medium", "medium"),
		COAL(5, "coal", "coal"),
		LARGE(6, "large", "large"),
		DIRT(7, "dirt", "dirt"),
		;

		//

		private static final EnumVariant[] META_LOOKUP;
		static {
			META_LOOKUP = new EnumVariant[EnumVariant.values().length];
			EnumVariant[] types = EnumVariant.values();
			for (int i = 0; i < types.length; i++) {
				EnumVariant.META_LOOKUP[types[i].metadata] = types[i];
			}
		}

		public static EnumVariant byMetadata(int metadata)
		{
			if (metadata < 0 || metadata >= META_LOOKUP.length) metadata = 0;
			return META_LOOKUP[metadata];
		}

		//

		public final int metadata;
		public final String resourceName;
		public final String unlocalizedName;

		private EnumVariant(int metadata, String resourceName, String unlocalizedName)
		{
			this.metadata = metadata;
			this.resourceName = resourceName;
			this.unlocalizedName = unlocalizedName;
		}

		public String toString()
		{
			return this.resourceName;
		}

		public String getName()
		{
			return this.resourceName;
		}

	}

}
