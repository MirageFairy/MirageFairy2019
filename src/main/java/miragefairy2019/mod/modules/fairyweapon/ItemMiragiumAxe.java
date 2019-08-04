package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.fairy.FairyType;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMiragiumAxe extends ItemFairyWeaponBase
{

	public ItemMiragiumAxe()
	{
		setMaxDamage(256 - 1);
		setHarvestLevel("axe", 1);
	}

	//

	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		for (String type : getToolClasses(stack)) {
			if (state.getBlock().isToolEffective(type, state)) {
				return 6;
			}
		}
		// return this.effectiveBlocks.contains(state.getBlock()) ? this.efficiency : 1.0F;
		return 1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack itemStack = player.getHeldItem(hand);

		// 妖精を取得
		Tuple<ItemStack, FairyType> fairy = findFairy(player).orElse(null);
		if (fairy == null) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);

		// ステータスを評価
		Status status = new Status(fairy.y);

		// 視線判定
		RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
		if (rayTraceResult == null) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
		if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
		BlockPos blockPos = rayTraceResult.getBlockPos();

		// 対象が原木でない場合は不発
		if (!isLog(world, blockPos)) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);

		// きこり発動
		{

			// 音取得
			SoundEvent breakSound;
			{
				IBlockState blockState = world.getBlockState(blockPos);
				breakSound = blockState.getBlock().getSoundType(blockState, world, blockPos, player).getBreakSound();
			}

			int yMin = blockPos.getY();

			// 基点検索
			int yMax = blockPos.getY();
			for (int yi = 1; yi < status.maxHeight; yi++) {
				if (isLog(world, blockPos.add(0, yi, 0))) {
					yMax = blockPos.getY() + yi;
				} else {
					break;
				}
			}

			// 破壊
			int successed = 0;
			double power2 = status.power;
			for (int y = yMax; y >= yMin; y--) {
				BlockPos blockPos2 = new BlockPos(blockPos.getX(), y, blockPos.getZ());

				IBlockState blockState = world.getBlockState(blockPos2);
				float blockHardness = blockState.getBlockHardness(world, blockPos2);

				// パワーが足りないので破壊をやめる
				if (power2 < blockHardness) break;

				// 耐久が0のときは破壊をやめる
				if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) break;

				if (world.rand.nextDouble() < status.wear) itemStack.damageItem(1, player);
				power2 -= blockHardness;
				breakBlock(world, player, rayTraceResult.sideHit, itemStack, blockPos2, status.fortune);
				successed++;

			}

			if (successed > 0) {

				// エフェクト
				player.playSound(breakSound, 1.0F, 1.0F);

				// クールタイム
				player.getCooldownTracker().setCooldown(this, (int) (status.coolTime * (1 - power2 / status.power)));

			}

		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	private boolean isLog(World world, BlockPos blockPos)
	{
		IBlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() instanceof BlockLog) return true;
		if (blockState.getBlock() instanceof BlockLeaves) return true;
		return false;
	}

	private boolean breakBlock(World world, EntityPlayer player, EnumFacing facing, ItemStack itemStack, BlockPos blockPos, int fortune)
	{
		if (!world.isBlockModifiable(player, blockPos)) return false;
		if (!player.canPlayerEdit(blockPos, facing, itemStack)) return false;

		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		block.dropBlockAsItem(world, blockPos, blockState, fortune);
		world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);

		return true;
	}

	protected Vec3d getSight(EntityPlayer player, double distance)
	{
		float rotationPitch = player.rotationPitch;
		float rotationYaw = player.rotationYaw;
		double x = player.posX;
		double y = player.posY + (double) player.getEyeHeight();
		double z = player.posZ;
		Vec3d vec1 = new Vec3d(x, y, z);
		float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
		float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		Vec3d vec2 = vec1.addVector((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
		return vec2;
	}

	protected RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids, double additionalReach)
	{
		float rotationPitch = player.rotationPitch;
		float rotationYaw = player.rotationYaw;
		double x = player.posX;
		double y = player.posY + (double) player.getEyeHeight();
		double z = player.posZ;
		Vec3d vec3d = new Vec3d(x, y, z);
		float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
		float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + additionalReach;
		Vec3d vec3d1 = vec3d.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			if (isSelected || player.getHeldItemOffhand() == itemStack) {

				if (ApiMain.side.isClient()) {

					// 妖精がない場合はマゼンタ
					Tuple<ItemStack, FairyType> fairy = findFairy(player).orElse(null);
					if (fairy == null) {
						spawnParticle(
							world,
							getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()),
							0xFF00FF);
						return;
					}

					// ステータスを評価
					Status status = new Status(fairy.y);

					// 耐久がない場合は赤
					// 対象が原木でない場合は緑
					// クールタイムの場合は黄色
					RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
					if (rayTraceResult == null) {
						spawnParticle(
							world,
							getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach),
							itemStack.getItemDamage() >= itemStack.getMaxDamage() ? 0xFF0000 : player.getCooldownTracker().hasCooldown(this) ? 0x00FF00 : 0x00FFFF);
						return;
					}
					if (!(rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK && isLog(world, rayTraceResult.getBlockPos()))) {
						spawnParticle(
							world,
							rayTraceResult.hitVec,
							itemStack.getItemDamage() >= itemStack.getMaxDamage() ? 0xFF0000 : player.getCooldownTracker().hasCooldown(this) ? 0x00FF00 : 0x00FFFF);
						return;
					}

					spawnParticle(
						world,
						rayTraceResult.hitVec,
						itemStack.getItemDamage() >= itemStack.getMaxDamage() ? 0xFF0000 : player.getCooldownTracker().hasCooldown(this) ? 0xFFFF00 : 0xFFFFFF);

				}

			}

		}
	}

	private void spawnParticle(World world, Vec3d sight, int color)
	{
		world.spawnParticle(
			EnumParticleTypes.SPELL_MOB,
			sight.x,
			sight.y,
			sight.z,
			((color >> 16) & 0xFF) / 255.0,
			((color >> 8) & 0xFF) / 255.0,
			((color >> 0) & 0xFF) / 255.0);
	}

	protected static class Status
	{

		public final int maxHeight;
		public final double power;
		public final int fortune;
		public final int coolTime;
		public final double wear;
		public final double additionalReach;

		public Status(FairyType fairyType)
		{
			maxHeight = Math.min((int) (fairyType.manaSet.gaia / 2 + 2), 100);
			power = fairyType.manaSet.aqua / 2 + fairyType.abilitySet.get(EnumAbilityType.fell) / 2;
			fortune = Math.min((int) (fairyType.manaSet.shine / 5), 3);
			coolTime = (int) (fairyType.cost * 2 * Math.pow(0.5, fairyType.manaSet.dark / 50));
			wear = Math.pow(0.5, fairyType.manaSet.fire / 50);
			additionalReach = Math.min(fairyType.manaSet.wind / 5, 20);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("飛べるって素敵");

		// アイテムステータス
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));

		// 機能
		tooltip.add(TextFormatting.RED + "Right click to cut whole tree");

		// 素材
		tooltip.add(TextFormatting.YELLOW + "Contains: Miragium(3.000), Wood(1.000), Sphere of \"FELL\"");

		// 妖精魔法
		Tuple<ItemStack, FairyType> fairy = Optional.ofNullable(Minecraft.getMinecraft().player).flatMap(p -> findFairy(p)).orElse(null);
		if (fairy != null) {
			addInformation(itemStack, fairy.x, fairy.y, world, tooltip, flag);
		} else {
			tooltip.add(TextFormatting.BLUE + "No fairy is supplied");
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, FairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformation(itemStackFairyWeapon, itemStackFairy, fairyType, world, tooltip, flag);

		Status status = new Status(fairyType);
		tooltip.add(TextFormatting.BLUE + "Max Height: " + status.maxHeight + " (Gaia)");
		tooltip.add(TextFormatting.BLUE + "Power: " + String.format("%.1f", status.power) + " (Aqua, " + EnumAbilityType.fell.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Fortune: " + status.fortune + " (Shine)");
		tooltip.add(TextFormatting.BLUE + "Cool Time: " + status.coolTime + "t (Dark, Cost) (" + String.format("%.1f", status.coolTime / status.power) + "t per 1.0 power)");
		tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Fire)");
		tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Wind)");
	}

}
