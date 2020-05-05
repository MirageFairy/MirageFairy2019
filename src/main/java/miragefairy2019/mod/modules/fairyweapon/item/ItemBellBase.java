package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.List;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBellBase extends ItemFairyWeaponBase
{

	protected static class Status
	{

		public final double pitch;

		public Status(IFairyType fairyType)
		{
			pitch = 1.0 * Math.pow(0.5, fairyType.getCost() / 50.0 - 1);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		tooltip.add(TextFormatting.RED + "Right click to sound");

		super.addInformationFunctions(itemStack, world, tooltip, flag);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		Status status = new Status(fairyType);
		tooltip.add(TextFormatting.BLUE + "Pitch: " + String.format("%.2f", Math.log(status.pitch) / Math.log(2) * 12) + " (Cost)");
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

		public Result(
			EnumExecutability executability)
		{
			this.executability = executability;
		}

	}

	private static class ResultWithFairy extends Result
	{

		public final IFairyType fairyType;
		public final Status status;

		public ResultWithFairy(
			EnumExecutability executability,
			IFairyType fairyType,
			Status status)
		{
			super(executability);
			this.fairyType = fairyType;
			this.status = status;
		}

	}

	// /fill ~-10 ~ ~-10 ~10 ~ ~10 miragefairy2019:mirage_flower 3
	private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player)
	{

		// 妖精取得
		Tuple<ItemStack, IFairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy == null) {
			return new Result(EnumExecutability.NO_FAIRY);
		}

		// ステータスを評価
		Status status = new Status(fairy.y);

		// 実行可能性を計算
		EnumExecutability executability = EnumExecutability.OK;

		return new ResultWithFairy(executability, fairy.y, status);
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

		// エフェクト
		playSound(world, player, resultWithFairy.status.pitch);

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	public static void playSound(World world, EntityPlayer player, double pitch)
	{
		world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, (float) pitch);
	}

}
