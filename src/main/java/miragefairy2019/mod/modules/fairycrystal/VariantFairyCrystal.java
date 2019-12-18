package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.lib.multi.ItemVariant;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class VariantFairyCrystal extends ItemVariant
{

	public final String registryName;
	public final String unlocalizedName;
	public final String oreName;

	public VariantFairyCrystal(String registryName, String unlocalizedName, String oreName)
	{
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		this.oreName = oreName;
	}

	public FairyCrystalDropper getDropper()
	{
		return new FairyCrystalDropper() {
			@Override
			public ISuppliterator<IRightClickDrop> getDropList()
			{
				return ISuppliterator.ofIterable(ApiFairyCrystal.dropsFairyCrystal);
			}
		};
	}

}
