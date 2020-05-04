package miragefairy2019.mod.modules.fairyweapon.status;

import java.util.List;

import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.lib.UtilsMinecraft;
import mirrg.boron.util.suppliterator.ISuppliterator;

public abstract class FairyWeaponStatusPropertyBase<T> implements IFairyWeaponStatusProperty<T>
{

	private final String unlocalizedName;
	private final List<IFairyWeaponStatusPropertySource> sources;
	private final IFairyWeaponStatusPropertyView<T> view;

	public FairyWeaponStatusPropertyBase(String unlocalizedName, List<IFairyWeaponStatusPropertySource> sources, IFairyWeaponStatusPropertyView<T> view)
	{
		this.unlocalizedName = unlocalizedName;
		this.sources = sources;
		this.view = view;
	}

	@Override
	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources()
	{
		return ISuppliterator.ofIterable(sources);
	}

	@Override
	public String getLocalizedName()
	{
		return UtilsMinecraft.translateToLocal(unlocalizedName);
	}

	@Override
	public String getString(IFairyType fairyType)
	{
		return view.toString(get(fairyType));
	}

	@Override
	public String getLocalizedSourceListString()
	{
		return getSources().map(s -> s.getLocalizedName()).join(", ");
	}

}
