package miragefairy2019.mod.modules.fairyweapon.item;

import static miragefairy2019.mod.api.fairy.AbilityTypes.*;
import static miragefairy2019.mod.api.fairy.ManaTypes.*;

import java.util.List;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.damagesource.IDamageSourceLooting;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.modules.fairyweapon.magic.IExecutorRightClick;
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorEntityRanged;
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace;
import miragefairy2019.mod.modules.fairyweapon.status.FairyWeaponStatusBase;
import miragefairy2019.mod.modules.fairyweapon.status.IFairyWeaponStatusProperty;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBellChristmas extends ItemBellBase
{

	// TODO localize
	protected static class Status extends FairyWeaponStatusBase
	{

		public IFairyWeaponStatusProperty<Double> pitch = property(
			formula(1)
				.mul(cost().apply(v -> Math.pow(0.5, v / 50.0 - 1)))
				.asDouble("Pitch", pitch()));

		public IFairyWeaponStatusProperty<Double> damage = property(
			formula(1)
				.add(value(dark.get()).max(90).div(90).pow(1 / 3.0).mul(5))
				.add(value(christmas.get()).max(20).div(2))
				.asDouble("Damage", float1()));

		public IFairyWeaponStatusProperty<Double> additionalReach = property(
			formula(8 - 4)
				.add(value(wind.get()).max(30).div(5))
				.asDouble("Additional Reach", float1()));

		public IFairyWeaponStatusProperty<Double> radius = property(
			formula(3)
				.add(value(gaia.get()).max(30).div(10))
				.asDouble("Radius", float1()));

		public IFairyWeaponStatusProperty<Integer> maxTargetCount = property(
			formula(2)
				.add(value(dark.get()).max(90).div(90).pow(1 / 3.0).mul(3))
				.add(value(attack.get()).max(10).div(3))
				.asInt("Max Target Count", integer()));

		public IFairyWeaponStatusProperty<Integer> looting = property(
			formula(0)
				.add(value(shine.get()))
				.threshold(1, 2, 5, 10)
				.asInt("Looting", integer()));

		public IFairyWeaponStatusProperty<Double> wear = property(
			formula(1)
				.mul(cost().div(50))
				.mul(value(fire.get()).max(30).div(30).xp(0.5))
				.mul(value(aqua.get()).max(30).div(30).xp(0.5))
				.asDouble("Wear", percent1()));

		public IFairyWeaponStatusProperty<Double> coolTime = property(
			formula(0)
				.add(cost())
				.mul(value(0.5))
				.mul(value(dark.get()).max(90).div(90).pow(1 / 3.0).xp(0.5))
				.mul(value(submission.get()).max(10).div(10).xp(0.5))
				.asDouble("Cool Time", float0()));

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
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		new Status().addInformation(tooltip, fairyType);
	}

	public IExecutorRightClick getExecutor(World world, ItemStack itemStack, EntityPlayer player)
	{
		Item item = this;

		// 妖精取得
		Tuple<ItemStack, IFairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy == null) {

			// 視線判定
			SelectorRayTrace selectorRayTrace = new SelectorRayTrace(world, player, 0);

			return new IExecutorRightClick() {
				@Override
				public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
				{
					return new ActionResult<>(EnumActionResult.PASS, itemStack);
				}

				@Override
				public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
				{
					selectorRayTrace.effect(0xFF00FF);
				}
			};
		} else {

			IFairyType fairyType = fairy.y;

			// ステータスを評価
			Status status = new Status();

			// 視線判定
			SelectorRayTrace selectorRayTrace = new SelectorRayTrace(world, player, status.additionalReach.get(fairyType));

			// 対象判定
			SelectorEntityRanged<EntityLivingBase> selectorEntityRanged = new SelectorEntityRanged<>(
				world,
				selectorRayTrace.getTarget(),
				EntityLivingBase.class,
				e -> e != player,
				status.radius.get(fairyType),
				status.maxTargetCount.get(fairyType));

			// 実行可能性を計算
			boolean ok;
			int color;
			if (itemStack.getItemDamage() + (int) Math.ceil(status.wear.get(fairy.y)) > itemStack.getMaxDamage()) {
				ok = false;
				color = 0xFF0000;
			} else if (selectorEntityRanged.getEffectiveEntities().count() == 0) {
				ok = false;
				color = 0x00FFFF;
			} else if (player.getCooldownTracker().hasCooldown(item)) {
				ok = false;
				color = 0xFFFF00;
			} else {
				ok = true;
				color = 0xFFFFFF;
			}

			return new IExecutorRightClick() {
				@Override
				public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
				{
					if (!ok) return new ActionResult<>(EnumActionResult.PASS, itemStack);

					int targetCount = 0;
					for (EntityLivingBase target : selectorEntityRanged.getEffectiveEntities()) {

						// 耐久が足りないので中止
						if (itemStack.getItemDamage() + (int) Math.ceil(status.wear.get(fairyType)) > itemStack.getMaxDamage()) break;

						// パワーが足りないので中止
						if (targetCount >= status.maxTargetCount.get(fairyType)) break;

						// 行使
						itemStack.damageItem(Utils.randomInt(world.rand, status.wear.get(fairyType)), player);
						targetCount++;
						{
							double damage = status.damage.get(fairyType);

							if (target.isEntityUndead()) damage *= 1.5;

							target.attackEntityFrom(new DamageSourceFairyMagic(player, status.looting.get(fairyType)), (float) damage);
						}

					}

					if (targetCount >= 1) {

						// エフェクト
						ItemBellBase.playSound(world, player, status.pitch.get(fairyType));

						// クールタイム
						player.getCooldownTracker().setCooldown(item, (int) (double) status.coolTime.get(fairyType));

					}

					return new ActionResult<>(targetCount >= 1 ? EnumActionResult.SUCCESS : EnumActionResult.PASS, itemStack);
				}

				@Override
				public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
				{
					selectorRayTrace.effect(color);
					selectorEntityRanged.effect();
				}
			};
		}

	}

	public static class DamageSourceFairyMagic extends EntityDamageSource implements IDamageSourceLooting
	{

		private int lootingLevel;

		public DamageSourceFairyMagic(Entity damageSourceEntity, int lootingLevel)
		{
			super("indirectMagic", damageSourceEntity);
			this.lootingLevel = lootingLevel;
			setDamageBypassesArmor();
			setMagicDamage();
		}

		@Override
		public int getLootingLevel()
		{
			return lootingLevel;
		}

	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{

		// アイテム取得
		ItemStack itemStack = player.getHeldItem(hand);

		return getExecutor(world, itemStack, player).onItemRightClick(world, player, hand);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{

		// クライアントのみ
		if (!ApiMain.side().isClient()) return;

		// プレイヤー取得
		if (!(entity instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) entity;

		// アイテム取得
		if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

		getExecutor(world, itemStack, player).onUpdate(itemStack, world, entity, itemSlot, isSelected);

	}

}
