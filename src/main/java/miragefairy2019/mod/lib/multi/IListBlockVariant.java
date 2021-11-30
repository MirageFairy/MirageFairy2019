package miragefairy2019.mod.lib.multi;

import java.util.List;

public interface IListBlockVariant<V extends IBlockVariant> {

    public default int getDefaultMetadata() {
        return 0;
    }

    public V byMetadata(int metadata);

    public List<V> getBlockVariants();

}
