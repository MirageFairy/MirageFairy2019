package miragefairy2019.mod.modules.fairy;

import static miragefairy2019.mod.api.fairy.ManaTypes.*;
import static net.minecraft.util.text.TextFormatting.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairy.IItemFairy;
import miragefairy2019.mod.api.fairy.IManaType;
import miragefairy2019.mod.api.fairyweapon.item.IItemFairyWeapon;
import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.lib.multi.ItemMulti;
import mirrg.boron.util.UtilsString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFairy extends ItemMulti<VariantFairy> implements IItemFairy
{

	@Override
	public Optional<IFairyType> getMirageFairy2019Fairy(ItemStack itemStack)
	{
		VariantFairy variant = getVariant(itemStack).orElse(null);
		if (variant == null) return Optional.empty();
		return Optional.of(variant.type);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack)
	{
		VariantFairy variant = getVariant(itemStack).orElse(null);
		if (variant == null) return UtilsMinecraft.translateToLocal(getUnlocalizedName() + ".name");
		return UtilsMinecraft.translateToLocalFormatted(getUnlocalizedName() + ".format", variant.type.getDisplayName().getFormattedText());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{
		VariantFairy variant = getVariant(itemStack).orElse(null);
		if (variant == null) return;

		if (!flag.isAdvanced()) {
			tooltip.add("" + GREEN + "No: " + variant.type.id + " (" + variant.type.modid + ")");
			tooltip.add("" + "Type:"
				+ " " + GOLD + UtilsString.repeat("★", variant.type.rare) + getRankColor(variant) + UtilsString.repeat("★", variant.type.rank - 1)
				+ " " + WHITE + variant.type.name);
		} else {
			tooltip.add("" + GREEN + "No: " + variant.type.id + " (" + variant.type.modid + ")");
			tooltip.add("" + "Type:"
				+ " " + GOLD + UtilsString.repeat("★", variant.type.rare) + getRankColor(variant) + UtilsString.repeat("★", variant.type.rank - 1)
				+ " " + GOLD + "Rare." + variant.type.rare
				+ " " + getRankColor(variant) + "Rank." + variant.type.rank
				+ " " + WHITE + variant.type.name);
		}

		if (!flag.isAdvanced()) {
			tooltip.add("    " + f1(shine, variant));
			tooltip.add("" + f1(fire, variant) + "    " + f1(wind, variant));
			tooltip.add("" + f1(gaia, variant) + "    " + f1(aqua, variant));
			tooltip.add("    " + f1(dark, variant));
		} else {
			tooltip.add("        " + f2(shine, variant));
			tooltip.add("" + f2(fire, variant) + "    " + f2(wind, variant));
			tooltip.add("" + f2(gaia, variant) + "    " + f2(aqua, variant));
			tooltip.add("        " + f2(dark, variant));
		}

		if (!flag.isAdvanced()) {
			tooltip.add("" + DARK_PURPLE + "Cost: " + String.format("%.1f", variant.type.cost));
		} else {
			tooltip.add("" + DARK_PURPLE + "Cost: " + String.format("%.3f", variant.type.cost));
		}

		if (!flag.isAdvanced()) {
			tooltip.add(new TextComponentString("")
				.setStyle(new Style().setColor(GREEN))
				.appendText("Abilities: ")
				.appendSibling(variant.type.abilitySet.getAbilities()
					.filter(tuple -> format(tuple.y) >= 10)
					.sorted((a, b) -> -a.y.compareTo(b.y))
					.map(t -> {
						return new TextComponentString("")
							.appendSibling(t.x.getDisplayName())
							.appendText(String.format("(%s)", format(t.y)));
					})
					.sandwich(new TextComponentString(", "))
					.apply(tcs -> {
						TextComponentString textComponent = new TextComponentString("");
						tcs.forEach(textComponent::appendSibling);
						return textComponent;
					}))
				.getFormattedText());
		} else {
			tooltip.add(new TextComponentString("")
				.setStyle(new Style().setColor(GREEN))
				.appendText("Abilities: ")
				.appendSibling(variant.type.abilitySet.getAbilities()
					.filter(tuple -> format(tuple.y) >= 10)
					.sorted((a, b) -> -a.y.compareTo(b.y))
					.map(t -> {
						return new TextComponentString("")
							.appendSibling(t.x.getDisplayName())
							.appendText(String.format("(%.3f)", t.y));
					})
					.sandwich(new TextComponentString(", "))
					.apply(tcs -> {
						TextComponentString textComponent = new TextComponentString("");
						tcs.forEach(textComponent::appendSibling);
						return textComponent;
					}))
				.getFormattedText());
		}

		// 妖精武器のステータス
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null) {
			ItemStack itemStackFairyWeapon;
			itemStackFairyWeapon = player.getHeldItem(EnumHand.MAIN_HAND);
			if (itemStackFairyWeapon.getItem() instanceof IItemFairyWeapon) {
				tooltip.add(new TextComponentString("Magic: ")
					.setStyle(new Style().setColor(BLUE))
					.appendSibling(((IItemFairyWeapon) itemStackFairyWeapon.getItem()).getFairyMagicDisplayName(itemStackFairyWeapon)
						.setStyle(new Style().setColor(AQUA).setBold(true)))
					.appendText(" of ")
					.appendSibling(new TextComponentString(itemStackFairyWeapon.getDisplayName())
						.setStyle(new Style().setColor(WHITE)))
					.getFormattedText());
				((IItemFairyWeapon) itemStackFairyWeapon.getItem()).addInformationFairyWeapon(itemStackFairyWeapon, itemStack, variant.type, world, tooltip, flag);
			}
			itemStackFairyWeapon = player.getHeldItem(EnumHand.OFF_HAND);
			if (itemStackFairyWeapon.getItem() instanceof IItemFairyWeapon) {
				tooltip.add(new TextComponentString("Magic: ")
					.setStyle(new Style().setColor(BLUE))
					.appendSibling(((IItemFairyWeapon) itemStackFairyWeapon.getItem()).getFairyMagicDisplayName(itemStackFairyWeapon)
						.setStyle(new Style().setColor(AQUA).setBold(true)))
					.appendText(" of ")
					.appendSibling(new TextComponentString(itemStackFairyWeapon.getDisplayName())
						.setStyle(new Style().setColor(WHITE)))
					.getFormattedText());
				((IItemFairyWeapon) itemStackFairyWeapon.getItem()).addInformationFairyWeapon(itemStackFairyWeapon, itemStack, variant.type, world, tooltip, flag);
			}
		}

	}

	private TextFormatting getRankColor(VariantFairy variant)
	{
		switch (variant.type.rank) {
			case 1:
				return RED;
			case 2:
				return BLUE;
			case 3:
				return GREEN;
			case 4:
				return YELLOW;
			case 5:
				return DARK_GRAY;
			case 6:
				return WHITE;
			case 7:
				return AQUA;
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

	private String f1(Supplier<IManaType> sManaType, VariantFairy variant)
	{
		return "" + sManaType.get().getTextColor() + String.format("%4d", format(variant.type.manaSet.getMana(sManaType.get())));
	}

	private String f2(Supplier<IManaType> sManaType, VariantFairy variant)
	{
		return "" + sManaType.get().getTextColor() + sManaType.get().getDisplayName().getUnformattedText() + ":" + String.format("%.3f", variant.type.manaSet.getMana(sManaType.get()));
	}

}
