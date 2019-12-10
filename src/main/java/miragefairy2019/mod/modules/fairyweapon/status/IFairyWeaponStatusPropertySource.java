package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.FairyType;
import net.minecraft.util.text.TextFormatting;

public interface IFairyWeaponStatusPropertySource
{

	public String getLocalizedName();

	public TextFormatting getTextColor();

	/**
	 * コストによって重みづけされる必要がある値を取得します。
	 */
	public double raw(FairyType fairyType);

	/**
	 * コストによって重みづけされた値を取得します。
	 */
	public double get(FairyType fairyType);

}
