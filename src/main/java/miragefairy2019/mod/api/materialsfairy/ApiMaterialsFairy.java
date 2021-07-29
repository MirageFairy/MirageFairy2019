package miragefairy2019.mod.api.materialsfairy;

import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.materialsfairy.ModuleMaterialsFairy;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ApiMaterialsFairy
{

	public static Block blockTwinkleStone;
	public static ItemBlock itemBlockTwinkleStone;

	public static void init(EventRegistryMod erMod)
	{
		ModuleMaterialsFairy.init(erMod);
	}

}
