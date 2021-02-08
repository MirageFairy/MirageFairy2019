package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.modules.fairyweapon.magic.MagicExecutor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemFairyWeaponBase2 extends ItemFairyWeaponBase
{

	protected MagicExecutor getExecutor(World world, ItemStack itemStack, EntityPlayer player)
	{
		return new MagicExecutor();
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
