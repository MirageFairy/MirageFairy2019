package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyWeaponBase;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTemptationOcarina extends ItemFairyWeaponBase
{

	public ItemTemptationOcarina()
	{
		super(Composite.empty()
			.add(Components.PYROPE, 4)
			.add(Components.MIRAGIUM, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.food), 1));
		setMaxDamage(64 - 1);
	}

	//

	protected static class Status
	{

		public final double radius;
		public final int maxEntities;
		public final double wear;
		public final double experienceCost;
		public final double coolTime;

		public Status(FairyType fairyType)
		{
			radius = UtilsMath.trim(5 + fairyType.manaSet.wind / 5.0, 5, 10);
			maxEntities = UtilsMath.trim(1 + (int) (fairyType.manaSet.aqua / 7.0), 1, 8);
			wear = UtilsMath.trim(0.25 * Math.pow(0.5, fairyType.manaSet.fire / 50.0), 0.025, 0.25);
			experienceCost = UtilsMath.trim(Math.pow(0.5, fairyType.manaSet.gaia / 50.0 + fairyType.abilitySet.get(EnumAbilityType.food) / 10.0), 0.1, 1);
			coolTime = fairyType.cost * UtilsMath.trim(Math.pow(0.5, fairyType.manaSet.dark / 50.0), 0.1, 1);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("聞くだけで腹が膨れる音色とは");

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
		tooltip.add(TextFormatting.BLUE + "Max Entities: " + String.format("%d", status.maxEntities) + " (Aqua)");
		tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Fire)");
		tooltip.add(TextFormatting.BLUE + "Experience Cost: " + String.format("%.1f", status.experienceCost * 100) + "% (Gaia, " + EnumAbilityType.food.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Cool Time: " + String.format("%d", status.coolTime) + "t (Dark, Cost)");

	}

	//

	private static enum EnumExecutability
	{
		OK(0xFFFFFF),
		COOLTIME(0xFFFF00),
		NO_TARGET(0x00FFFF),
		NO_FAIRY(0xFF00FF),
		NO_RESOURCE(0xFF0000),
		;

		public final int color;

		private EnumExecutability(int color)
		{
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
		public final RayTraceResult rayTraceResult;
		public final boolean isEntity;
		public final List<EntityVillager> entities;

		public ResultWithFairy(
			EnumExecutability executability,
			Vec3d positionTarget,
			Status status,
			RayTraceResult rayTraceResult,
			boolean isEntity,
			List<EntityVillager> entities)
		{
			super(executability, positionTarget);
			this.status = status;
			this.rayTraceResult = rayTraceResult;
			this.isEntity = isEntity;
			this.entities = entities;
		}

	}

	private static class ResultOk extends ResultWithFairy
	{

		public ResultOk(
			EnumExecutability executability,
			Vec3d positionTarget,
			Status status,
			RayTraceResult rayTraceResult,
			boolean isEntity,
			List<EntityVillager> entities)
		{
			super(executability, positionTarget, status, rayTraceResult, isEntity, entities);
		}

	}

	private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player)
	{

		// 妖精取得
		Tuple<ItemStack, FairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy == null) {
			RayTraceResult rayTraceResult = rayTrace(world, player, false, 0);
			return new Result(
				EnumExecutability.NO_FAIRY,
				rayTraceResult != null
					? rayTraceResult.hitVec
					: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()));
		}

		// ステータスを評価
		Status status = new Status(fairy.y);

		// 発動対象
		RayTraceResult rayTraceResult = rayTrace(world, player, false, 0);
		Vec3d positionTarget = rayTraceResult != null
			? rayTraceResult.hitVec
			: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
		boolean isEntity;
		List<EntityVillager> entities;
		if (rayTraceResult != null && rayTraceResult.typeOfHit == Type.ENTITY) {
			// エンティティ

			isEntity = true;
			entities = new ArrayList<>();
			if (rayTraceResult.entityHit instanceof EntityVillager) {
				entities.add((EntityVillager) rayTraceResult.entityHit);
			}

		} else {
			// 範囲

			isEntity = false;
			entities = getEntities(status, world, player, player.getPositionVector());

		}

		// 実行可能性を計算
		EnumExecutability executability = itemStack.getItemDamage() >= itemStack.getMaxDamage()
			? EnumExecutability.NO_RESOURCE
			: entities.isEmpty()
				? EnumExecutability.NO_TARGET
				: player.getCooldownTracker().hasCooldown(this)
					? EnumExecutability.COOLTIME
					: EnumExecutability.OK;

		return executability == EnumExecutability.OK
			? new ResultOk(executability, positionTarget, status, rayTraceResult, isEntity, entities)
			: new ResultWithFairy(executability, positionTarget, status, rayTraceResult, isEntity, entities);
	}

	private List<EntityVillager> getEntities(Status status, World world, EntityPlayer player, Vec3d position)
	{
		return world.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(
			position.x - status.radius,
			position.y - status.radius,
			position.z - status.radius,
			position.x + status.radius,
			position.y + status.radius,
			position.z + status.radius),
			e -> {
				if (e.getDistanceSq(position.x, position.y, position.z) > status.radius * status.radius) return false;
				return true;
			});
	}

	//

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack itemStack = player.getHeldItem(hand);

		// 視線判定
		RayTraceResult rayTraceResult = rayTrace(world, player, false, 0);
		if (rayTraceResult == null) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
		if (rayTraceResult.typeOfHit != RayTraceResult.Type.ENTITY) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
		Entity entity = rayTraceResult.entityHit;

		// 対象が村人でない場合は不発
		if (!(entity instanceof EntityVillager)) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);
		EntityVillager entityVillager = (EntityVillager) entity;

		// 対象が成体でない場合は不発
		if (entityVillager.getGrowingAge() < 0) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);

		// 対象が満腹である場合は不発
		if (entityVillager.getIsWillingToMate(false)) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);

		{

			// 耐久が足りないので中止
			if (itemStack.getItemDamage() + 4 > itemStack.getMaxDamage()) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);

			// レベルが足りないので中止
			if (!player.isCreative()) if (player.experienceLevel < 30) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);

			// 行使
			itemStack.damageItem(4, player);
			if (!player.isCreative()) player.addExperienceLevel(-1);
			world.setEntityState(entityVillager, (byte) 18);
			entityVillager.setIsWillingToMate(true);

			// エフェクト
			if (!player.isCreative()) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.5F);
			world.playSound(null, entityVillager.posX, entityVillager.posY, entityVillager.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);
			Random random = new Random();
			for (int i = 0; i < 5; i++) {
				world.spawnParticle(
					EnumParticleTypes.VILLAGER_HAPPY,
					entityVillager.posX + (random.nextDouble() * 2 - 1) * entityVillager.width,
					entityVillager.posY + 1 + random.nextDouble() * entityVillager.height,
					entityVillager.posZ + (random.nextDouble() * 2 - 1) * entityVillager.width,
					random.nextGaussian() * 0.02,
					random.nextGaussian() * 0.02,
					random.nextGaussian() * 0.02);
			}

			// クールタイム
			player.getCooldownTracker().setCooldown(this, (int) (60));

		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

}
