package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;
import java.util.Random;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.lib.Utils;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTemptationOcarina extends ItemOcarinaBase
{

	public ItemTemptationOcarina()
	{
		composite = composite
			.add(Components.PYROPE, 4)
			.add(Components.fairyAbilityType(EnumAbilityType.food));
		setMaxDamage(128 - 1);
	}

	//

	protected static class Status
	{

		public final double radius;
		public final int maxTargetCount;
		public final double wear;
		public final double experienceCost;
		public final double coolTime;

		public Status(FairyType fairyType)
		{
			radius = UtilsMath.trim(5 + fairyType.manaSet.wind / 5.0, 5, 10);
			maxTargetCount = UtilsMath.trim(1 + (int) (fairyType.manaSet.aqua / 7.0), 1, 8);
			wear = UtilsMath.trim(4 * Math.pow(0.5, fairyType.manaSet.fire / 50.0), 0.4, 4);
			experienceCost = UtilsMath.trim(1 * Math.pow(0.5, fairyType.manaSet.gaia / 50.0 + fairyType.abilitySet.get(EnumAbilityType.food) / 10.0), 0.1, 1);
			coolTime = fairyType.cost * UtilsMath.trim(1 * Math.pow(0.5, fairyType.manaSet.dark / 50.0), 0.1, 1);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("その音は人の腹を満たし、淫靡な気分にさせる");

		super.addInformation(itemStack, world, tooltip, flag);

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
		super.addInformationFairyWeapon(itemStackFairyWeapon, itemStackFairy, fairyType, world, tooltip, flag);

		Status status = new Status(fairyType);
		tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Wind)");
		tooltip.add(TextFormatting.BLUE + "Max Targets: " + String.format("%d", status.maxTargetCount) + " (Aqua)");
		tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Fire)");
		tooltip.add(TextFormatting.BLUE + "Experience Cost: " + String.format("%.1f", status.experienceCost * 100) + "% (Gaia, " + EnumAbilityType.food.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Cool Time: " + String.format("%.0f", status.coolTime) + "t (Dark, Cost)");

	}

	//

	private static enum EnumExecutability
	{
		OK(4, 0xFFFFFF),
		COOLTIME(3, 0xFFFF00),
		NO_TARGET(2, 0x00FFFF),
		NO_RESOURCE(1, 0xFF0000),
		NO_FAIRY(0, 0xFF00FF),
		;

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

		public final Status status;
		public final boolean isEntity;
		public final List<Tuple<EntityVillager, Boolean>> targets;

		public ResultWithFairy(
			EnumExecutability executability,
			Vec3d positionTarget,
			Status status,
			boolean isEntity,
			List<Tuple<EntityVillager, Boolean>> targets)
		{
			super(executability, positionTarget);
			this.status = status;
			this.isEntity = isEntity;
			this.targets = targets;
		}

	}

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
		RayTraceResult rayTraceResult = rayTrace(world, player, false, 0, EntityVillager.class, e -> true);
		Vec3d positionTarget = rayTraceResult != null
			? rayTraceResult.hitVec
			: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
		boolean isEntity;
		List<EntityVillager> targetEntities;
		if (rayTraceResult != null && rayTraceResult.typeOfHit == Type.ENTITY && rayTraceResult.entityHit instanceof EntityVillager) {
			// エンティティ

			isEntity = true;
			targetEntities = ISuppliterator.of((EntityVillager) rayTraceResult.entityHit).toList();

		} else {
			// 範囲

			isEntity = false;
			targetEntities = ItemCollectingMagicWand.getEntities(EntityVillager.class, world, player.getPositionVector(), status.radius);

		}
		List<Tuple<EntityVillager, Boolean>> targets = ISuppliterator.ofIterable(targetEntities)
			.map(trgetEntity -> Tuple.of(trgetEntity, validate(trgetEntity)))
			.toList();

		// 実行可能性を計算
		EnumExecutability executability = itemStack.getItemDamage() + (int) Math.ceil(status.wear) > itemStack.getMaxDamage() || (!player.isCreative() && player.experienceLevel < 30)
			? EnumExecutability.NO_RESOURCE
			: !targets.stream().anyMatch(t -> t.y)
				? EnumExecutability.NO_TARGET
				: player.getCooldownTracker().hasCooldown(this)
					? EnumExecutability.COOLTIME
					: EnumExecutability.OK;

		return new ResultWithFairy(executability, positionTarget, status, isEntity, targets);
	}

	private boolean validate(EntityVillager entity)
	{
		if (entity.getGrowingAge() < 0) return false;
		if (entity.getIsWillingToMate(false)) return false;
		return true;
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
		if (result.executability.health < EnumExecutability.OK.health) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		ResultWithFairy resultWithFairy = (ResultWithFairy) result;

		// 魔法成立

		int targetCount = 0;
		boolean experienceCosted = false;
		for (Tuple<EntityVillager, Boolean> tuple : resultWithFairy.targets) {
			if (tuple.y) {

				// 耐久が足りないので中止
				if (itemStack.getItemDamage() + (int) Math.ceil(resultWithFairy.status.wear) > itemStack.getMaxDamage()) break;

				// パワーが足りないので中止
				if (targetCount >= resultWithFairy.status.maxTargetCount) break;

				// レベルが足りないので中止
				if (!player.isCreative()) if (player.experienceLevel < 30) break;

				// 行使
				{
					int damage = Utils.randomInt(world.rand, resultWithFairy.status.wear);
					System.out.println(damage);
					for (int i = 0; i < damage; i++) {
						itemStack.damageItem(1, player);
					}
				}
				targetCount++;
				if (!player.isCreative()) if (world.rand.nextDouble() < resultWithFairy.status.experienceCost) {
					player.addExperienceLevel(-1);
					experienceCosted = true;
				}
				world.setEntityState(tuple.x, (byte) 18);
				tuple.x.setIsWillingToMate(true);

				// エフェクト
				Random random = new Random();
				for (int i = 0; i < 5; i++) {
					world.spawnParticle(
						EnumParticleTypes.VILLAGER_HAPPY,
						tuple.x.posX + (random.nextDouble() * 2 - 1) * tuple.x.width,
						tuple.x.posY + 1 + random.nextDouble() * tuple.x.height,
						tuple.x.posZ + (random.nextDouble() * 2 - 1) * tuple.x.width,
						random.nextGaussian() * 0.02,
						random.nextGaussian() * 0.02,
						random.nextGaussian() * 0.02);
				}

			}
		}

		if (targetCount >= 1) {

			// エフェクト
			if (experienceCosted) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.5F);
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);

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
			if (!resultWithFairy.isEntity) {
				ItemCollectingMagicWand.spawnParticleSphericalRange(
					world,
					player.getPositionVector(),
					resultWithFairy.status.radius);
			}

			// 対象にパーティクルを表示
			ItemCollectingMagicWand.spawnParticleTargets(
				world,
				resultWithFairy.targets,
				target -> target.y,
				target -> target.x.getPositionVector(),
				resultWithFairy.status.maxTargetCount);

		}

	}

}
