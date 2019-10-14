package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;
import java.util.function.Predicate;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.modules.sphere.EnumSphere;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;

public class ItemCollectingMagicWand extends ItemFairyWeaponBase
{

	public ItemCollectingMagicWand()
	{
		setMaxDamage(256 - 1);
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
			wear = Math.pow(0.5, fairyType.manaSet.aqua / 30);
			coolTime = fairyType.cost * 3 * Math.pow(0.5, fairyType.manaSet.dark / 40);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("魔法のマジックハンド");

		// アイテムステータス
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));

		// 素材
		tooltip.add(TextFormatting.YELLOW + "Contains: Fluorite(3.000), Miragium(2.000), Obsidian(2.000), Sphere of \"WARP\"");

		// 機能
		tooltip.add(TextFormatting.RED + "Right click to use magic");
		tooltip.add(TextFormatting.RED + "Can be repaired by crafting with contained sphere");

		super.addInformation(itemStack, world, tooltip, flag);

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

		public EnumExecutability executability = null;
		public Status status = null;
		public RayTraceResult rayTraceResult = null;
		public Vec3d positionTarget = null;
		public List<EntityItem> entityItems = null;

	}

	private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player)
	{
		Result result = new Result();

		// 妖精取得
		Tuple<ItemStack, FairyType> fairy = findFairy(player).orElse(null);
		if (fairy == null) {
			result.executability = EnumExecutability.NO_FAIRY;
			result.positionTarget = getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue());
			return result;
		}

		// ステータスを評価
		result.status = new Status(fairy.y);

		// 発動座標
		result.rayTraceResult = rayTrace(world, player, false, result.status.additionalReach);
		result.positionTarget = result.rayTraceResult != null
			? result.rayTraceResult.hitVec
			: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + result.status.additionalReach);

		// 対象を取得
		result.entityItems = getEntityItems(result.status, world, player, result.positionTarget);

		// 実行可能性を計算
		result.executability = itemStack.getItemDamage() >= itemStack.getMaxDamage()
			? EnumExecutability.NO_DURABILITY
			: result.entityItems.isEmpty()
				? EnumExecutability.NO_TARGET
				: player.getCooldownTracker().hasCooldown(this)
					? EnumExecutability.COOLTIME
					: EnumExecutability.OK;

		return result;
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
		if (result.executability != EnumExecutability.OK) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		// 魔法成立

		int successed = 0;
		double maxStacks2 = result.status.maxStacks;
		for (EntityItem entityItem : result.entityItems) {

			// 耐久が0のときは破壊をやめる
			if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) break;

			// パワーが足りないので破壊をやめる
			if (maxStacks2 < 1) break;

			//消費
			if (world.rand.nextDouble() < result.status.wear) itemStack.damageItem(1, player);
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
			player.getCooldownTracker().setCooldown(this, (int) (result.status.coolTime * (1 - maxStacks2 / (double) result.status.maxStacks)));

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
		{

			// 角度アニメーション更新
			rotateY += 0.02;
			if (rotateY > 2 * Math.PI) rotateY -= 2 * Math.PI;

			for (int i = 0; i < 4; i++) {

				// 横角度
				double a = rotateY + i * 0.5 * Math.PI;

				a:
				for (int j = 0; j < 100; j++) {

					// パーティクル出現点
					double b = (-0.5 + Math.random()) * Math.PI;
					double x = Math.cos(a) * Math.cos(b);
					double z = Math.sin(a) * Math.cos(b);
					double y = Math.sin(b);
					Vec3d positionParticle = result.positionTarget.addVector(
						x * result.status.radius,
						y * result.status.radius,
						z * result.status.radius);

					if (!world.getBlockState(new BlockPos(positionParticle)).isOpaqueCube()) {
						if (world.getBlockState(new BlockPos(positionParticle).down()).isOpaqueCube()) {

							world.spawnParticle(
								EnumParticleTypes.END_ROD,
								positionParticle.x,
								Math.floor(positionParticle.y),
								positionParticle.z,
								0,
								0.03,
								0);

							break a;
						}
					}

				}

			}

		}

		// 対象のアイテムにパーティクルを表示
		{
			int maxStacks2 = result.status.maxStacks;
			for (EntityItem entityItem : result.entityItems) {

				int color = 0x00FF00;

				if (maxStacks2 < 1) color = 0xFF0000;
				maxStacks2--;

				if (Math.random() < 0.2) {
					spawnParticle(world, entityItem.getPositionVector(), color);
				}

			}
		}

	}

	//

	@Override
	public NonNullList<Predicate<ItemStack>> getSpheres(ItemStack itemStack)
	{
		return ISuppliterator.of(
			new OreIngredient(EnumSphere.warp.getOreName()))
			.toCollection(NonNullList::create);
	}

}
