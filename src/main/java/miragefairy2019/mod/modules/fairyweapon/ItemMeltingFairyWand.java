package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;
import java.util.function.Predicate;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.sphere.EnumSphere;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;

public class ItemMeltingFairyWand extends ItemFairyCraftingToolBase
{

	public ItemMeltingFairyWand()
	{
		super(Composite.empty()
			.add(Components.WOOD, 2)
			.add(Components.fairyAbilityType(EnumAbilityType.flame), 1));
		this.setMaxDamage(16 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("金属を溶かすほどの情熱");

		// アイテムステータス
		tooltip.add(TextFormatting.GREEN + "Durability: " + (getMaxDamage(itemStack) - getDamage(itemStack)) + " / " + getMaxDamage(itemStack));

		// 素材
		tooltip.add(TextFormatting.YELLOW + "Contains: Wood(2.000), Sphere of \"FLAME\"");

		// 機能
		tooltip.add(TextFormatting.RED + "Can be repaired by crafting with contained sphere");

		super.addInformation(itemStack, world, tooltip, flag);

	}

	//

	@Override
	public NonNullList<Predicate<ItemStack>> getRepaitmentSpheres(ItemStack itemStack)
	{
		return ISuppliterator.of(
			new OreIngredient(EnumSphere.flame.getOreName()))
			.toCollection(NonNullList::create);
	}

}
