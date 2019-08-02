package miragefairy2019.mod.modules.sphere;

import java.util.function.Supplier;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.fairy.IAbilityType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public enum EnumSphere
{
	attack(EnumAbilityType.attack, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_SWORD))),
	craft(EnumAbilityType.craft, 0xADADAD, 0xFFC8A4, 0xB57919, 0xDCDCDC, () -> Ingredient.fromStacks(new ItemStack(Blocks.CRAFTING_TABLE))),
	fell(EnumAbilityType.fell, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_AXE))),
	light(EnumAbilityType.light, 0xFFFFFF, 0x848484, 0x000000, 0xFFFFFF, () -> Ingredient.fromStacks(new ItemStack(Blocks.TORCH))),
	flame(EnumAbilityType.flame, 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.FLINT_AND_STEEL))),
	water(EnumAbilityType.water, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, () -> Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET))),
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
	}

}
