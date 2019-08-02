package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantFairy extends ItemVariant
{

	public final FairyType type;

	public VariantFairy(FairyType type)
	{
		this.type = type;
	}

}
