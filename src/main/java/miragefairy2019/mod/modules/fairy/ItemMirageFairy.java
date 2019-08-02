package miragefairy2019.mod.modules.fairy;

import static miragefairy2019.mod.api.fairy.EnumMirageFairyManaType.*;
import static net.minecraft.util.text.TextFormatting.*;

import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairy.EnumMirageFairyManaType;
import miragefairy2019.mod.api.fairy.IItemMirageFairy;
import miragefairy2019.mod.api.fairy.MirageFairyType;
import miragefairy2019.mod.lib.multi.ItemMulti;
import mirrg.boron.util.UtilsString;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMirageFairy extends ItemMulti<VariantMirageFairy> implements IItemMirageFairy
{

	@Override
	public Optional<MirageFairyType> getMirageFairy(ItemStack itemStack)
	{
		VariantMirageFairy variant = getVariant(itemStack).orElse(null);
		if (variant == null) return Optional.empty();
		return Optional.of(variant.type);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		VariantMirageFairy variant = getVariant(stack).orElse(null);
		if (variant == null) return;

		if (!flag.isAdvanced()) {
			tooltip.add("" + "Type:"
				+ " " + variant.getMetadata()
				+ " " + GOLD + UtilsString.repeat("★", variant.type.rare) + getRankColor(variant) + UtilsString.repeat("★", variant.type.rank - 1)
				+ " " + WHITE + variant.type.name);
		} else {
			tooltip.add("" + "Type:"
				+ " No." + variant.getMetadata()
				+ " " + GOLD + "Rare." + variant.type.rare
				+ " " + getRankColor(variant) + "Rank." + variant.type.rank
				+ " " + WHITE + variant.type.name);
		}

		if (!flag.isAdvanced()) {
			tooltip.add("    " + format1(shine, variant.type.manaSet.shine));
			tooltip.add("" + format1(fire, variant.type.manaSet.fire) + "    " + format1(wind, variant.type.manaSet.wind));
			tooltip.add("" + format1(gaia, variant.type.manaSet.gaia) + "    " + format1(aqua, variant.type.manaSet.aqua));
			tooltip.add("    " + format1(dark, variant.type.manaSet.dark));
		} else {
			tooltip.add("    " + format2(shine, variant.type.manaSet.shine));
			tooltip.add("" + format2(fire, variant.type.manaSet.fire) + "    " + format2(wind, variant.type.manaSet.wind));
			tooltip.add("" + format2(gaia, variant.type.manaSet.gaia) + "    " + format2(aqua, variant.type.manaSet.aqua));
			tooltip.add("    " + format2(dark, variant.type.manaSet.dark));
		}

		if (!flag.isAdvanced()) {
			tooltip.add("" + YELLOW + "Cost: " + WHITE + String.format("%.3f", variant.type.cost));
		} else {
			tooltip.add("" + YELLOW + "Cost: " + WHITE + String.format("%.1f", variant.type.cost));
		}

		if (!flag.isAdvanced()) {
			String string = variant.type.abilitySet.tuples.suppliterator()
				.sorted((a, b) -> -a.y.compareTo(b.y))
				.map(tuple -> Tuple.of(tuple.x, format(tuple.y)))
				.filter(tuple -> tuple.y >= 10)
				.map(tuple -> "" + tuple.x.getTextColor() + tuple.x.getLocalizedName() + WHITE + "(" + tuple.y + ")")
				.join(", ");
			tooltip.add("" + GREEN + "Abilities: " + WHITE + string);
		} else {
			String string = variant.type.abilitySet.tuples.suppliterator()
				.sorted((a, b) -> -a.y.compareTo(b.y))
				.filter(tuple -> format(tuple.y) >= 10)
				.map(tuple -> "" + tuple.x.getTextColor() + tuple.x.getLocalizedName() + WHITE + "(" + String.format("%.3f", tuple.y) + ")")
				.join(", ");
			tooltip.add("" + GREEN + "Abilities: " + WHITE + string);
		}

	}

	private TextFormatting getRankColor(VariantMirageFairy variant)
	{
		switch (variant.type.rank) {
			case 1:
				return GRAY;
			case 2:
				return RED;
			case 3:
				return BLUE;
			case 4:
				return GREEN;
			case 5:
				return YELLOW;
			default:
				return LIGHT_PURPLE;
		}
	}

	private int format(double value)
	{
		int value2 = (int) value;
		if (value2 == 0 && value > 0) value2 = 1;
		return value2;
	}

	private String format1(EnumMirageFairyManaType manaType, double value)
	{
		return "" + manaType.colorText + String.format("%4d", format(value));
	}

	private String format2(EnumMirageFairyManaType manaType, double value)
	{
		return "" + manaType.colorText + String.format("%8.3f", value);
	}

}
