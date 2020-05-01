package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.mirageflower.BlockMirageFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFairyWandMelting extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandMelting()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.flame));
		setMaxDamage(32 - 1);
		setDescription("金属を溶かすほどの情熱");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		// 指定の妖精を持っていない場合、抜ける
		a:
		{
			ItemStack itemStackFairy = getCombinedFairy(player.getHeldItem(hand));
			if (getFairy(itemStackFairy).isPresent()) {
				if (getFairy(itemStackFairy).get().modid.equals(ModuleFairy.FairyTypes.mina[0].type.modid)) {
					if (getFairy(itemStackFairy).get().name.equals(ModuleFairy.FairyTypes.mina[0].type.name)) {
						break a;
					}
				}
			}
			return EnumActionResult.PASS;
		}

		if (!world.isRemote) {
			if (player.isSneaking()) {
				player.sendStatusMessage(BlockMirageFlower.getGrowRateTableMessage(), false);
			} else {
				player.sendStatusMessage(BlockMirageFlower.getGrowRateMessage(world, pos.offset(facing)), false);
			}
		}

		return EnumActionResult.SUCCESS;
	}

}
