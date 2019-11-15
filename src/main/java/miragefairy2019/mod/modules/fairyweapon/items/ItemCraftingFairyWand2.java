package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyCraftingToolBase;
import miragefairy2019.mod.modules.ore.BlockOreSeed;
import miragefairy2019.mod.modules.ore.BlockOreSeed.EnumVariant;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCraftingFairyWand2 extends ItemFairyCraftingToolBase
{

	public ItemCraftingFairyWand2()
	{
		super(Composite.empty()
			.add(Components.MIRAGIUM, 1)
			.add(Components.fairyAbilityType(EnumAbilityType.craft), 1));
		this.setMaxDamage(32 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("腕が4本欲しくなったときにどうぞ");

		super.addInformation(itemStack, world, tooltip, flag);

	}

	@SuppressWarnings("deprecation")
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		if (ApiMain.side.isClient()) {

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
