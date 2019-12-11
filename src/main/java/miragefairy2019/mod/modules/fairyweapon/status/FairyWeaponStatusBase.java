package miragefairy2019.mod.modules.fairyweapon.status;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairy.FairyType;
import net.minecraft.util.text.TextFormatting;

public abstract class FairyWeaponStatusBase implements IFairyWeaponStatusHelper
{

	public List<IFairyWeaponStatusProperty<?>> properties = new ArrayList<>();

	protected <P extends IFairyWeaponStatusProperty<?>> P property(P property)
	{
		properties.add(property);
		return property;
	}

	public void addInformation(List<String> tooltip, FairyType fairyType)
	{
		for (IFairyWeaponStatusProperty<?> property : properties) {
			String localizedSourceListString = property.getLocalizedSourceListString();
			tooltip.add(String.format(TextFormatting.BLUE + "%s: %s%s",
				property.getLocalizedName(),
				property.getString(fairyType),
				localizedSourceListString.isEmpty() ? "" : " (" + localizedSourceListString + ")"));
		}
	}

}
