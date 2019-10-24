package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyWeaponBase;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCollectingMagicWand extends ItemFairyWeaponBase
{

	public ItemCollectingMagicWand()
	{
		super(Composite.empty()
			.add(Components.FLUORITE, 3)
			.add(Components.MIRAGIUM, 2)
			.add(Components.OBSIDIAN, 2)
			.add(Components.fairyAbilityType(EnumAbilityType.warp), 1));
		setMaxDamage(64 - 1);
	}

	//

	protected static class Status
	{

		public final double additionalReach;
		public final double radius;
		public final int maxStacks;
		public final double wear;
		public final double coolTime;

		public Status(FairyType fairyType)
		{
			additionalReach = Math.min(fairyType.manaSet.wind / 5.0, 8);
			radius = Math.min(2 + fairyType.manaSet.fire / 10.0 + fairyType.abilitySet.get(EnumAbilityType.warp) / 10.0, 7);
			maxStacks = (int) (Math.min(1 + fairyType.manaSet.gaia / 2.0 + fairyType.abilitySet.get(EnumAbilityType.store) / 2.0, 20));
			wear = 0.25 * Math.pow(0.5, fairyType.manaSet.aqua / 30);
			coolTime = fairyType.cost * 3 * Math.pow(0.5, fairyType.manaSet.dark / 40);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("魔法のマジックハンド");

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
		tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Wind)");
		tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Fire, " + EnumAbilityType.warp.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Max Stacks: " + status.maxStacks + " (Gaia, " + EnumAbilityType.store.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Aqua)");
		tooltip.add(TextFormatting.BLUE + "Cool Time: " + ((int) status.coolTime) + "t (Dark, Cost)");

	}

	//

	private static enum EnumExecutability
	{
		OK(0xFFFFFF),
		COOLTIME(0xFFFF00),
		NO_TARGET(0x00FFFF),
		NO_FAIRY(0xFF00FF),
		NO_DURABILITY(0xFF0000),
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
		public final List<EntityItem> entityItems;

		public ResultWithFairy(
			EnumExecutability executability,
			Vec3d positionTarget,
			Status status,
			RayTraceResult rayTraceResult,
			List<EntityItem> entityItems)
		{
			super(executability, positionTarget);
			this.status = status;
			this.rayTraceResult = rayTraceResult;
			this.entityItems = entityItems;
		}

	}

	private static class ResultOk extends ResultWithFairy
	{

		public ResultOk(
			EnumExecutability executability,
			Vec3d positionTarget,
			Status status,
			RayTraceResult rayTraceResult,
			List<EntityItem> entityItems)
		{
			super(executability, positionTarget, status, rayTraceResult, entityItems);
		}

	}

	private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player)
	{

		// 妖精取得
		Tuple<ItemStack, FairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy == null) {
			return new Result(EnumExecutability.NO_FAIRY, getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()));
		}

		// ステータスを評価
		Status status = new Status(fairy.y);

		// 発動座標
		RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
		Vec3d positionTarget = rayTraceResult != null
			? rayTraceResult.hitVec
			: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach);

		// 対象を取得
		List<EntityItem> entityItems = getEntityItems(status, world, player, positionTarget);

		// 実行可能性を計算
		EnumExecutability executability = itemStack.getItemDamage() >= itemStack.getMaxDamage()
			? EnumExecutability.NO_DURABILITY
			: entityItems.isEmpty()
				? EnumExecutability.NO_TARGET
				: player.getCooldownTracker().hasCooldown(this)
					? EnumExecutability.COOLTIME
					: EnumExecutability.OK;

		return new ResultWithFairy(executability, positionTarget, status, rayTraceResult, entityItems);
	}

	private List<EntityItem> getEntityItems(Status status, World world, EntityPlayer player, Vec3d position)
	{
		return world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
			position.x - status.radius,
			position.y - status.radius,
			position.z - status.radius,
			position.x + status.radius,
			position.y + status.radius,
			position.z + status.radius),
			e -> {
				if (e.getDistanceSq(position.x, position.y, position.z) > status.radius * status.radius) return false;
				if (e.getDistanceSq(player) < 0.1) return false;
				return true;
			});
	}

	//

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{

		// アイテム取得
		ItemStack itemStack = player.getHeldItem(hand);

		// サーバーのみ
		if (world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		//

		// 判定
		Result result = getExecutability(world, itemStack, player);

		// 判定がだめだったらスルー
		if (!(result instanceof ResultOk)) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		ResultOk resultOk = (ResultOk) result;

		// 魔法成立

		int successed = 0;
		double maxStacks2 = resultOk.status.maxStacks;
		for (EntityItem entityItem : resultOk.entityItems) {

			// 耐久が0のときは破壊をやめる
			if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) break;

			// パワーが足りないので破壊をやめる
			if (maxStacks2 < 1) break;

			//消費
			if (world.rand.nextDouble() < resultOk.status.wear) itemStack.damageItem(1, player);
			maxStacks2--;
			successed++;

			// 魔法を行使
			entityItem.setPosition(player.posX, player.posY, player.posZ);
			entityItem.setNoPickupDelay();

		}

		if (successed > 0) {

			// エフェクト
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

			// クールタイム
			player.getCooldownTracker().setCooldown(this, (int) (resultOk.status.coolTime * (1 - maxStacks2 / (double) resultOk.status.maxStacks)));

		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	private static double rotateY = 0;

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{

		// プレイヤー取得
		if (!(entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entity;

		// アイテム取得
		if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

		// クライアントのみ
		if (!ApiMain.side.isClient()) return;

		//

		// 判定
		Result result = getExecutability(world, itemStack, player);

		// 発動中心点にパーティクルを表示
		spawnParticle(world, result.positionTarget, result.executability.color);

		// 発動範囲にパーティクルを表示
		if (result instanceof ResultWithFairy) {
			ResultWithFairy resultWithFairy = (ResultWithFairy) result;

			// 角度アニメーション更新
			rotateY += 4.9 / 180.0 * Math.PI;
			if (rotateY > 2 * Math.PI) rotateY -= 2 * Math.PI;

			for (int i = 0; i < 8; i++) {

				// 横角度
				double yaw = rotateY + i * 0.25 * Math.PI;

				a:
				for (int j = 0; j < 100; j++) {

					// パーティクル仮出現点
					double pitch = (-0.5 + Math.random()) * Math.PI;
					Vec3d offset = new Vec3d(
						Math.cos(pitch) * Math.cos(yaw),
						Math.sin(pitch),
						Math.cos(pitch) * Math.sin(yaw)).scale(resultWithFairy.status.radius);
					Vec3d positionParticle = result.positionTarget.add(offset);

					// 仮出現点が、真下がブロックな空洞だった場合のみ受理
					if (!world.getBlockState(new BlockPos(positionParticle)).isFullBlock()) {
						if (world.getBlockState(new BlockPos(positionParticle).down()).isFullBlock()) {

							// パーティクル出現点2
							// 高さを地面にくっつけるために、高さを地面の高さに固定した状態で横位置を調整する
							double y = Math.floor(positionParticle.y) + 0.15;
							double offsetY = y - result.positionTarget.y;
							double r1 = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
							if (Double.isNaN(r1)) break a;
							double r2 = Math.sqrt(resultWithFairy.status.radius * resultWithFairy.status.radius - offsetY * offsetY);
							if (Double.isNaN(r2)) break a;
							double offsetX = offset.x / r1 * r2;
							double offsetZ = offset.z / r1 * r2;

							world.spawnParticle(
								EnumParticleTypes.END_ROD,
								result.positionTarget.x + offsetX,
								Math.floor(positionParticle.y) + 0.15,
								result.positionTarget.z + offsetZ,
								0,
								-0.08,
								0);

							break a;
						}
					}

				}

			}

		}

		// 対象のアイテムにパーティクルを表示
		if (result instanceof ResultWithFairy) {
			ResultWithFairy resultWithFairy = (ResultWithFairy) result;

			int maxStacks2 = resultWithFairy.status.maxStacks;
			for (EntityItem entityItem : resultWithFairy.entityItems) {

				int color = 0x00FF00;

				if (maxStacks2 < 1) color = 0xFF0000;
				maxStacks2--;

				if (Math.random() < 0.2) {
					spawnParticle(world, entityItem.getPositionVector(), color);
				}

			}
		}

	}

}
