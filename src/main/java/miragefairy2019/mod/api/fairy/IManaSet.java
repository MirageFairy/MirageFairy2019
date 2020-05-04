package miragefairy2019.mod.api.fairy;

public interface IManaSet
{

	public double getShine();

	public double getFire();

	public double getWind();

	public double getGaia();

	public double getAqua();

	public double getDark();

	public default double getMana(IManaType manaType)
	{
		if (manaType.equals(ManaTypes.shine.get())) return getShine();
		if (manaType.equals(ManaTypes.fire.get())) return getFire();
		if (manaType.equals(ManaTypes.wind.get())) return getWind();
		if (manaType.equals(ManaTypes.gaia.get())) return getGaia();
		if (manaType.equals(ManaTypes.aqua.get())) return getAqua();
		if (manaType.equals(ManaTypes.dark.get())) return getDark();
		return 0;
	}

	public default double getMax()
	{
		double max = 0;
		if (getShine() > max) max = getShine();
		if (getFire() > max) max = getFire();
		if (getWind() > max) max = getWind();
		if (getGaia() > max) max = getGaia();
		if (getAqua() > max) max = getAqua();
		if (getDark() > max) max = getDark();
		return max;
	}

	public default double getSum()
	{
		return getShine() + getFire() + getWind() + getGaia() + getAqua() + getDark();
	}

	public default double sum(double rateShine, double rateFire, double rateWind, double rateGaia, double rateAqua, double rateDark)
	{
		return getShine() * rateShine
			+ getFire() * rateFire
			+ getWind() * rateWind
			+ getGaia() * rateGaia
			+ getAqua() * rateAqua
			+ getDark() * rateDark;
	}

}
