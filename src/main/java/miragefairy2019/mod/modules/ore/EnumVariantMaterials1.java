package miragefairy2019.mod.modules.ore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.util.IStringSerializable;

public enum EnumVariantMaterials1 implements IStringSerializable, IBlockVariantMaterials
{
	APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", 1, 0),
	FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", 2, 0),
	SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", 1, 0),
	CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", 2, 0),
	MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", 3, 0),
	MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", 1, 0),
	PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", 2, 0),
	SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", 1, 0),
	CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", 0, 20 * 10 * 8 * 9),
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
	public final String oreName;
	public final int harvestLevel;
	public final int burnTime;

	private EnumVariantMaterials1(int metadata, String resourceName, String unlocalizedName, String oreName, int harvestLevel, int burnTime)
	{
		this.metadata = metadata;
		this.resourceName = resourceName;
		this.unlocalizedName = unlocalizedName;
		this.oreName = oreName;
		this.harvestLevel = harvestLevel;
		this.burnTime = burnTime;
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

	@Override
	public int getHarvestLevel()
	{
		return harvestLevel;
	}

	@Override
	public int getBurnTime()
	{
		return burnTime;
	}

}
