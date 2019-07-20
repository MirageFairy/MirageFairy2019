package miragefairy2019.mod.modules.ore;

public interface IOreVariantList<V> extends Iterable<V>
{

	public default int getDefaultMetadata()
	{
		return 0;
	}

	public V byMetadata(int metadata);

}
