package miragefairy2019.mod.modules.fairyweapon.items;

import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.modules.fairyweapon.ItemFairyWeaponBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOcarinaBase extends ItemFairyWeaponBase
{

	public ItemOcarinaBase()
	{
		super(Composite.empty()
			.add(Components.MIRAGIUM, 1)
			.add(Components.APATITE, 4)
			.add(Components.fairyAbilityType(EnumAbilityType.art), 1));
		this.setMaxDamage(128 - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// ポエム
		tooltip.add("適当に吹いても音楽になる笛");

		super.addInformation(itemStack, world, tooltip, flag);

	}

}
