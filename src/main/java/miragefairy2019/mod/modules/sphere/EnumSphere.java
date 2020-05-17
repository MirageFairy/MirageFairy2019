package miragefairy2019.mod.modules.sphere;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.IAbilityType;
import mirrg.boron.util.UtilsString;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public enum EnumSphere
{
	attack(AbilityTypes.attack.get(), 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_SWORD))),
	craft(AbilityTypes.craft.get(), 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC, () -> Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE))),
	fell(AbilityTypes.fell.get(), 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_AXE))),
	light(AbilityTypes.light.get(), 0xE0F8FF, 0xFFFFE0, 0xFFFFFF, 0xFFDD3E, () -> Ingredient.fromStacks(new ItemStack(Blocks.TORCH))),
	flame(AbilityTypes.flame.get(), 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.FLINT_AND_STEEL))),
	water(AbilityTypes.water.get(), 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, () -> Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET))),
	crystal(AbilityTypes.crystal.get(), 0xA2FFFF, 0xB6FFFF, 0x36CECE, 0xEBFFFF, () -> Ingredient.fromStacks(new ItemStack(Items.DIAMOND))),
	art(AbilityTypes.art.get(), 0xFF5353, 0x41C6FF, 0xFFFF84, 0x00C800, () -> Ingredient.fromStacks(new ItemStack(Items.PAINTING))),
	store(AbilityTypes.store.get(), 0xDCDCDC, 0xEBA242, 0xC47F25, 0x404040, () -> Ingredient.fromStacks(new ItemStack(Blocks.CHEST))),
	warp(AbilityTypes.warp.get(), 0x3A00D3, 0x8CF4E2, 0x349988, 0xD004FB, () -> Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL))),
	shoot(AbilityTypes.shoot.get(), 0x969696, 0x896727, 0x896727, 0xD8D8D8, () -> Ingredient.fromStacks(new ItemStack(Items.BOW))),
	breaking(AbilityTypes.breaking.get(), 0xFFFFFF, 0xFF5A35, 0xFF4800, 0x000000, () -> Ingredient.fromStacks(new ItemStack(Blocks.TNT))),
	chemical(AbilityTypes.chemical.get(), 0x0067FF, 0xC9DFEF, 0xB0C4D7, 0x0755FF, () -> Ingredient.fromStacks(new ItemStack(Items.FERMENTED_SPIDER_EYE))),
	slash(AbilityTypes.slash.get(), 0xAAAAAA, 0xFFC9B2, 0xD20000, 0xFFFFFF, () -> Ingredient.fromStacks(new ItemStack(Items.SHEARS))),
	food(AbilityTypes.food.get(), 0xC66000, 0xFFCF86, 0xCB6E00, 0xFFC261, () -> Ingredient.fromStacks(new ItemStack(Items.BREAD))),
	knowledge(AbilityTypes.knowledge.get(), 0xFFFF00, 0x006200, 0x00A000, 0x50DD00, () -> Ingredient.fromStacks(new ItemStack(Items.BOOK))),
	energy(AbilityTypes.energy.get(), 0xFFED30, 0xFFF472, 0xFFE84C, 0xBFE7FF, () -> Ingredient.fromStacks(new ItemStack(Items.COAL))),
	submission(AbilityTypes.submission.get(), 0xFF0000, 0x593232, 0x1E1E1E, 0xA90000, () -> Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS))),
	christmas(AbilityTypes.christmas.get(), 0xFF0000, 0xFFD723, 0x00B900, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 1))),
	freeze(AbilityTypes.freeze.get(), 0x5AFFFF, 0xFFFFFF, 0xF6FFFF, 0xACFFFF, () -> Ingredient.fromStacks(new ItemStack(Blocks.ICE))),
	thunder(AbilityTypes.thunder.get(), 0xEAFFFF, 0x2B2BFF, 0x000076, 0xFFFF00, () -> Ingredient.fromStacks(new ItemStack(Items.GOLDEN_SWORD))),
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
		return "mirageFairy2019Sphere" + UtilsString.toUpperCaseHead(abilityType.getName());
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
