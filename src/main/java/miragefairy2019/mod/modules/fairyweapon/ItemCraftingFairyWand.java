package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.sphere.EnumSphere;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;

public class ItemCraftingFairyWand extends ItemFairyCraftingToolBase
{

	public ItemCraftingFairyWand()
	{
		super(Composite.empty()
			.add(Components.WOOD, 2)
			.add(Components.fairyAbilityType(EnumAbilityType.craft), 1));
		this.setMaxDamage(16 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("スフィアから聞こえる、妖精の声");

		// アイテムステータス
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));

		// 素材
		tooltip.add(TextFormatting.YELLOW + "Contains: Wood(2.000), Sphere of \"CRAFT\"");
		tooltip.add(TextFormatting.RED + "Can be repaired by crafting with contained sphere");

		// 機能

		super.addInformation(itemStack, world, tooltip, flag);

	}

	//

	@Override
	public NonNullList<Ingredient> getRepaitmentSpheres(ItemStack itemStack)
	{
		return ISuppliterator.of(
			new OreIngredient(EnumSphere.craft.getOreName()))
			.toCollection(NonNullList::create);
	}

}
