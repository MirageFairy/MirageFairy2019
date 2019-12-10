package miragefairy2019.mod.modules.fairyweapon.status;

import miragefairy2019.mod.api.fairy.FairyType;
import mirrg.boron.util.suppliterator.ISuppliterator;

public interface IFairyWeaponStatusProperty<T>
{

	public T get(FairyType fairyType);

	public ISuppliterator<IFairyWeaponStatusPropertySource> getSources();

	public String getLocalizedName();

	public String getString(FairyType fairyType);

	public String getLocalizedSourceListString();

}
