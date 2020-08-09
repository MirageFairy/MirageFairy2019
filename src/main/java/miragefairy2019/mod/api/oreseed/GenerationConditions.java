package miragefairy2019.mod.api.oreseed;

public class GenerationConditions
{

	public static IGenerationCondition minY(int minY)
	{
		return t -> t.y.getY() >= minY;
	}

	public static IGenerationCondition maxY(int maxY)
	{
		return t -> t.y.getY() <= maxY;
	}

}
