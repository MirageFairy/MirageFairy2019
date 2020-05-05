package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.modules.ore.BlockOreSeed;
import miragefairy2019.mod.modules.ore.BlockOreSeed.EnumVariant;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFairyWandCrafting extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandCrafting()
	{
		addComponent(Components.wood.get(), 1);
		addComponent(ApiFairy.getComponentAbilityType(AbilityTypes.craft.get()));
		setMaxDamage(16 - 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		// 指定の妖精を持っていない場合、抜ける
		a:
		{
			ItemStack itemStackFairy = getCombinedFairy(player.getHeldItem(hand));
			if (getFairy(itemStackFairy).isPresent()) {
				if (getFairy(itemStackFairy).get().getName().equals(new ResourceLocation(ModMirageFairy2019.MODID, "mina"))) {
					break a;
				}
			}
			return EnumActionResult.PASS;
		}

		if (!world.isRemote) {

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

		return EnumActionResult.SUCCESS;
	}

}
