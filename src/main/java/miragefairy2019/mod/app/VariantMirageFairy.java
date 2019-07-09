package miragefairy2019.mod.app;

import miragefairy2019.mod.lib.multi.ItemVariant;

public class VariantMirageFairy extends ItemVariant
{

	private String registryName;
	private String oreName;
	private String name;
	private int rare;
	private int rank;
	private double cost;
	private double shine;
	private double fire;
	private double wind;
	private double gaia;
	private double aqua;
	private double dark;

	public VariantMirageFairy(
		String registryName,
		String oreName,
		String name,
		int rare,
		int rank,
		double cost,
		double shine,
		double fire,
		double wind,
		double gaia,
		double aqua,
		double dark)
	{
		this.registryName = registryName;
		this.oreName = oreName;
		this.name = name;
		this.rare = rare;
		this.rank = rank;
		this.cost = cost;
		this.shine = shine;
		this.fire = fire;
		this.wind = wind;
		this.gaia = gaia;
		this.aqua = aqua;
		this.dark = dark;
	}

	public String getName()
	{
		return name;
	}

	public int getRare()
	{
		return rare;
	}

	public int getRank()
	{
		return rank;
	}

	public double getCost()
	{
		return cost;
	}

	public double getShine()
	{
		return shine;
	}

	public double getFire()
	{
		return fire;
	}

	public double getWind()
	{
		return wind;
	}

	public double getGaia()
	{
		return gaia;
	}

	public double getAqua()
	{
		return aqua;
	}

	public double getDark()
	{
		return dark;
	}

	@Override
	public String getRegistryName()
	{
		return registryName;
	}

	@Override
	public String getOreName()
	{
		return oreName;
	}

}
