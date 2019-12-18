package miragefairy2019.mod.modules.fairycrystal;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;

import miragefairy2019.mod.api.fairycrystal.DropFixed;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.api.fairycrystal.RightClickDrops;
import mirrg.boron.util.suppliterator.ISuppliterator;

public class VariantFairyCrystalChristmas extends VariantFairyCrystal
{

	public VariantFairyCrystalChristmas(String registryName, String unlocalizedName, String oreName)
	{
		super(registryName, unlocalizedName, oreName);
	}

	@Override
	public FairyCrystalDropper getDropper()
	{
		FairyCrystalDropper self = super.getDropper();
		return new FairyCrystalDropper() {
			@Override
			public ISuppliterator<IRightClickDrop> getDropList()
			{
				return self.getDropList()
					.after(RightClickDrops.world(new DropFixed(santaclaus[0].createItemStack(), 0.1), (w, bp) -> bp.getDistance(207, 64, -244) < 32))
					.after(RightClickDrops.world(new DropFixed(christmas[0].createItemStack(), 0.01), (w, bp) -> bp.getDistance(207, 64, -244) < 32));
			}
		};
	}

}
