package miragefairy2019.mod.api.fairy;

public final class MirageFairyType
{

	public final String modid;
	public final int id;
	public final String name;
	public final int rare;
	public final int rank;
	public final double cost;
	public final MirageFairyManaSet manaSet;
	public final MirageFairyAbilitySet abilitySet;
	public final MirageFairyColorSet colorSet;

	public MirageFairyType(
		String modid,
		int id,
		String name,
		int rare,
		int rank,
		double cost,
		MirageFairyManaSet manaSet,
		MirageFairyAbilitySet abilitySet,
		MirageFairyColorSet colorSet)
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
