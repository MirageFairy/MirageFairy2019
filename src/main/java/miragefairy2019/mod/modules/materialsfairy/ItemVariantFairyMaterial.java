package miragefairy2019.mod.modules.materialsfairy;

import miragefairy2019.mod.api.composite.ApiComposite;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.composite.IComposite;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;

public class ItemVariantFairyMaterial extends ItemVariantMaterial
{

	public final int tier;

	public ItemVariantFairyMaterial(String registryName, String unlocalizedName, int tier)
	{
		super(registryName, unlocalizedName);
		this.tier = tier;
	}

	//

	private IComposite composite = ApiComposite.composite();

	public void addComponent(IComponentInstance componentInstance)
	{
		composite = composite.add(componentInstance);
	}

	public void addComponent(IComposite composite)
	{
		this.composite = this.composite.add(composite);
	}

	public IComposite getComposite()
	{
		return composite;
	}

}
