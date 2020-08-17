package miragefairy2019.mod.modules.materialsfairy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import miragefairy2019.mod.lib.UtilsMinecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;

public class ItemFairyStick extends Item
{

	public ItemFairyStick()
	{
		setMaxStackSize(1);
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	//

	@Override
	@SideOnly(Side.CLIENT)
	public final void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		if (UtilsMinecraft.canTranslate(getUnlocalizedName() + ".poem")) {
			String string = UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".poem");
			if (!string.isEmpty()) {
				tooltip.add(string);
			}
		}

		// 機能
		tooltip.add(TextFormatting.RED + "Right Click: World Craft");

	}

	//

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		// TODO レシピシステムの設置

		BlockPos pos2 = pos.offset(facing);
		IBlockState blockState = worldIn.getBlockState(pos2);

		List<Runnable> onCrafts = new ArrayList<>();

		// 材料の判定
		{

			// ブロック
			if (!blockState.getBlock().isReplaceable(worldIn, pos2)) return EnumActionResult.PASS;
			if (!Blocks.WATER.getDefaultState().getBlock().canPlaceBlockAt(worldIn, pos2)) return EnumActionResult.PASS;
			if (BiomeDictionary.hasType(worldIn.getBiome(pos2), BiomeDictionary.Type.NETHER)) return EnumActionResult.PASS;
			onCrafts.add(() -> {
				worldIn.setBlockState(pos2, Blocks.AIR.getDefaultState(), 2);
				worldIn.setBlockState(pos2, Blocks.WATER.getDefaultState(), 2);
			});

			// アイテム
			{
				List<EntityItem> entities = worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos2));

				Predicate<Predicate<ItemStack>> pPuller = ingredient -> {

					Iterator<EntityItem> iterator = entities.iterator();
					while (iterator.hasNext()) {
						EntityItem entity = iterator.next();

						if (ingredient.test(entity.getItem())) {
							onCrafts.add(() -> {
								ItemStack itemStack = entity.getItem();
								itemStack.shrink(1);
								if (itemStack.isEmpty()) worldIn.removeEntity(entity);
							});
							iterator.remove();
							return true;
						}

					}

					return false;
				};

				if (!pPuller.test(new OreIngredient("mirageFairyCrystal"))) return EnumActionResult.PASS;
				if (!pPuller.test(new OreIngredient("mirageFairy2019FairyWaterRank1"))) return EnumActionResult.PASS;
			}

			// エフェクト
			onCrafts.add(() -> {
				worldIn.playSound(null, pos2, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.2F, 1.0F);
				for (int i = 0; i < 10; i++) {
					worldIn.spawnParticle(
						EnumParticleTypes.SPELL_MOB,
						pos2.getX() + worldIn.rand.nextDouble(),
						pos2.getY() + worldIn.rand.nextDouble(),
						pos2.getZ() + worldIn.rand.nextDouble(),
						0.5 + worldIn.rand.nextDouble() * 0.5,
						0.5 + worldIn.rand.nextDouble() * 0.5,
						0.5 + worldIn.rand.nextDouble() * 0.5);
				}
			});

		}

		// 成功
		onCrafts.forEach(Runnable::run);

		return EnumActionResult.SUCCESS;
	}

	//

	@Override
	public boolean hasContainerItem(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		return itemStack.copy();
	}

}
