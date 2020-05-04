package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.IFairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public interface IFairyWeaponStatusProperty<T>
{

	public T get(IFairyType fairyType);

	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources();

	public String getLocalizedName();

	public String getString(IFairyType fairyType);

	public String getLocalizedSourceListString();

}
