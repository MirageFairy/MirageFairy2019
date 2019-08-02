package miragefairy2019.mod.api.fairy;

public final class FairyType
{

	public final String modid;
	public final int id;
	public final String name;
	public final int rare;
	public final int rank;
	public final double cost;
	public final ManaSet manaSet;
	public final AbilitySet abilitySet;
	public final ColorSet colorSet;

	public FairyType(
		String modid,
		int id,
		String name,
		int rare,
		int rank,
		double cost,
		ManaSet manaSet,
		AbilitySet abilitySet,
		ColorSet colorSet)
	{
		this.modid = modid;
		this.id = id;
		this.name = name;
		this.rare = rare;
		this.rank = rank;
		this.cost = cost;
		this.manaSet = manaSet;
		this.abilitySet = abilitySet;
		this.colorSet = colorSet;
	}

}
