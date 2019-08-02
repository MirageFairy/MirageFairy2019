package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantMirageFairy extends ItemVariant
{

	public final FairyType type;

	public VariantMirageFairy(FairyType type)
	{
		this.type = type;
	}

	public String getOreName()
	{
		return "mirageFairy" + Utils.toUpperCaseHead(type.name);
	}

	public String getRegistryName()
	{
		return "mirage_fairy_" + type.name;
	}

	public String getUnlocalizedName()
	{
		return "mirageFairy2019.mirageFairy." + type.name;
	}

}
