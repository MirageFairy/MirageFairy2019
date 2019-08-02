package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.MirageFairyType;
import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantMirageFairy extends ItemVariant
{

	public final MirageFairyType type;

	public VariantMirageFairy(MirageFairyType type)
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
		return "fairy.mirageFairy." + type.name;
	}

}
