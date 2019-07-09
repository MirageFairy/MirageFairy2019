package miragefairy2019.mod.app;

public final class MirageFairyType
{

	public final String name;
	public final int rare;
	public final int rank;
	public final double cost;
	public final double shine;
	public final double fire;
	public final double wind;
	public final double gaia;
	public final double aqua;
	public final double dark;
	public final MirageFairyColorSet colorSet;

	public MirageFairyType(
		String name,
		int rare,
		int rank,
		double cost,
		double shine,
		double fire,
		double wind,
		double gaia,
		double aqua,
		double dark,
		MirageFairyColorSet colorSet)
	{
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
		this.colorSet = colorSet;
	}

}
