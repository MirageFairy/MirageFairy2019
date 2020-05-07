package miragefairy2019.mod.lib.multi;

import miragefairy2019.mod.api.composite.ApiComposite;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.composite.IComposite;

public class ItemVariantMaterial extends ItemVariant
{

	public final String registryName;
	public final String unlocalizedName;

	public ItemVariantMaterial(String registryName, String unlocalizedName)
	{
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
	}

	public String getUnlocalizedName()
	{
		return "item." + unlocalizedName;
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
