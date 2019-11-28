package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBellFlowerPicking extends ItemBellBase
{

	public ItemBellFlowerPicking()
	{
		addComponent(Components.MIRAGIUM, 0.5);
		addComponent(Components.MAGNETITE, 0.5);
		addComponent(Components.PYROPE, 2);
		addComponent(Components.GOLD, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.slash));
		setMaxDamage(128 - 1);
		setDescription("ちょっとお花を摘みに");
	}

	//

	protected static class Status
	{

		public final double pitch;
		public final double additionalReach;
		public final double radius;
		public final int maxTargetCount;
		public final int fortune;
		public final boolean seeding;
		public final boolean collection;
		public final double wear;
		public final double coolTime;

		public Status(FairyType fairyType)
		{
			pitch = 1.0 * Math.pow(0.5, fairyType.cost / 50.0 - 1);
			additionalReach = 0 + Math.min(fairyType.manaSet.wind / 10.0, 3);
			radius = 2 + UtilsMath.trim(fairyType.manaSet.gaia / 10.0, 0, 3);
			maxTargetCount = 1 + (int) (Math.min(fairyType.manaSet.dark + fairyType.abilitySet.get(EnumAbilityType.submission) / (fairyType.cost / 50.0) + fairyType.abilitySet.get(EnumAbilityType.slash) / (fairyType.cost / 50.0), 50));
			fortune = fairyType.manaSet.shine >= 10
				? 4
				: fairyType.manaSet.shine >= 5
					? 3
					: fairyType.manaSet.shine >= 2
						? 2
						: fairyType.manaSet.shine >= 1
							? 1
							: 0;
			seeding = fairyType.abilitySet.get(EnumAbilityType.knowledge) >= 10;
			collection = fairyType.abilitySet.get(EnumAbilityType.warp) >= 10;
			wear = 0.25 * UtilsMath.trim(Math.pow(0.5, fairyType.manaSet.fire / 30.0), 0.5, 1.0);
			coolTime = fairyType.cost * 4 * UtilsMath.trim(Math.pow(0.5, fairyType.manaSet.aqua / 30.0), 0.5, 1.0);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		tooltip.add(TextFormatting.RED + "Right click to use magic");

		super.addInformationFunctions(itemStack, world, tooltip, flag);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, FairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		Status status = new Status(fairyType);
		tooltip.add(TextFormatting.BLUE + "Pitch: " + String.format("%.2f", Math.log(status.pitch) / Math.log(2) * 12) + " (Cost)");
		tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Wind)");
		tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Gaia)");
		tooltip.add(TextFormatting.BLUE + "Max Targets: " + String.format("%d", status.maxTargetCount) + " (Dark, " + EnumAbilityType.submission.getLocalizedName() + ", " + EnumAbilityType.slash.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Fortune: " + String.format("%d", status.fortune) + " (Shine)");
		tooltip.add(TextFormatting.BLUE + "Seeding: " + (status.seeding ? "Yes" : "No") + " (" + EnumAbilityType.knowledge.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Collection: " + (status.collection ? "Yes" : "No") + " (" + EnumAbilityType.warp.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Fire)");
		tooltip.add(TextFormatting.BLUE + "Cool Time: " + String.format("%.0f", status.coolTime) + "t (Aqua, Cost)");
	}

	//

	private static enum EnumExecutability
	{
		OK(4, 0xFFFFFF),
		COOLTIME(3, 0xFFFF00),
		NO_TARGET(2, 0x00FFFF),
		NO_RESOURCE(1, 0xFF0000),
		NO_FAIRY(0, 0xFF00FF),;

		public final int health;
		public final int color;

		private EnumExecutability(int health, int color)
		{
			this.health = health;
			this.color = color;
		}

	}

	private static class Result
	{

		public final EnumExecutability executability;
		public final Vec3d positionTarget;

		public Result(
			EnumExecutability executability,
			Vec3d positionTarget)
		{
			this.executability = executability;
			this.positionTarget = positionTarget;
		}

	}

	private static class ResultWithFairy extends Result
	{

		public final FairyType fairyType;
		public final Status status;
		public final List<Tuple<BlockPos, Boolean>> targets;

		public ResultWithFairy(
			EnumExecutability executability,
			Vec3d positionTarget,
			FairyType fairyType,
			Status status,
			List<Tuple<BlockPos, Boolean>> targets)
		{
			super(executability, positionTarget);
			this.fairyType = fairyType;
			this.status = status;
			this.targets = targets;
		}

	}

	// /fill ~-10 ~ ~-10 ~10 ~ ~10 miragefairy2019:mirage_flower 3
	private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player)
	{

		// 妖精取得
		Tuple<ItemStack, FairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy == null) {
			RayTraceResult rayTraceResult = rayTrace(world, player, false, 0);
			Vec3d positionTarget = rayTraceResult != null
				? rayTraceResult.hitVec
				: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
			return new Result(
				EnumExecutability.NO_FAIRY,
				positionTarget);
		}

		// ステータスを評価
		Status status = new Status(fairy.y);

		// 発動対象
		RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
		Vec3d positionTarget = rayTraceResult != null
			? rayTraceResult.hitVec
			: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach);
		List<Tuple<BlockPos, Boolean>> targets = new ArrayList<>();
		{
			List<Tuple<BlockPos, Double>> targets2 = new ArrayList<>();

			int xMin = (int) Math.floor(positionTarget.x - status.radius);
			int xMax = (int) Math.ceil(positionTarget.x + status.radius);
			int zMin = (int) Math.floor(positionTarget.z - status.radius);
			int zMax = (int) Math.ceil(positionTarget.z + status.radius);
			for (int x = xMin; x <= xMax; x++) {
				int y = (int) positionTarget.y;
				for (int z = zMin; z <= zMax; z++) {
					BlockPos blockPos = new BlockPos(x, y, z);

					double dx = (x + 0.5) - positionTarget.x;
					double dz = (z + 0.5) - positionTarget.z;
					double distance2 = dx * dx + dz * dz;
					if (distance2 <= status.radius * status.radius) {

						IBlockState blockState = world.getBlockState(blockPos);
						if (blockState.getBlock() == ModuleMirageFlower.blockMirageFlower) {
							if (ModuleMirageFlower.blockMirageFlower.isMaxAge(blockState)) {
								targets2.add(Tuple.of(blockPos, distance2));
							}
						}

					}

				}
			}

			targets = ISuppliterator.ofIterable(targets2)
				.sortedDouble(Tuple::getY)
				.map((t, i) -> Tuple.of(t.x, i < status.maxTargetCount))
				.toList();
		}

		// 実行可能性を計算
		EnumExecutability executability = itemStack.getItemDamage() + (int) Math.ceil(status.wear) > itemStack.getMaxDamage()
			? EnumExecutability.NO_RESOURCE
			: !targets.stream().anyMatch(t -> t.y)
				? EnumExecutability.NO_TARGET
				: player.getCooldownTracker().hasCooldown(this)
					? EnumExecutability.COOLTIME
					: EnumExecutability.OK;

		return new ResultWithFairy(executability, positionTarget, fairy.y, status, targets);
	}

	//

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{

		// アイテム取得
		ItemStack itemStack = player.getHeldItem(hand);

		//

		// 判定
		Result result = getExecutability(world, itemStack, player);

		// 判定がだめだったらスルー
		if (result.executability.health < EnumExecutability.OK.health) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);
		ResultWithFairy resultWithFairy = (ResultWithFairy) result;

		// 魔法成立

		SoundEvent breakSound = null;

		int targetCount = 0;
		for (Tuple<BlockPos, Boolean> tuple : resultWithFairy.targets) {
			if (tuple.y) {

				// 耐久が足りないので中止
				if (itemStack.getItemDamage() + (int) Math.ceil(resultWithFairy.status.wear) > itemStack.getMaxDamage()) break;

				// パワーが足りないので中止
				if (targetCount >= resultWithFairy.status.maxTargetCount) break;

				// 行使
				itemStack.damageItem(Utils.randomInt(world.rand, resultWithFairy.status.wear), player);
				targetCount++;
				{
					IBlockState blockState = world.getBlockState(tuple.x);
					breakSound = blockState.getBlock().getSoundType(blockState, world, tuple.x, player).getBreakSound();
				}
				{

					// 破壊
					breakBlock(world, player, EnumFacing.UP, itemStack, tuple.x, resultWithFairy.status.fortune, false);

					// 種まき
					if (resultWithFairy.status.seeding) {

						// 破壊したばかりのブロックの周辺のアイテムを得る
						List<EntityItem> entityItems = world.getEntitiesWithinAABB(
							EntityItem.class,
							new AxisAlignedBB(tuple.x),
							e -> e.getItem().isItemEqual(new ItemStack(ModuleMirageFlower.itemMirageFlowerSeeds)) && e.getItem().getCount() > 0);
						if (!entityItems.isEmpty()) {
							EntityItem entityItem = entityItems.get(0);

							// 種を削る
							entityItem.getItem().shrink(1);
							if (entityItem.getItem().isEmpty()) entityItem.setDead();

							// 植える
							world.setBlockState(tuple.x, ModuleMirageFlower.blockMirageFlower.getState(0));

						}

					}

					// 収集
					if (resultWithFairy.status.collection) {

						// 破壊したばかりのブロックの周辺のアイテムを得る
						for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(tuple.x))) {
							entityItem.setPosition(player.posX, player.posY, player.posZ);
							entityItem.setNoPickupDelay();
						}

					}

				}

				// エフェクト
				int color = resultWithFairy.fairyType.colorSet.hair;
				world.spawnParticle(
					EnumParticleTypes.SPELL_MOB,
					tuple.x.getX() + 0.5,
					tuple.x.getY() + 0.5,
					tuple.x.getZ() + 0.5,
					((color >> 16) & 0xFF) / 255.0,
					((color >> 8) & 0xFF) / 255.0,
					((color >> 0) & 0xFF) / 255.0);

			}
		}

		if (targetCount >= 1) {

			// エフェクト
			ItemBellBase.playSound(world, player, resultWithFairy.status.pitch);
			world.playSound(player, player.posX, player.posY, player.posZ, breakSound, SoundCategory.PLAYERS, 1.0F, 1.0F);

			// クールタイム
			player.getCooldownTracker().setCooldown(this, (int) (resultWithFairy.status.coolTime * (targetCount / (double) resultWithFairy.status.maxTargetCount)));

		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{

		// クライアントのみ
		if (!ApiMain.side.isClient()) return;

		// プレイヤー取得
		if (!(entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entity;

		// アイテム取得
		if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

		//

		// 判定
		Result result = getExecutability(world, itemStack, player);

		//

		// 発動中心点にパーティクルを表示
		spawnParticle(world, result.positionTarget, result.executability.color);

		if (result instanceof ResultWithFairy && result.executability.health >= EnumExecutability.NO_TARGET.health) {
			ResultWithFairy resultWithFairy = (ResultWithFairy) result;

			// 発動範囲にパーティクルを表示
			ItemMagicWandCollecting.spawnParticleSphericalRange(
				world,
				resultWithFairy.positionTarget,
				resultWithFairy.status.radius);

			// 対象にパーティクルを表示
			ItemMagicWandCollecting.spawnParticleTargets(
				world,
				resultWithFairy.targets,
				target -> target.y,
				target -> new Vec3d(target.x),
				resultWithFairy.status.maxTargetCount);

		}

	}

}
