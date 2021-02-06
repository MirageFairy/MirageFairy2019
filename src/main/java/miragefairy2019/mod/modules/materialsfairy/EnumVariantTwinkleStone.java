package miragefairy2019.mod.modules.materialsfairy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import miragefairy2019.mod.lib.multi.IBlockVariant;
import miragefairy2019.mod.lib.multi.IListBlockVariant;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.IStringSerializable;

public enum EnumVariantTwinkleStone implements IStringSerializable, IBlockVariant
{
	WHITE(0, "white_twinkle_stone", "twinkleStoneWhite", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneWhite"), 15),
	ORANGE(1, "orange_twinkle_stone", "twinkleStoneOrange", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneOrange"), 15),
	MAGENTA(2, "magenta_twinkle_stone", "twinkleStoneMagenta", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneMagenta"), 11),
	LIGHT_BLUE(3, "light_blue_twinkle_stone", "twinkleStoneLightBlue", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneLightBlue"), 15),
	YELLOW(4, "yellow_twinkle_stone", "twinkleStoneYellow", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneYellow"), 15),
	LIME(5, "lime_twinkle_stone", "twinkleStoneLime", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneLime"), 15),
	PINK(6, "pink_twinkle_stone", "twinkleStonePink", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStonePink"), 15),
	GRAY(7, "gray_twinkle_stone", "twinkleStoneGray", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneGray"), 11),
	SILVER(8, "silver_twinkle_stone", "twinkleStoneSilver", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneSilver"), 15),
	CYAN(9, "cyan_twinkle_stone", "twinkleStoneCyan", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneCyan"), 11),
	PURPLE(10, "purple_twinkle_stone", "twinkleStonePurple", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStonePurple"), 15),
	BLUE(11, "blue_twinkle_stone", "twinkleStoneBlue", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneBlue"), 11),
	BROWN(12, "brown_twinkle_stone", "twinkleStoneBrown", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneBrown"), 11),
	GREEN(13, "green_twinkle_stone", "twinkleStoneGreen", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneGreen"), 11),
	RED(14, "red_twinkle_stone", "twinkleStoneRed", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneRed"), 11),
	BLACK(15, "black_twinkle_stone", "twinkleStoneBlack", o("mirageFairy2019TwinkleStone", "mirageFairy2019TwinkleStoneBlack"), 3),
	;

	//

	public static final IListBlockVariant<EnumVariantTwinkleStone> variantList = new IListBlockVariant<EnumVariantTwinkleStone>() {
		private final EnumVariantTwinkleStone[] values = EnumVariantTwinkleStone.values();
		private final Map<Integer, EnumVariantTwinkleStone> metaLookup = new HashMap<>();
		{
			for (EnumVariantTwinkleStone variant : EnumVariantTwinkleStone.values()) {
				if (metaLookup.containsKey(variant.metadata)) throw new IllegalArgumentException();
				metaLookup.put(variant.metadata, variant);
			}
		}

		@Override
		public EnumVariantTwinkleStone byMetadata(int metadata)
		{
			return metaLookup.getOrDefault(metadata, values[0]);
		}

		@Override
		public Iterator<EnumVariantTwinkleStone> iterator()
		{
			return ISuppliterator.ofObjArray(values).iterator();
		}
	};

	private static String[] o(String... strings)
	{
		return strings;
	}

	//

	public final int metadata;
	public final String resourceName;
	public final String unlocalizedName;
	public final String[] oreNames;
	public final int lightValue;

	private EnumVariantTwinkleStone(int metadata, String resourceName, String unlocalizedName, String[] oreNames, int lightValue)
	{
		this.metadata = metadata;
		this.resourceName = resourceName;
		this.unlocalizedName = unlocalizedName;
		this.oreNames = oreNames;
		this.lightValue = lightValue;
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

	@Override
	public int getMetadata()
	{
		return metadata;
	}

	@Override
	public String getResourceName()
	{
		return resourceName;
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}

}
