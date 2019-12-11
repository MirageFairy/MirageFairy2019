package miragefairy2019.mod.modules.fairyweapon.item;

import static miragefairy2019.mod.api.ApiFairy.EnumAbilityType.*;
import static miragefairy2019.mod.api.fairy.EnumManaType.*;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.modules.fairyweapon.status.FairyWeaponStatusBase;
import miragefairy2019.mod.modules.fairyweapon.status.IFairyWeaponStatusProperty;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBellChristmas extends ItemBellBase
{

	public ItemBellChristmas()
	{
		addComponent(Components.MIRAGIUM, 0.5);
		addComponent(Components.MAGNETITE, 0.5);
		addComponent(Components.GOLD, 10);
		addComponent(Components.fairyAbilityType(EnumAbilityType.christmas));
		addComponent(Components.fairyAbilityType(EnumAbilityType.attack));
		setMaxDamage(128 - 1);
		setDescription("いけない子には");
	}

	//

	protected static class Status extends FairyWeaponStatusBase
	{

		// TODO localize
		public IFairyWeaponStatusProperty<Double> pitch = property(formula(1).mul(cost().apply(v -> Math.pow(0.5, v / 50.0 - 1))).asDouble("Pitch", pitch()));
		public IFairyWeaponStatusProperty<Double> damage = property(formula(1).add(value(dark).max(50).div(5)).add(value(christmas).max(10).div(1)).asDouble("Damage", float1()));
		public IFairyWeaponStatusProperty<Double> additionalReach = property(formula(12 - 4).add(value(wind).max(30).div(5)).asDouble("Additional Reach", float1()));
		public IFairyWeaponStatusProperty<Double> radius = property(formula(3).add(value(gaia).max(30).div(10)).asDouble("Radius", float1()));
		public IFairyWeaponStatusProperty<Integer> maxTargetCount = property(formula(4).add(value(attack).max(10).div(3)).asInt("Max Target Count", integer()));
		public IFairyWeaponStatusProperty<Integer> looting = property(formula(0).add(value(shine)).threshold(1, 2, 5, 10).asInt("Looting", integer()));
		public IFairyWeaponStatusProperty<Double> wear = property(formula(1).mul(value(fire).max(30).div(30).xp(0.5)).asDouble("Wear", percent1()));
		public IFairyWeaponStatusProperty<Double> coolTime = property(formula(0).add(cost()).mul(value(1)).mul(value(aqua).max(30).div(30).xp(0.5)).asDouble("Cool Time", percent0()));

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
		new Status().addInformation(tooltip, fairyType);
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
		Status status = new Status();

		// 発動対象
		RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach.get(fairy.y));
		Vec3d positionTarget = rayTraceResult != null
			? rayTraceResult.hitVec
			: getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach.get(fairy.y));
		List<Tuple<BlockPos, Boolean>> targets = new ArrayList<>();
		{
			List<Tuple<BlockPos, Double>> targets2 = new ArrayList<>();

			int xMin = (int) Math.floor(positionTarget.x - status.radius.get(fairy.y));
			int xMax = (int) Math.ceil(positionTarget.x + status.radius.get(fairy.y));
			int zMin = (int) Math.floor(positionTarget.z - status.radius.get(fairy.y));
			int zMax = (int) Math.ceil(positionTarget.z + status.radius.get(fairy.y));
			for (int x = xMin; x <= xMax; x++) {
				int y = (int) positionTarget.y;
				for (int z = zMin; z <= zMax; z++) {
					BlockPos blockPos = new BlockPos(x, y, z);

					double dx = (x + 0.5) - positionTarget.x;
					double dz = (z + 0.5) - positionTarget.z;
					double distance2 = dx * dx + dz * dz;
					if (distance2 <= status.radius.get(fairy.y) * status.radius.get(fairy.y)) {

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
				.map((t, i) -> Tuple.of(t.x, i < status.maxTargetCount.get(fairy.y)))
				.toList();
		}

		// 実行可能性を計算
		EnumExecutability executability = itemStack.getItemDamage() + (int) Math.ceil(status.wear.get(fairy.y)) > itemStack.getMaxDamage()
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
				resultWithFairy.status.radius.get(resultWithFairy.fairyType));

			// 対象にパーティクルを表示
			ItemMagicWandCollecting.spawnParticleTargets(
				world,
				resultWithFairy.targets,
				target -> target.y,
				target -> new Vec3d(target.x).addVector(0.5, 0.5, 0.5),
				resultWithFairy.status.maxTargetCount.get(resultWithFairy.fairyType));

		}

	}

}
