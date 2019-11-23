package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.modules.ore.BlockOreSeed;
import miragefairy2019.mod.modules.ore.BlockOreSeed.EnumVariant;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFairyWandCrafting extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting()
	{
		addComponent(Components.WOOD, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.craft));
		setMaxDamage(16 - 1);
		setDescription("スフィアから聞こえる、妖精の声");
	}

	@SuppressWarnings("deprecation")
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		if (ApiMain.side.isClient()) {
			if (player.isSneaking()) {

				// 鉱石生成確率表示
				List<String> lines = new ArrayList<>();
				lines.add("===== Ore List =====");
				for (EnumVariant variant : EnumVariant.values()) {
					lines.add("----- " + variant.name());
					BlockOreSeed.getList(world, player.getPosition(), variant).stream()
						.forEach(t -> lines.add(String.format("%.2f", t.weight) + ": " + t.item.get().getBlock().getItem(world, pos, t.item.get()).getDisplayName()));
				}
				lines.add("====================");
				player.sendStatusMessage(new TextComponentString(ISuppliterator.ofIterable(lines)
					.join("\n")), false);

			}
		}

		return EnumActionResult.SUCCESS;
	}

}
