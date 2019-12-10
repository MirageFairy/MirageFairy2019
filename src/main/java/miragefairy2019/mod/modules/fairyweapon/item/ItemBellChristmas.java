package miragefairy2019.mod.modules.fairyweapon.item;

import static miragefairy2019.mod.api.fairy.EnumManaType.*;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.EnumManaType;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.modules.fairyweapon.status.IFairyWeaponStatusHelper;
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

	protected static class Status implements IFairyWeaponStatusHelper
	{

		// TODO localize
		public final IFairyWeaponStatusProperty<Double> pitch = formula(1).mul(cost().apply(v -> Math.pow(0.5, v / 50.0 - 1))).asDouble("Pitch", pitch());

		// TODO delete
		private final FairyType fairyType;

		// TODO convert
		public final double damage;
		public final double additionalReach;
		public final double radius;
		public final int maxTargetCount;
		public final int looting;
		public final double wear;
		public final double coolTime;

		public Status(FairyType fairyType)
		{
			this.fairyType = fairyType;

			// TODO delete
			damage = 1 + get(dark, 50, 5) + get(EnumAbilityType.christmas, 10, 1);
			additionalReach = (12 - 4) + get(wind, 30, 5);
			radius = 3 + get(gaia, 30, 10);
			maxTargetCount = 4 + (int) get(EnumAbilityType.attack, 10, 3);
			looting = get(shine) >= 10
				? 4
				: get(shine) >= 5
					? 3
					: get(shine) >= 2
						? 2
						: get(shine) >= 1
							? 1
							: 0;
			wear = 1.0 * Math.pow(0.5, get(fire, 30, 30));
			coolTime = cost2() * 1 * Math.pow(0.5, get(aqua, 30, 30));
		}

		// TODO delete
		private double cost2()
		{
			return fairyType.cost;
		}

		// TODO delete
		private double get(EnumManaType manaType)
		{
			return fairyType.manaSet.get(manaType);
		}

		// TODO delete
		private double get(EnumManaType manaType, int max, int div)
		{
			return Math.min(fairyType.manaSet.get(manaType), max) / div;
		}

		// TODO delete
		private double get(EnumAbilityType abilityType, int max, int div)
		{
			return Math.min(fairyType.abilitySet.get(abilityType) * (fairyType.cost / 50.0), max) / div;
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

		IFairyWeaponStatusProperty<?> proterty = status.pitch;

		// TODO list
		String localizedSourceListString = proterty.getLocalizedSourceListString();
		tooltip.add(String.format(TextFormatting.BLUE + "%s: %s%s",
			proterty.getLocalizedName(),
			proterty.getString(fairyType),
			localizedSourceListString.isEmpty() ? "" : " (" + localizedSourceListString + ")"));

		// TODO convert
		tooltip.add(TextFormatting.BLUE + "Damage: " + String.format("%.1f", status.damage) + " (Dark, " + EnumAbilityType.christmas.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Wind)");
		tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Gaia)");
		tooltip.add(TextFormatting.BLUE + "Max Targets: " + String.format("%d", status.maxTargetCount) + " (" + EnumAbilityType.attack.getLocalizedName() + ")");
		tooltip.add(TextFormatting.BLUE + "Looting: " + String.format("%d", status.looting) + " (Shine)");
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
				target -> new Vec3d(target.x).addVector(0.5, 0.5, 0.5),
				resultWithFairy.status.maxTargetCount);

		}

	}

}
