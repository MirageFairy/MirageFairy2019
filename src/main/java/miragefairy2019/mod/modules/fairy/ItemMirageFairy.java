package miragefairy2019.mod.modules.fairy;

import static miragefairy2019.mod.modules.fairy.EnumManaType.*;
import static net.minecraft.util.text.TextFormatting.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import miragefairy2019.mod.lib.multi.ItemMulti;
import mirrg.boron.util.struct.Tuple;
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
			TextFormatting colorRare = LIGHT_PURPLE;

			String stringRareRank = IntStream.range(0, variant.type.rank - 1).mapToObj(i -> "★").collect(Collectors.joining());
			TextFormatting colorRareRank;
			switch (variant.type.rank) {
				case 1:
					colorRareRank = GRAY;
					break;
				case 2:
					colorRareRank = RED;
					break;
				case 3:
					colorRareRank = BLUE;
					break;
				case 4:
					colorRareRank = GREEN;
					break;
				case 5:
					colorRareRank = YELLOW;
					break;
				default:
					colorRareRank = LIGHT_PURPLE;
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
					colorRank = GRAY;
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

			tooltip.add("" + "Type: " + colorRare + stringRare + colorRareRank + stringRareRank + " " + WHITE + variant.type.name + " " + colorRank + stringRank);
		}

		tooltip.add("    " + format(shine, variant.type.manaSet.shine));
		tooltip.add("" + format(fire, variant.type.manaSet.fire) + "    " + format(wind, variant.type.manaSet.wind));
		tooltip.add("" + format(gaia, variant.type.manaSet.gaia) + "    " + format(aqua, variant.type.manaSet.aqua));
		tooltip.add("    " + format(dark, variant.type.manaSet.dark));

		tooltip.add("" + YELLOW + "Cost: " + WHITE + String.format("%.1f", variant.type.cost));

		{
			String string = variant.type.abilitySet.tuples.suppliterator()
				.sorted((a, b) -> -a.y.compareTo(b.y))
				.map(tuple -> Tuple.of(tuple.x, format(tuple.y)))
				.filter(tuple -> tuple.y >= 10)
				.map(tuple -> "" + DARK_RED + tuple.x + WHITE + "(" + tuple.y + ")")
				.join(", ");
			tooltip.add("" + GREEN + "Abilities: " + WHITE + string);
		}

	}

	private int format(double value)
	{
		int value2 = (int) value;
		if (value2 == 0 && value > 0) value2 = 1;
		return value2;
	}

	private String format(EnumManaType manaType, double value)
	{
		return "" + manaType.color + String.format("%4d", format(value));
	}

}
