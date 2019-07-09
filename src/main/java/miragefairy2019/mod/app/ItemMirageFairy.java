package miragefairy2019.mod.app;

import static miragefairy2019.mod.modules.fairy.EnumManaType.*;
import static net.minecraft.util.text.TextFormatting.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import miragefairy2019.mod.lib.multi.ItemMulti;
import miragefairy2019.mod.modules.fairy.EnumManaType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMirageFairy extends ItemMulti<VariantMirageFairy>
{

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		VariantMirageFairy variant = getVariant(stack).orElse(null);
		if (variant == null) return;

		{
			String stringRare = IntStream.range(0, variant.type.rare).mapToObj(i -> "★").collect(Collectors.joining());
			TextFormatting colorRare;
			switch (variant.type.rare) {
				case 1:
					colorRare = GRAY;
					break;
				case 2:
					colorRare = RED;
					break;
				case 3:
					colorRare = BLUE;
					break;
				case 4:
					colorRare = GREEN;
					break;
				case 5:
					colorRare = YELLOW;
					break;
				default:
					colorRare = GRAY;
					break;
			}

			String stringRank;
			int rank = variant.type.rank;
			switch (rank) {
				case 1:
					stringRank = "I";
					break;
				case 2:
					stringRank = "II";
					break;
				case 3:
					stringRank = "III";
					break;
				case 4:
					stringRank = "IV";
					break;
				case 5:
					stringRank = "V";
					break;
				default:
					stringRank = "" + rank;
					break;
			}
			TextFormatting colorRank;
			switch (variant.type.rank) {
				case 1:
					colorRank = WHITE;
					break;
				case 2:
					colorRank = RED;
					break;
				case 3:
					colorRank = BLUE;
					break;
				case 4:
					colorRank = GREEN;
					break;
				case 5:
					colorRank = YELLOW;
					break;
				default:
					colorRank = LIGHT_PURPLE;
					break;
			}

			tooltip.add("" + "Type: " + colorRare + stringRare + " " + colorRank + variant.type.name + " " + stringRank);
		}

		tooltip.add("    " + format(shine, variant.type.shine));
		tooltip.add("" + format(fire, variant.type.fire) + "    " + format(wind, variant.type.wind));
		tooltip.add("" + format(gaia, variant.type.gaia) + "    " + format(aqua, variant.type.aqua));
		tooltip.add("    " + format(dark, variant.type.dark));

		tooltip.add("" + YELLOW + "Cost: " + WHITE + String.format("%.1f", variant.type.cost));

	}

	private String format(EnumManaType manaType, double value)
	{
		int value2 = (int) value;
		if (value2 == 0 && value > 0) value2 = 1;
		return "" + manaType.color + String.format("%4d", value2);
	}

}
