package miragefairy2019.mod.lib.multi;

public interface IListBlockVariant<V extends IBlockVariant> extends Iterable<V>
{

	public default int getDefaultMetadata()
	{
		return 0;
	}

	public V byMetadata(int metadata);

}
