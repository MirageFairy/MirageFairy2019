package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyWeaponBase;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLightMagicWand extends ItemFairyWeaponBase
{

	public ItemLightMagicWand()
	{
		composite = composite
			.add(Components.APATITE, 4)
			.add(Components.MIRAGIUM, 2)
			.add(Components.fairyAbilityType(EnumAbilityType.light));
		setMaxDamage(64 - 1);
	}

	//

	protected static class Status
	{

		public final double additionalReach;
		public final double coolTime;

		public Status(FairyType fairyType)
		{
			additionalReach = Math.min(fairyType.manaSet.aqua / 50.0 * 20, 40);
			coolTime = fairyType.cost * 2 * Math.pow(0.5, fairyType.manaSet.gaia / 30);
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("優しい光が洞窟を照らす");

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
		tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Aqua)");
		tooltip.add(TextFormatting.BLUE + "Cool Time: " + ((int) status.coolTime) + "t (Gaia, Cost)");

	}

	//

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack itemStack = player.getHeldItem(hand);
		if (world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		// 妖精を取得
		Tuple<ItemStack, FairyType> fairy = findFairy(itemStack, player).orElse(null);
		if (fairy == null) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		// ステータスを評価
		Status status = new Status(fairy.y);

		// 松明検索
		ItemStack itemStackTorch = findTorch(player).orElse(null);
		if (itemStackTorch == null) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		// 視線判定
		RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
		if (rayTraceResult == null) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		BlockPos blockPos = rayTraceResult.getBlockPos();

		// 置換不可能な場合はそのブロックの表面に対象を変更
		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (!block.isReplaceable(world, blockPos)) {
			blockPos = blockPos.offset(rayTraceResult.sideHit);
		}

		// 対象が変更不能なら失敗
		if (!player.canPlayerEdit(blockPos, rayTraceResult.sideHit, itemStack)) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		// 対象が置換できないブロックの場合は失敗
		if (!world.mayPlace(Blocks.TORCH, blockPos, false, rayTraceResult.sideHit, player)) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		int meta = 0;
		boolean result = world.setBlockState(blockPos, Blocks.TORCH.getStateForPlacement(
			world,
			blockPos,
			rayTraceResult.sideHit,
			(float) rayTraceResult.hitVec.x,
			(float) rayTraceResult.hitVec.y,
			(float) rayTraceResult.hitVec.z,
			meta,
			player,
			hand), 2);

		// 設置失敗した場合は失敗
		if (!result) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		if (world.getBlockState(blockPos).getBlock() != Blocks.TORCH) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

		// エフェクト
		IBlockState blockState2 = world.getBlockState(blockPos);
		SoundType soundtype = blockState2.getBlock().getSoundType(blockState2, world, blockPos, player);
		world.playSound(player, blockPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

		//消費
		itemStack.damageItem(1, player);
		itemStackTorch.shrink(1);

		// クールタイム
		player.getCooldownTracker().setCooldown(this, (int) status.coolTime);

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	protected Optional<ItemStack> findTorch(EntityPlayer player)
	{
		ItemStack itemStack;

		itemStack = player.getHeldItem(EnumHand.OFF_HAND);
		if (itemStack.getItem() == Item.getItemFromBlock(Blocks.TORCH)) return Optional.of(itemStack);

		itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (itemStack.getItem() == Item.getItemFromBlock(Blocks.TORCH)) return Optional.of(itemStack);

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

			itemStack = player.inventory.getStackInSlot(i);
			if (itemStack.getItem() == Item.getItemFromBlock(Blocks.TORCH)) return Optional.of(itemStack);

		}

		return Optional.empty();
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			if (isSelected || player.getHeldItemOffhand() == itemStack) {

				if (ApiMain.side.isClient()) {

					// 妖精がない場合はマゼンタ
					Tuple<ItemStack, FairyType> fairy = findFairy(itemStack, player).orElse(null);
					if (fairy == null) {
						spawnParticle(
							world,
							getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()),
							0xFF00FF);
						return;
					}

					// 松明検索
					ItemStack itemStackTorch = findTorch(player).orElse(null);
					if (itemStackTorch == null) {
						spawnParticle(
							world,
							getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()),
							0xFF00FF);
						return;
					}

					// ステータスを評価
					Status status = new Status(fairy.y);

					// 耐久がない場合は赤
					// 対象が発動対象でない場合は緑
					// クールタイムの場合は黄色
					RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
					if (rayTraceResult == null) {
						spawnParticle(
							world,
							getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach),
							itemStack.getItemDamage() >= itemStack.getMaxDamage() ? 0xFF0000 : player.getCooldownTracker().hasCooldown(this) ? 0x00FF00 : 0x00FFFF);
						return;
					}
					if (!canExecute(world, rayTraceResult)) {
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

	protected boolean canExecute(World world, RayTraceResult rayTraceResult)
	{
		return rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK;
	}

}
