package miragefairy2019.mod.modules.ore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.IStringSerializable;

public enum EnumVariantOre1 implements IStringSerializable, IOreVariant
{
	APATITE_ORE(0, "apatite_ore", "oreApatite", 1),
	FLUORITE_ORE(1, "fluorite_ore", "oreFluorite", 2),
	;

	//

	public static final IOreVariantList<EnumVariantOre1> variantList = new IOreVariantList<EnumVariantOre1>() {
		private final EnumVariantOre1[] values = EnumVariantOre1.values();
		private final Map<Integer, EnumVariantOre1> metaLookup = new HashMap<>();
		{
			for (EnumVariantOre1 variant : EnumVariantOre1.values()) {
				if (metaLookup.containsKey(variant.metadata)) throw new IllegalArgumentException();
				metaLookup.put(variant.metadata, variant);
			}
		}

		@Override
		public EnumVariantOre1 byMetadata(int metadata)
		{
			return metaLookup.getOrDefault(metadata, values[0]);
		}

		@Override
		public Iterator<EnumVariantOre1> iterator()
		{
			return ISuppliterator.ofObjArray(values).iterator();
		}
	};

	//

	public final int metadata;
	public final String resourceName;
	public final String unlocalizedName;
	public final int harvestLevel;

	private EnumVariantOre1(int metadata, String resourceName, String unlocalizedName, int harvestLevel)
	{
		this.metadata = metadata;
		this.resourceName = resourceName;
		this.unlocalizedName = unlocalizedName;
		this.harvestLevel = harvestLevel;
	}

	public String toString()
	{
		return this.resourceName;
	}

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

	@Override
	public int getHarvestLevel()
	{
		return harvestLevel;
	}

}
