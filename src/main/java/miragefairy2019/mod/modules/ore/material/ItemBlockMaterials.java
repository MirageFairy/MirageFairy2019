package miragefairy2019.mod.modules.ore.material;

import miragefairy2019.mod.lib.multi.ItemBlockMulti;
import net.minecraft.item.ItemStack;

public class ItemBlockMaterials<V extends IBlockVariantMaterials> extends ItemBlockMulti<BlockMaterials<V>, V> {

    public ItemBlockMaterials(BlockMaterials<V> block) {
        super(block);
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        return block2.getVariant(itemStack.getMetadata()).getBurnTime();
    }

}
