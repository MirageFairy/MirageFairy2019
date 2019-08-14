package miragefairy2019.mod.modules.ore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.IStringSerializable;

public enum EnumVariantMaterials1 implements IStringSerializable, IBlockVariant
{
	APATITE_BLOCK(0, "apatite_block", "blockApatite", 1),
	FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", 2),
	SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", 1),
	CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", 2),
	MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", 3),
	MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", 1),
	;

	//

	public static final IOreVariantList<EnumVariantMaterials1> variantList = new IOreVariantList<EnumVariantMaterials1>() {
		private final EnumVariantMaterials1[] values = EnumVariantMaterials1.values();
		private final Map<Integer, EnumVariantMaterials1> metaLookup = new HashMap<>();
		{
			for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
				if (metaLookup.containsKey(variant.metadata)) throw new IllegalArgumentException();
				metaLookup.put(variant.metadata, variant);
			}
		}

		@Override
		public EnumVariantMaterials1 byMetadata(int metadata)
		{
			return metaLookup.getOrDefault(metadata, values[0]);
		}

		@Override
		public Iterator<EnumVariantMaterials1> iterator()
		{
			return ISuppliterator.ofObjArray(values).iterator();
		}
	};

	//

	public final int metadata;
	public final String resourceName;
	public final String unlocalizedName;
	public final int harvestLevel;

	private EnumVariantMaterials1(int metadata, String resourceName, String unlocalizedName, int harvestLevel)
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
