package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;
import java.util.function.Predicate;

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

public class ItemCraftingFairyWand extends ItemFairyCraftingToolBase
{

	public ItemCraftingFairyWand()
	{
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
	public NonNullList<Predicate<ItemStack>> getRepaitmentSpheres(ItemStack itemStack)
	{
		return ISuppliterator.of(
			new OreIngredient(EnumSphere.craft.getOreName()))
			.toCollection(NonNullList::create);
	}

}
