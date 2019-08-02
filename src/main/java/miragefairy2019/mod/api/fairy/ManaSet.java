package miragefairy2019.mod.api.fairy;

public final class ManaSet
{

	public final double shine;
	public final double fire;
	public final double wind;
	public final double gaia;
	public final double aqua;
	public final double dark;

	public final double max;
	public final double sum;

	public ManaSet(
		double shine,
		double fire,
		double wind,
		double gaia,
		double aqua,
		double dark)
	{
		this.shine = shine;
		this.fire = fire;
		this.wind = wind;
		this.gaia = gaia;
		this.aqua = aqua;
		this.dark = dark;

		max = Math.max(Math.max(Math.max(shine, fire), Math.max(wind, gaia)), Math.max(aqua, dark));
		sum = shine + fire + wind + gaia + aqua + dark;
	}

	public double get(EnumManaType manaType)
	{
		switch (manaType) {
			case shine:
				return shine;
			case fire:
				return fire;
			case wind:
				return wind;
			case gaia:
				return gaia;
			case aqua:
				return aqua;
			case dark:
				return dark;
			default:
				throw new IllegalArgumentException();
		}
	}

	public double sum(double rateShine, double rateFire, double rateWind, double rateGaia, double rateAqua, double rateDark)
	{
		return shine * rateShine
			+ fire * rateFire
			+ wind * rateWind
			+ gaia * rateGaia
			+ aqua * rateAqua
			+ dark * rateDark;
	}

}
