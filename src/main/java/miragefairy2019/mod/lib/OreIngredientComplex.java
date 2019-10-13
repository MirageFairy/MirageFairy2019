package miragefairy2019.mod.lib;

import net.minecraftforge.oredict.OreIngredient;

public class OreIngredientComplex extends OreIngredient
{

	public OreIngredientComplex(String ore)
	{
		super(ore);
	}

	@Override
	public boolean isSimple()
	{
		// これがtrueになっていると、subItemsを参照するようになるのでクラフティングツールが反応しなくなる
		return false;
	}

}
