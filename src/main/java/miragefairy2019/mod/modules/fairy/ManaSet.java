package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.api.fairy.IManaSet;

public final class ManaSet implements IManaSet
{

	public final double shine;
	public final double fire;
	public final double wind;
	public final double gaia;
	public final double aqua;
	public final double dark;

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
	}

	@Override
	public double getShine()
	{
		return shine;
	}

	@Override
	public double getFire()
	{
		return fire;
	}

	@Override
	public double getWind()
	{
		return wind;
	}

	@Override
	public double getGaia()
	{
		return gaia;
	}

	@Override
	public double getAqua()
	{
		return aqua;
	}

	@Override
	public double getDark()
	{
		return dark;
	}

}
