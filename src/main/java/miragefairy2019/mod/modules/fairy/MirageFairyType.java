package miragefairy2019.mod.modules.fairy;

public final class MirageFairyType
{

	public final String modid;
	public final String name;
	public final int rare;
	public final int rank;
	public final double cost;
	public final MirageFairyManaSet manaSet;
	public final MirageFairyAbilitySet abilitySet;
	public final MirageFairyColorSet colorSet;

	public MirageFairyType(
		String modid,
		String name,
		int rare,
		int rank,
		double cost,
		MirageFairyManaSet manaSet,
		MirageFairyAbilitySet abilitySet,
		MirageFairyColorSet colorSet)
	{
		this.modid = modid;
		this.name = name;
		this.rare = rare;
		this.rank = rank;
		this.cost = cost;
		this.manaSet = manaSet;
		this.abilitySet = abilitySet;
		this.colorSet = colorSet;
	}

}
