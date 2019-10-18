package miragefairy2019.mod.modules.sphere;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.lib.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public enum EnumSphere
{
	attack(EnumAbilityType.attack, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_SWORD))),
	craft(EnumAbilityType.craft, 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC, () -> Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE))),
	fell(EnumAbilityType.fell, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_AXE))),
	light(EnumAbilityType.light, 0xE0F8FF, 0xFFFFE0, 0xFFFFFF, 0xFFDD3E, () -> Ingredient.fromStacks(new ItemStack(Blocks.TORCH))),
	flame(EnumAbilityType.flame, 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.FLINT_AND_STEEL))),
	water(EnumAbilityType.water, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, () -> Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET))),
	crystal(EnumAbilityType.crystal, 0xA2FFFF, 0xB6FFFF, 0x36CECE, 0xEBFFFF, () -> Ingredient.fromStacks(new ItemStack(Items.DIAMOND))),
	art(EnumAbilityType.art, 0xFF5353, 0x41C6FF, 0xFFFF84, 0x00C800, () -> Ingredient.fromStacks(new ItemStack(Items.PAINTING))),
	store(EnumAbilityType.store, 0xDCDCDC, 0xEBA242, 0xC47F25, 0x404040, () -> Ingredient.fromStacks(new ItemStack(Blocks.CHEST))),
	warp(EnumAbilityType.warp, 0x3A00D3, 0x8CF4E2, 0x349988, 0xD004FB, () -> Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL))),
	shoot(EnumAbilityType.shoot, 0x969696, 0x896727, 0x896727, 0xD8D8D8, () -> Ingredient.fromStacks(new ItemStack(Items.BOW))),
	breaking(EnumAbilityType.breaking, 0xFFFFFF, 0xFF5A35, 0xFF4800, 0x000000, () -> Ingredient.fromStacks(new ItemStack(Blocks.TNT))),
	;

	public final IAbilityType abilityType;
	public final int colorCore;
	public final int colorHighlight;
	public final int colorBackground;
	public final int colorPlasma;
	public final Supplier<Ingredient> sIngredient;

	private EnumSphere(IAbilityType abilityType, int colorCore, int colorHighlight, int colorBackground, int colorPlasma, Supplier<Ingredient> sIngredient)
	{
		this.abilityType = abilityType;
		this.colorCore = colorCore;
		this.colorHighlight = colorHighlight;
		this.colorBackground = colorBackground;
		this.colorPlasma = colorPlasma;
		this.sIngredient = sIngredient;
		A.map.put(abilityType, this);
	}

	public String getOreName()
	{
		return "mirageFairy2019Sphere" + Utils.toUpperCaseHead(abilityType.getName());
	}

	private static class A
	{

		private static Map<IAbilityType, EnumSphere> map = new HashMap<>();

	}

	public static Optional<EnumSphere> of(IAbilityType abilityType)
	{
		return Optional.ofNullable(A.map.get(abilityType));
	}

}
