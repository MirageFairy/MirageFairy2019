package miragefairy2019.mod.modules.ore.material

import miragefairy2019.mod.lib.multi.ItemBlockMulti
import net.minecraft.item.ItemStack

class ItemBlockMaterials<V : IBlockVariantMaterials>(block: BlockMaterials<V>) : ItemBlockMulti<BlockMaterials<V>, V>(block) {
    override fun getItemBurnTime(itemStack: ItemStack) = block2.getVariant(itemStack.metadata).burnTime
}
