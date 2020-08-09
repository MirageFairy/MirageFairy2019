package miragefairy2019.mod.modules.oreseed;

import net.minecraft.util.IStringSerializable;

public enum EnumVariantOreSeed implements IStringSerializable
{
	TINY(0, "tiny", "tiny"),
	LAPIS(1, "lapis", "lapis"),
	DIAMOND(2, "diamond", "diamond"),
	IRON(3, "iron", "iron"),
	MEDIUM(4, "medium", "medium"),
	LARGE(5, "large", "large"),
	COAL(6, "coal", "coal"),
	HUGE(7, "huge", "huge"),

	STRING(8, "string", "string"),
	HORIZONTAL(9, "horizontal", "horizontal"),
	VERTICAL(10, "vertical", "vertical"),
	POINT(11, "point", "point"),
	STAR(12, "star", "star"),
	RING(13, "ring", "ring"),
	PYRAMID(14, "pyramid", "pyramid"),
	CUBE(15, "cube", "cube"),

	;

	//

	private static final EnumVariantOreSeed[] META_LOOKUP;
	static {
		META_LOOKUP = new EnumVariantOreSeed[EnumVariantOreSeed.values().length];
		EnumVariantOreSeed[] types = EnumVariantOreSeed.values();
		for (int i = 0; i < types.length; i++) {
			EnumVariantOreSeed.META_LOOKUP[types[i].metadata] = types[i];
		}
	}

	public static EnumVariantOreSeed byMetadata(int metadata)
	{
		if (metadata < 0 || metadata >= META_LOOKUP.length) metadata = 0;
		return META_LOOKUP[metadata];
	}

	//

	public final int metadata;
	public final String resourceName;
	public final String unlocalizedName;

	private EnumVariantOreSeed(int metadata, String resourceName, String unlocalizedName)
	{
		this.metadata = metadata;
		this.resourceName = resourceName;
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String toString()
	{
		return this.resourceName;
	}

	@Override
	public String getName()
	{
		return this.resourceName;
	}

}
