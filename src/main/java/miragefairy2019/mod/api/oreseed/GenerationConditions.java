package miragefairy2019.mod.api.oreseed;

public class GenerationConditions
{

	public static IGenerationCondition minY(int minY)
	{
		return (type, shape, world, pos) -> pos.getY() >= minY;
	}

	public static IGenerationCondition maxY(int maxY)
	{
		return (type, shape, world, pos) -> pos.getY() <= maxY;
	}

}
